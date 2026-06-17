package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Sale;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SaleRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

  private final BookRepository bookRepository;
  private final BookCopyRepository bookCopyRepository;
  private final SaleRepository saleRepository;
  private final ReservationRepository reservationRepository;
  private final CustomerRepository customerRepository;
  private final CategoryRepository categoryRepository;
  private final AuthorRepository authorRepository;

  public DashboardController(
      BookRepository bookRepository,
      BookCopyRepository bookCopyRepository,
      SaleRepository saleRepository,
      ReservationRepository reservationRepository,
      CustomerRepository customerRepository,
      CategoryRepository categoryRepository,
      AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.saleRepository = saleRepository;
    this.reservationRepository = reservationRepository;
    this.customerRepository = customerRepository;
    this.categoryRepository = categoryRepository;
    this.authorRepository = authorRepository;
  }

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("totalBooks", bookRepository.count());
    model.addAttribute("totalBookCopies", bookCopyRepository.count());
    model.addAttribute("totalCustomers", customerRepository.count());
    model.addAttribute("totalCategories", categoryRepository.count());
    model.addAttribute("totalAuthors", authorRepository.count());

    model.addAttribute("totalReservations", reservationRepository.count());

    LocalDate today = LocalDate.now();
    double todayRevenue =
        saleRepository.findAll().stream()
            .filter(sale -> sale.getSaleDate() != null && sale.getSaleDate().equals(today))
            .mapToDouble(Sale::getTotalAmount)
            .sum();
    model.addAttribute("todayRevenue", todayRevenue);

    long todaySalesCount =
        saleRepository.findAll().stream()
            .filter(sale -> sale.getSaleDate() != null && sale.getSaleDate().equals(today))
            .count();
    model.addAttribute("todaySalesCount", todaySalesCount);

    return "dashboard";
  }
}
