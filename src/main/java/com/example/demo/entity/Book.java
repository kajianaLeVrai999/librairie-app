package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private Double price;
    private LocalDate publicationDate;
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany
    @JoinTable(
        name = "book_gender",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "gender_id")
    )
    private List<Gender> genders;

    public Book() {}

    public Book(String title, String description, Double price,
                LocalDate publicationDate, String isbn,
                Category category, List<Author> authors, List<Gender> genders) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.category = category;
        this.authors = authors;
        this.genders = genders;
    }
}