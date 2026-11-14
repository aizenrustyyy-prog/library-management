package com.aizenrusty.library_management.borrow.service;

import com.aizenrusty.library_management.borrow.entity.Borrow;
import jakarta.validation.Valid;

import java.util.List;

public interface BorrowService {
    void issue(Long bookId, Long userId);

    void returnBook(Long id);

    List<Borrow> getBorrowingsByUserId(Long userId);
}
