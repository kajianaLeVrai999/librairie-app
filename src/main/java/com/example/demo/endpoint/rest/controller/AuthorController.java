package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorRepository authorRepository;

  public AuthorController(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @GetMapping
  public String listAuthors(Model model) {
    List<Author> authors = authorRepository.findAll();
    model.addAttribute("authors", authors);
    return "author-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("author", new Author());
    return "author-form";
  }

  @PostMapping("/save")
  public String saveAuthor(@ModelAttribute Author author) {
    authorRepository.save(author);
    return "redirect:/authors";
  }

  @GetMapping("/{id}")
  public String viewAuthor(@PathVariable Integer id, Model model) {
    Author author = authorRepository.findById(id).orElse(null);
    model.addAttribute("author", author);
    return "author-detail.html";
  }

  @GetMapping("/delete/{id}")
  public String deleteAuthor(@PathVariable Integer id) {
    authorRepository.deleteById(id);
    return "redirect:/authors";
  }
}
