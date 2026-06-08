package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private LocalDate reservationDate;
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @OneToOne
  @JoinColumn(name = "book_copy_id")
  private BookCopy bookCopy;

  public Reservation() {}

  public Reservation(
      String id,
      LocalDate reservationDate,
      LocalDate expirationDate,
      ReservationStatus status,
      Customer customer,
      BookCopy bookCopy) {
    this.id = id;
    this.reservationDate = reservationDate;
    this.expirationDate = expirationDate;
    this.status = status;
    this.customer = customer;
    this.bookCopy = bookCopy;
  }
}
