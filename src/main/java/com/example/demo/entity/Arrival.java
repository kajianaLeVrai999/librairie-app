package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Arrival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date entryDate;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}