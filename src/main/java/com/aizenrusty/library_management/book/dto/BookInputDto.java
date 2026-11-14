package com.aizenrusty.library_management.book.dto;

import com.aizenrusty.library_management.book.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BookInputDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Language is required")
    private Book.Language language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Book.Language getLanguage() {
        return language;
    }

    public void setLanguage(Book.Language language) {
        this.language = language;
    }

    public Book.Genre getGenre() {
        return genre;
    }

    public void setGenre(Book.Genre genre) {
        this.genre = genre;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    @NotNull(message = "Genre is required")
    private Book.Genre genre;

    @NotNull(message = "Pages is required")
    @Min(value = 1, message = "Pages must be at least 1")
    private Integer pages;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

}
