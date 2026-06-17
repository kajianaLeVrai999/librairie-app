package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Arrival;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.ArrivalRepository;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arrivals")
public class ArrivalController {

  private final ArrivalRepository arrivalRepository;
  private final BookRepository bookRepository;
  private final BookCopyRepository bookCopyRepository;

  public ArrivalController(
      ArrivalRepository arrivalRepository,
      BookRepository bookRepository,
      BookCopyRepository bookCopyRepository) {
    this.arrivalRepository = arrivalRepository;
    this.bookRepository = bookRepository;
    this.bookCopyRepository = bookCopyRepository;
  }

  // Liste tous les arrivages
  @GetMapping
  public String listArrivals(Model model) {
    List<Arrival> arrivals = arrivalRepository.findAll();
    model.addAttribute("arrivals", arrivals);
    return "arrival-list";
  }

  // Formulaire d'ajout d'arrivage
  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("arrival", new Arrival());
    model.addAttribute("books", bookRepository.findAll());
    return "arrival-form";
  }

  // Sauvegarde d'un arrivage (crée automatiquement les exemplaires)
  @PostMapping("/save")
  public String saveArrival(@ModelAttribute Arrival arrival) {
    arrival.setArrivalDate(LocalDate.now());
    Arrival savedArrival = arrivalRepository.save(arrival);

    // Crée les exemplaires en fonction de la quantité
    for (int i = 0; i < savedArrival.getQuantity(); i++) {
      BookCopy bookCopy = new BookCopy();
      bookCopy.setBook(savedArrival.getBook());
      bookCopy.setStatus(CopyStatus.AVAILABLE);
      bookCopyRepository.save(bookCopy);
    }

    return "redirect:/arrivals";
  }

  // Détail d'un arrivage
  @GetMapping("/{id}")
  public String viewArrival(@PathVariable Integer id, Model model) {
    Arrival arrival = arrivalRepository.findById(id).orElse(null);
    model.addAttribute("arrival", arrival);
    return "arrival-detail";
  }

  // Supprimer un arrivage
  @GetMapping("/delete/{id}")
  public String deleteArrival(@PathVariable Integer id) {
    arrivalRepository.deleteById(id);
    return "redirect:/arrivals";
  }
}
