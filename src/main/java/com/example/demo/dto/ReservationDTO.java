package com.example.demo.dto;

import com.example.demo.entity.ReservationStatus;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDTO {
  private String id;
  private LocalDate reservationDate;
  private LocalDate expirationDate;
  private ReservationStatus status;
  private CustomerDTO customer;
  private BookCopyDTO bookCopy;

  public ReservationDTO() {}

  public ReservationDTO(
      String id,
      LocalDate reservationDate,
      LocalDate expirationDate,
      ReservationStatus status,
      CustomerDTO customer,
      BookCopyDTO bookCopy) {
    this.id = id;
    this.reservationDate = reservationDate;
    this.expirationDate = expirationDate;
    this.status = status;
    this.customer = customer;
    this.bookCopy = bookCopy;
  }
}
