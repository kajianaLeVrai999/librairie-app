package com.example.demo.dto;

import com.example.demo.entity.Author;
import com.example.demo.entity.Category;
import com.example.demo.entity.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
  private Integer id;
  private String title;
  private String description;
  private Double price;
  private LocalDate publicationDate;
  private String isbn;
  private Category category;

  @JsonIgnore private List<Author> authors;

  @JsonIgnore private List<Gender> genders;

  public BookDTO() {}

  public BookDTO(
      String title, String description, Double price, LocalDate publicationDate, String isbn) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.publicationDate = publicationDate;
    this.isbn = isbn;
  }
}
