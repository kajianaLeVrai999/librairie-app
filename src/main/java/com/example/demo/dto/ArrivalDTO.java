package com.example.demo.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrivalDTO {
  private int id;
  private LocalDate arrivalDate;
  private int quantity;
  private String supplier;
  private BookDTO book;

  public ArrivalDTO() {}

  public ArrivalDTO(int id, LocalDate arrivalDate, int quantity, String supplier, BookDTO book) {
    this.id = id;
    this.arrivalDate = arrivalDate;
    this.quantity = quantity;
    this.supplier = supplier;
    this.book = book;
  }
}
