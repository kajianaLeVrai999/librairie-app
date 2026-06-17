package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.BookCopy;
import com.example.demo.entity.Sale;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SaleRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

  private final SaleRepository saleRepository;
  private final BookCopyRepository bookCopyRepository;
  private final CustomerRepository customerRepository;

  public SaleController(
      SaleRepository saleRepository,
      BookCopyRepository bookCopyRepository,
      CustomerRepository customerRepository) {
    this.saleRepository = saleRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.customerRepository = customerRepository;
  }

  @GetMapping
  public String listSales(Model model) {
    List<Sale> sales = saleRepository.findAll();
    model.addAttribute("sales", sales);
    return "sale-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("sale", new Sale());
    model.addAttribute("customers", customerRepository.findAll());
    model.addAttribute("bookCopies", bookCopyRepository.findAll());
    return "sale-form";
  }

  @PostMapping("/save")
  public String saveSale(@ModelAttribute Sale sale) {
    sale.setSaleDate(LocalDate.now());

    // Mettre à jour le statut de l'exemplaire
    BookCopy bookCopy = sale.getBookCopy();
    if (bookCopy != null) {
      bookCopy.setStatus(com.example.demo.entity.CopyStatus.SOLD);
      bookCopyRepository.save(bookCopy);
    }

    saleRepository.save(sale);
    return "redirect:/sales";
  }

  @GetMapping("/{id}")
  public String viewSale(@PathVariable String id, Model model) {
    Sale sale = saleRepository.findById(id).orElse(null);
    model.addAttribute("sale", sale);
    return "sale-detail";
  }
}
