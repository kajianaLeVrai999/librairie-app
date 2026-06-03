package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_copy")
@Getter
@Setter
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private CopyStatus status;

    private BookCopyFormat  bookCopyFormat;

    public BookCopy() {}

    public BookCopy(int id, Book book, CopyStatus status, BookCopyFormat bookCopyFormat) {
        this.id = id;
        this.book = book;
        this.status = status;
        this.bookCopyFormat = bookCopyFormat;
    }
}