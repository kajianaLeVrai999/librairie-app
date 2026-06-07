package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authorList;

    @ManyToMany
    @JoinTable(
        name = "book_gender",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "gender_id")
    )
    private List<Gender> genderList;

    @OneToMany(mappedBy = "book")
    private List<BookCopy> bookCopyList;

    @OneToMany(mappedBy = "book")
    private List<Arrival> arrivalList;

    @OneToMany(mappedBy = "book")
    private List<Sale> saleList;
}