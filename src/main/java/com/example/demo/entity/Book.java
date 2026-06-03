package com.example.demo.entity;

import java.util.List;

public class Book {
    private Long id;
    private String title;
    private List<Author> authorList;
    private List<Gender> genderList;
    private List<BookCopy> bookCopyList;
    private List<Arrival> arrivalList;
    private List<Sale> saleList;
}
