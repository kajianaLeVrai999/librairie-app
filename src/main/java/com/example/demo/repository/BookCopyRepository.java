package com.example.demo.repository;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
  List<BookCopy> findByStatus(CopyStatus status);

  List<BookCopy> findByBookId(Integer bookId);

  long countByStatus(CopyStatus status);
}
