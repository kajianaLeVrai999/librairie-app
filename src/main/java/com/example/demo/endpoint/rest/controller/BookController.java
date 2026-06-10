package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService service;

  public BookController(BookService service) {
    this.service = service;
  }

  @PostMapping
  public Book create(@RequestBody Book book) {
    return service.create(book);
  }

  @GetMapping
  public List<Book> getAll() {
    return service.getAll();
  }

  @GetMapping("/{id}")
  public Book getById(@PathVariable Integer id) {
    return service.getById(id);
  }

  @PutMapping("/{id}")
  public Book update(@PathVariable Integer id, @RequestBody Book book) {

    return service.update(id, book);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }

  @GetMapping("/search")
  public List<Book> search(@RequestParam String keyword) {

    return service.search(keyword);
  }
}
