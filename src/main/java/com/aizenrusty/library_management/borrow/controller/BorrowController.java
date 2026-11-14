package com.aizenrusty.library_management.borrow.controller;

import com.aizenrusty.library_management.borrow.entity.Borrow;
import com.aizenrusty.library_management.borrow.service.BorrowService;
import com.aizenrusty.library_management.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/issue/{bookId}/{userId}")
    public ResponseEntity<ApiResponse<String>> issue(@PathVariable Long bookId, @PathVariable Long userId) {
        borrowService.issue(bookId, userId);
        return ResponseEntity.ok(ApiResponse.success("Book issued successfully"));
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Borrow>>> getBorrowingsByUserId(@PathVariable Long userId) {
        List<Borrow> borrowings = borrowService.getBorrowingsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(borrowings));
    }

}
