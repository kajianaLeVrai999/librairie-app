package com.example.demo.service;

import com.example.demo.entity.CopyStatus;
import com.example.demo.entity.Sale;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.SaleRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

  private final SaleRepository saleRepository;
  private final BookCopyRepository bookCopyRepository;

  public SaleService(SaleRepository saleRepository, BookCopyRepository bookCopyRepository) {
    this.saleRepository = saleRepository;
    this.bookCopyRepository = bookCopyRepository;
  }

  public Sale create(Sale sale) {
    sale.setSaleDate(LocalDate.now());

    // Mettre à jour le statut de l'exemplaire
    if (sale.getBookCopy() != null) {
      sale.getBookCopy().setStatus(CopyStatus.SOLD);
      bookCopyRepository.save(sale.getBookCopy());
    }

    return saleRepository.save(sale);
  }

  public List<Sale> getAll() {
    return saleRepository.findAll();
  }

  public Sale getById(String id) {
    return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
  }

  public List<Sale> getSalesByDate(LocalDate date) {
    return saleRepository.findAll().stream()
        .filter(sale -> sale.getSaleDate() != null && sale.getSaleDate().equals(date))
        .toList();
  }

  public double getTotalRevenueByDate(LocalDate date) {
    return getSalesByDate(date).stream().mapToDouble(Sale::getTotalAmount).sum();
  }
}
