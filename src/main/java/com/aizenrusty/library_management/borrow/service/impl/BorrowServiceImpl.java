package com.aizenrusty.library_management.borrow.service.impl;

import com.aizenrusty.library_management.book.entity.Book;
import com.aizenrusty.library_management.book.entity.Users;
import com.aizenrusty.library_management.book.repository.BookRepository;
import com.aizenrusty.library_management.book.repository.UserRepository;
import com.aizenrusty.library_management.borrow.entity.Borrow;
import com.aizenrusty.library_management.borrow.repository.BorrowRepository;
import com.aizenrusty.library_management.borrow.service.BorrowService;
import com.aizenrusty.library_management.exception.GlobalExceptionHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class BorrowServiceImpl implements BorrowService {


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
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUser(user);
        borrow.setIssueDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(14));
        borrow.setStatus(Borrow.Status.ISSUED);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
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
        borrowRepository.save(borrow);
    }

    @Override
    public List<Borrow> getBorrowingsByUserId(Long userId) {
        return borrowRepository.findByUserId(userId);
    }

}
