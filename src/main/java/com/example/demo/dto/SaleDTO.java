package com.example.demo.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDTO {
  private String id;
  private LocalDate saleDate;
  private int quantity;
  private Double totalAmount;
  private CustomerDTO customer;
  private BookCopyDTO bookCopy;

  public SaleDTO() {}

  public SaleDTO(
      String id,
      LocalDate saleDate,
      int quantity,
      Double totalAmount,
      CustomerDTO customer,
      BookCopyDTO bookCopy) {
    this.id = id;
    this.saleDate = saleDate;
    this.quantity = quantity;
    this.totalAmount = totalAmount;
    this.customer = customer;
    this.bookCopy = bookCopy;
  }
}
