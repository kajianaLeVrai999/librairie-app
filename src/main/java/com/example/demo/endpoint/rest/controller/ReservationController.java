package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

  private final ReservationRepository reservationRepository;
  private final BookCopyRepository bookCopyRepository;
  private final CustomerRepository customerRepository;

  public ReservationController(
      ReservationRepository reservationRepository,
      BookCopyRepository bookCopyRepository,
      CustomerRepository customerRepository) {
    this.reservationRepository = reservationRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.customerRepository = customerRepository;
  }

  @GetMapping
  public String listReservations(Model model) {
    List<Reservation> reservations = reservationRepository.findAll();
    model.addAttribute("reservations", reservations);
    return "reservation-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("reservation", new Reservation());
    model.addAttribute("customers", customerRepository.findAll());
    model.addAttribute("bookCopies", bookCopyRepository.findAll());
    return "reservation-form";
  }

  @PostMapping("/save")
  public String saveReservation(@ModelAttribute Reservation reservation) {
    reservation.setReservationDate(LocalDate.now());
    reservation.setExpirationDate(LocalDate.now().plusDays(7));
    reservation.setStatus(ReservationStatus.PENDING);

    BookCopy bookCopy = reservation.getBookCopy();
    if (bookCopy != null) {
      bookCopy.setStatus(com.example.demo.entity.CopyStatus.RESERVED);
      bookCopyRepository.save(bookCopy);
    }

    reservationRepository.save(reservation);
    return "redirect:/reservations";
  }

  @GetMapping("/confirm/{id}")
  public String confirmReservation(@PathVariable String id) {
    Reservation reservation = reservationRepository.findById(id).orElse(null);
    if (reservation != null) {
      reservation.setStatus(ReservationStatus.CONFIRMED);
      reservationRepository.save(reservation);
    }
    return "redirect:/reservations";
  }

  @GetMapping("/cancel/{id}")
  public String cancelReservation(@PathVariable String id) {
    Reservation reservation = reservationRepository.findById(id).orElse(null);
    if (reservation != null) {
      reservation.setStatus(ReservationStatus.CANCELLED);

      BookCopy bookCopy = reservation.getBookCopy();
      if (bookCopy != null) {
        bookCopy.setStatus(com.example.demo.entity.CopyStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);
      }

      reservationRepository.save(reservation);
    }
    return "redirect:/reservations";
  }
}
