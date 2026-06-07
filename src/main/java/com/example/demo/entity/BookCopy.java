package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String barcode;
    private double price;
    private boolean sold;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public boolean isAvailable() {
        return !sold;
    }

    public void markAsSold() {
        this.sold = true;
    }
}