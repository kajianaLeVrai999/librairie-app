package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "genderList")
    private List<Book> books;
}