package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String firstName;
  private String lastName;
  private String biography;
  private String nationality;

  @ManyToMany(mappedBy = "authors")
  private List<Book> books;

  public Author() {}

  public Author(int id, String firstName, String lastName, String biography, String nationality) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.biography = biography;
    this.nationality = nationality;
  }
}
