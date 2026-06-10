package com.example.demo.repository;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    List<BookCopy> findByStatus(CopyStatus status);
    List<BookCopy> findByBookId(Integer bookId);
    long countByStatus(CopyStatus status);
}