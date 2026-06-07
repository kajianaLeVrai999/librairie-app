package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date saleDate;

    private int quantity;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}