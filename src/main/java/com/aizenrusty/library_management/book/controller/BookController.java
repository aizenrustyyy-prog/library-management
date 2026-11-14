package com.aizenrusty.library_management.book.controller;

import com.aizenrusty.library_management.book.dto.BookInputDto;
import com.aizenrusty.library_management.book.dto.BookOutputDto;
import com.aizenrusty.library_management.book.service.BookService;
import com.aizenrusty.library_management.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createBook(@Valid @RequestBody BookInputDto bookInputDto) {
        bookService.createBook(bookInputDto);
        return ResponseEntity.ok(ApiResponse.success("Book created successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BookOutputDto>>> getAllBooks() {
        return ResponseEntity.ok(ApiResponse.success(bookService.getAllBooks()));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<BookOutputDto>> getBookById(@PathVariable Long id) {
        BookOutputDto bookOutputDto = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(bookOutputDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateBook(@PathVariable Long id, @Valid @RequestBody BookInputDto bookInputDto) {
        bookService.updateBook(id, bookInputDto);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully"));
    }
}
