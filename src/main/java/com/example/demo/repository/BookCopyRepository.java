package com.example.demo.repository;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
  List<BookCopy> findByStatus(CopyStatus status);

  List<BookCopy> findByBookId(Integer bookId);

  long countByStatus(CopyStatus status);

  @Query(
      """
SELECT COUNT(bc)
FROM BookCopy bc
WHERE bc.book.id = :bookId
AND bc.status <> com.example.demo.entity.CopyStatus.SOLD
""")
  long countStockByBookId(Integer bookId);

  @Query(
      """
SELECT COUNT(bc)
FROM BookCopy bc
WHERE bc.book.id = :bookId
AND bc.status = com.example.demo.entity.CopyStatus.AVAILABLE
""")
  long countAvailableStock(Integer bookId);

  @Query(
      """
SELECT bc.book
FROM BookCopy bc
WHERE bc.status = com.example.demo.entity.CopyStatus.AVAILABLE
GROUP BY bc.book
HAVING COUNT(bc) <= 3
""")
  List<Book> findBooksWithLowStock();
}
