package com.aizenrusty.library_management.book.service;

import com.aizenrusty.library_management.book.dto.BookInputDto;
import com.aizenrusty.library_management.book.dto.BookOutputDto;
import jakarta.validation.Valid;
import java.util.List;


public interface BookService {
    void createBook(@Valid BookInputDto bookInputDto);

    List<BookOutputDto> getAllBooks();

    BookOutputDto getBookById(Long id);

    void updateBook(Long id, @Valid BookInputDto bookInputDto);
}
