package com.aizenrusty.library_management.borrow.repository;

import com.aizenrusty.library_management.borrow.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow,Long> {

    long countByUserIdAndStatus(Long userId, Borrow.Status status);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, Borrow.Status status);

    List<Borrow> findByUserId(Long userId);

    boolean existsByUserIdAndFinePaidFalse(Long userId);
}
