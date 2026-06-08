package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sale")
@Getter
@Setter
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private LocalDate saleDate;
  private int quantity;
  private Double totalAmount;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "book_copy_id")
  private BookCopy bookCopy;

  public Sale() {}

  public Sale(
      String id,
      LocalDate saleDate,
      int quantity,
      Double totalAmount,
      Customer customer,
      BookCopy bookCopy) {
    this.id = id;
    this.saleDate = saleDate;
    this.quantity = quantity;
    this.totalAmount = totalAmount;
    this.customer = customer;
    this.bookCopy = bookCopy;
  }
}
