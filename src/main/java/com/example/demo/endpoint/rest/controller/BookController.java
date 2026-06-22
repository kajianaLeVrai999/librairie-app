package com.example.demo.endpoint.rest.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {


private final BookService bookService;

public BookController(BookService bookService) {
    this.bookService = bookService;
}

// GET /books
@GetMapping
public ResponseEntity<List<Book>> getAllBooks() {
    return ResponseEntity.ok(bookService.getAll());
}

// GET /books/{id}
@GetMapping("/{id}")
public ResponseEntity<?> getBookById(@PathVariable Integer id) {
    try {
        return ResponseEntity.ok(bookService.getById(id));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// POST /books
@PostMapping
public ResponseEntity<?> createBook(@RequestBody Book book) {
    try {
        Book created = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// PUT /books/{id}
@PutMapping("/{id}")
public ResponseEntity<?> updateBook(
        @PathVariable Integer id,
        @RequestBody Book book) {

    try {
        Book updated = bookService.update(id, book);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// DELETE /books/{id}
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
    try {
        bookService.getById(id);
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// GET /books/search?keyword=java
@GetMapping("/search")
public ResponseEntity<List<Book>> searchBooks(
        @RequestParam String keyword) {

    return ResponseEntity.ok(bookService.search(keyword));
}

}
