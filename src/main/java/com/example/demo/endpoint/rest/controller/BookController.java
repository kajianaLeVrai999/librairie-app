package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.service.BookService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    // GET /books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {

        return ResponseEntity.ok(bookService.getAll());
    }



    // GET /books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {

        try {

            return ResponseEntity.ok(bookService.getById(id));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    // POST /books
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO dto) {

        try {

            BookDTO created = bookService.create(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);


        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }



    // PUT /books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Integer id,
            @RequestBody BookDTO dto) {


        try {

            return ResponseEntity.ok(
                    bookService.update(id, dto)
            );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());


        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
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

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    // GET /books/search?keyword=java
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(
            @RequestParam String keyword) {


        return ResponseEntity.ok(
                bookService.search(keyword)
        );
    }
}