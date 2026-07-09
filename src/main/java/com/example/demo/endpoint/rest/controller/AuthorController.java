package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.service.AuthorService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  // GET /authors
  @GetMapping
  public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
    return ResponseEntity.ok(authorService.getAll());
  }

  // GET /authors/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getAuthorById(@PathVariable Integer id) {

    try {
      return ResponseEntity.ok(authorService.getById(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /authors
  @PostMapping
  public ResponseEntity<?> createAuthor(@RequestBody AuthorDTO dto) {

    try {
      AuthorDTO created = authorService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(created);

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // PUT /authors/{id}
  @PutMapping("/{id}")
  public ResponseEntity<?> updateAuthor(@PathVariable Integer id, @RequestBody AuthorDTO dto) {

    try {
      AuthorDTO updated = authorService.update(id, dto);

      return ResponseEntity.ok(updated);

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // DELETE /authors/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteAuthor(@PathVariable Integer id) {

    try {
      authorService.getById(id);
      authorService.delete(id);

      return ResponseEntity.noContent().build();

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
