package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.GoogleBooksService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final AuthorRepository authorRepository;
  private final GoogleBooksService googleBooksService;

  public BookController(
      BookRepository bookRepository,
      CategoryRepository categoryRepository,
      AuthorRepository authorRepository,
      GoogleBooksService googleBooksService) {
    this.bookRepository = bookRepository;
    this.categoryRepository = categoryRepository;
    this.authorRepository = authorRepository;
    this.googleBooksService = googleBooksService;
  }

  @GetMapping
  public String listBooks(Model model) {
    List<Book> books = bookRepository.findAll();
    model.addAttribute("books", books);
    return "book-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("book", new Book());
    model.addAttribute("categories", categoryRepository.findAll());
    model.addAttribute("authors", authorRepository.findAll());
    return "book-form";
  }

  @PostMapping("/save")
  public String saveBook(@ModelAttribute Book book) {
    bookRepository.save(book);
    return "redirect:/books";
  }

  @GetMapping("/{id}")
  public String viewBook(@PathVariable Integer id, Model model) {
    Book book = bookRepository.findById(id).orElse(null);
    model.addAttribute("book", book);
    return "book-detail";
  }

  @GetMapping("/delete/{id}")
  public String deleteBook(@PathVariable Integer id) {
    bookRepository.deleteById(id);
    return "redirect:/books";
  }

  @PostMapping("/import-async")
  @ResponseBody
  public String importBookAsync(@RequestParam String isbn) {
    String cleanIsbn = isbn.replaceAll("[\\s-]", "");

    CompletableFuture<Book> future = googleBooksService.fetchAndSaveBookByIsbn(cleanIsbn);

    return "Import lancé en arrière-plan pour l'ISBN: "
        + cleanIsbn
        + ". La page va se rafraîchir automatiquement dans quelques secondes.";
  }
}
