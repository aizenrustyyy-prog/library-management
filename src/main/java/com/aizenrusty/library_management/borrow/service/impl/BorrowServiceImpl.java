package com.aizenrusty.library_management.borrow.service.impl;

import com.aizenrusty.library_management.book.entity.Book;
import com.aizenrusty.library_management.book.entity.Users;
import com.aizenrusty.library_management.book.repository.BookRepository;
import com.aizenrusty.library_management.book.repository.UserRepository;
import com.aizenrusty.library_management.borrow.entity.Borrow;
import com.aizenrusty.library_management.borrow.repository.BorrowRepository;
import com.aizenrusty.library_management.borrow.service.BorrowService;
import com.aizenrusty.library_management.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class BorrowServiceImpl implements BorrowService {

    @Value("${library.fine.per-day}")
    private BigDecimal finePerDay;
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void issue(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Book not found"));
        Users user = userRepository.findById(userId).orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("User not found"));
        if(book.getAvailableCopies() <= 0) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("No available copies of the book");
        }

        if(borrowRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, Borrow.Status.ISSUED)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User has already borrowed this book");
        }

        if(borrowRepository.countByUserIdAndStatus(userId, Borrow.Status.ISSUED) >= 3) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User has already borrowed maximum number of books");
        }

        if (borrowRepository.existsByUserIdAndFinePaidFalse(userId)) {
            throw new RuntimeException("Please pay your pending fine before borrowing");
        }

        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUser(user);
        borrow.setIssueDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(14));
        borrow.setStatus(Borrow.Status.ISSUED);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        borrow.setFinePaid(false);
        borrowRepository.save(borrow);
    }

    @Override
    @Transactional
    public void returnBook(Long id) {
        Borrow borrow = borrowRepository.findById(id).orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Borrow record not found"));
        if(borrow.getStatus() == Borrow.Status.RETURNED) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Book has already been returned");
        }
        borrow.setReturnDate(LocalDate.now());
        borrow.setStatus(Borrow.Status.RETURNED);
        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        LocalDate today = LocalDate.now();
        if (today.isAfter(borrow.getDueDate())) {
            long daysLate = DAYS.between(borrow.getDueDate(), today);
            BigDecimal fine = finePerDay.multiply(BigDecimal.valueOf(daysLate));
            borrow.setFineAmount(fine);
            borrow.setFinePaid(false);
        } else {
            borrow.setFineAmount(BigDecimal.ZERO);
            borrow.setFinePaid(true);
        }
        borrowRepository.save(borrow);
    }

    @Override
    public List<Borrow> getBorrowingsByUserId(Long userId) {
        return borrowRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void payFine(Long id) {
        Borrow borrow = borrowRepository.findById(id).orElseThrow(() -> new RuntimeException("No Fine Found"));
        if(borrow.getFineAmount() == null || borrow.getFineAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("No Fine Found");
        }
        if(borrow.isFinePaid()) {
            throw new RuntimeException("Fine already paid");
        }
        borrow.setFinePaid(true);
        borrowRepository.save(borrow);
    }
}
