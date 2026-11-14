package com.aizenrusty.library_management.book.service.impl;

import com.aizenrusty.library_management.book.dto.BookInputDto;
import com.aizenrusty.library_management.book.dto.BookOutputDto;
import com.aizenrusty.library_management.book.entity.Book;
import com.aizenrusty.library_management.book.entity.Users;
import com.aizenrusty.library_management.book.repository.BookRepository;
import com.aizenrusty.library_management.book.repository.UserRepository;
import com.aizenrusty.library_management.book.service.BookService;
import com.aizenrusty.library_management.exception.GlobalExceptionHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createBook(BookInputDto dto) {

        Users author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "User not found with id " + dto.getAuthorId()));

        if (author.getRole() == Users.Role.USER) {
            author.setRole(Users.Role.AUTHOR);
        }

        // 3. Build the book
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        book.setLanguage(dto.getLanguage());
        book.setGenre(dto.getGenre());
        book.setPages(dto.getPages());
        book.setPublicationDate(LocalDate.now());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getTotalCopies());

        bookRepository.save(book);
    }

    @Override
    public List<BookOutputDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> {
            BookOutputDto dto = new BookOutputDto();
            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setAuthorName(book.getAuthor().getName());
            dto.setLanguage(book.getLanguage().name());
            dto.setGenre(book.getGenre().name());
            dto.setPages(book.getPages());
            dto.setPublicationDate(book.getPublicationDate());
            dto.setTotalCopies(book.getTotalCopies());
            dto.setAvailableCopies(book.getAvailableCopies());
            return dto;
        }).toList();
    }

    @Override
    public BookOutputDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "Book not found with id " + id));
        BookOutputDto dto = new BookOutputDto();
        dto.setTitle(book.getTitle());
        dto.setId(book.getId());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setLanguage(book.getLanguage().name());
        dto.setGenre(book.getGenre().name());
        dto.setPages(book.getPages());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        return dto;
    }

    @Override
    @Transactional
    public void updateBook(Long id, BookInputDto bookInputDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "Book not found with id " + id));

        Users author = userRepository.findById(bookInputDto.getAuthorId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException(
                        "User not found with id " + bookInputDto.getAuthorId()));

        if (author.getRole() == Users.Role.USER) {
            author.setRole(Users.Role.AUTHOR);
        }

        book.setTitle(bookInputDto.getTitle());
        book.setAuthor(author);
        book.setLanguage(bookInputDto.getLanguage());
        book.setGenre(bookInputDto.getGenre());
        book.setPages(bookInputDto.getPages());
        bookRepository.save(book);
    }
}
