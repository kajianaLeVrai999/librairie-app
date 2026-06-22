package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.service.BookCopyService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {

  private final BookCopyService bookCopyService;

  public BookCopyController(BookCopyService bookCopyService) {
    this.bookCopyService = bookCopyService;
  }

  // GET /book-copies
  @GetMapping
  public ResponseEntity<List<BookCopy>> getAllCopies() {
    return ResponseEntity.ok(bookCopyService.getAll());
  }

  // GET /book-copies/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getCopyById(@PathVariable Integer id) {
    try {
      return ResponseEntity.ok(bookCopyService.getById(id));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /book-copies/book/{bookId}
  @PostMapping("/book/{bookId}")
  public ResponseEntity<?> createCopy(@PathVariable Integer bookId, @RequestBody BookCopy copy) {

    try {
      BookCopy created = bookCopyService.create(bookId, copy);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // POST /book-copies/book/{bookId}/multiple?quantity=5
  @PostMapping("/book/{bookId}/multiple")
  public ResponseEntity<?> createMultipleCopies(
      @PathVariable Integer bookId, @RequestParam int quantity) {

    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(bookCopyService.createMultiple(bookId, quantity));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // GET /book-copies/book/{bookId}
  @GetMapping("/book/{bookId}")
  public ResponseEntity<List<BookCopy>> getCopiesByBookId(@PathVariable Integer bookId) {

    return ResponseEntity.ok(bookCopyService.getByBookId(bookId));
  }

  // GET /book-copies/available
  @GetMapping("/available")
  public ResponseEntity<List<BookCopy>> getAvailableCopies() {
    return ResponseEntity.ok(bookCopyService.getAvailableCopies());
  }

  // PATCH /book-copies/{id}/status
  @PatchMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam CopyStatus status) {

    try {
      return ResponseEntity.ok(bookCopyService.updateStatus(id, status));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
    }
  }

  // GET /book-copies/book/{bookId}/available-count
  @GetMapping("/book/{bookId}/available-count")
  public ResponseEntity<Long> countAvailableCopies(@PathVariable Integer bookId) {

    return ResponseEntity.ok(bookCopyService.countAvailableByBookId(bookId));
  }

  // DELETE /book-copies/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCopy(@PathVariable Integer id) {
    try {
      bookCopyService.getById(id);
      bookCopyService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
