package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookcopies")
public class BookCopyController {

  private final BookCopyRepository bookCopyRepository;
  private final BookRepository bookRepository;

  public BookCopyController(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
    this.bookCopyRepository = bookCopyRepository;
    this.bookRepository = bookRepository;
  }

  // Liste tous les exemplaires
  @GetMapping
  public String listBookCopies(Model model) {
    List<BookCopy> bookCopies = bookCopyRepository.findAll();
    model.addAttribute("bookCopies", bookCopies);
    model.addAttribute("copyStatuses", CopyStatus.values());
    return "bookcopy-list";
  }

  // Formulaire d'ajout d'exemplaire
  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("bookCopy", new BookCopy());
    model.addAttribute("books", bookRepository.findAll());
    model.addAttribute("copyStatuses", CopyStatus.values());
    return "bookcopy-form";
  }

  // Sauvegarde d'un nouvel exemplaire
  @PostMapping("/save")
  public String saveBookCopy(@ModelAttribute BookCopy bookCopy) {
    bookCopyRepository.save(bookCopy);
    return "redirect:/bookcopies";
  }

  // Détail d'un exemplaire
  @GetMapping("/{id}")
  public String viewBookCopy(@PathVariable Integer id, Model model) {
    BookCopy bookCopy = bookCopyRepository.findById(id).orElse(null);
    model.addAttribute("bookCopy", bookCopy);
    model.addAttribute("copyStatuses", CopyStatus.values());
    return "bookcopy-detail";
  }

  // Changer le statut d'un exemplaire
  @GetMapping("/status/{id}")
  public String updateStatusForm(@PathVariable Integer id, Model model) {
    BookCopy bookCopy = bookCopyRepository.findById(id).orElse(null);
    model.addAttribute("bookCopy", bookCopy);
    model.addAttribute("copyStatuses", CopyStatus.values());
    return "bookcopy-status.html";
  }

  @PostMapping("/status/{id}")
  public String updateStatus(@PathVariable Integer id, @RequestParam CopyStatus status) {
    BookCopy bookCopy = bookCopyRepository.findById(id).orElse(null);
    if (bookCopy != null) {
      bookCopy.setStatus(status);
      bookCopyRepository.save(bookCopy);
    }
    return "redirect:/bookcopies";
  }

  // Supprimer un exemplaire
  @GetMapping("/delete/{id}")
  public String deleteBookCopy(@PathVariable Integer id) {
    bookCopyRepository.deleteById(id);
    return "redirect:/bookcopies";
  }

  // Voir les exemplaires d'un livre spécifique
  @GetMapping("/book/{bookId}")
  public String listCopiesByBook(@PathVariable Integer bookId, Model model) {
    List<BookCopy> copies =
        bookCopyRepository.findAll().stream()
            .filter(copy -> copy.getBook() != null && copy.getBook().getId() == bookId) // ← corrigé
            .toList();
    model.addAttribute("book", bookRepository.findById(bookId).orElse(null));
    model.addAttribute("bookCopies", copies);
    model.addAttribute("copyStatuses", CopyStatus.values());
    return "bookcopy-list";
  }
}
