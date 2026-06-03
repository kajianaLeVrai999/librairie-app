package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "arrival")
@Getter
@Setter
public class Arrival {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate arrivalDate;
    private int quantity;
    private String supplier;

    @ManyToOne
    @JoinColumn(name = "bookCopy_id")
    private BookCopy bookcopy;

    public Arrival() {}

    public Arrival(String id, LocalDate arrivalDate,
                   int quantity, String supplier, BookCopy bookcopy) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.quantity = quantity;
        this.supplier = supplier;
        this.bookcopy = bookcopy;
    }
}