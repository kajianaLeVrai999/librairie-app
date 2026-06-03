package com.example.demo.entity;

public class BookCopy {
    private Long id;
    private String barcode;
    private double price;
    private boolean sold;
    private Book book;

    public boolean isAvailable();
    public void markAsSold();
}
