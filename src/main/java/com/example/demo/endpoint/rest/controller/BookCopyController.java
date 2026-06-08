package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.BookCopy;
import com.example.demo.service.BookCopyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/copies")
public class BookCopyController {

  private final BookCopyService service;

  public BookCopyController(BookCopyService service) {
    this.service = service;
  }

  @PostMapping("/book/{bookId}")
  public BookCopy createCopy(@PathVariable Integer bookId, @RequestBody BookCopy copy) {

    return service.create(bookId, copy);
  }
}
