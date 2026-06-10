package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "arrival")
@Getter
@Setter
public class Arrival {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private LocalDate arrivalDate;
  private int quantity;
  private String supplier;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  public Arrival() {}

  public Arrival(int id, LocalDate arrivalDate, int quantity, String supplier, Book book) {
    this.id = id;
    this.arrivalDate = arrivalDate;
    this.quantity = quantity;
    this.supplier = supplier;
    this.book = book;
  }
}
