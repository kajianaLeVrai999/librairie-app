package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
  private int id;
  private String title;
  private String description;
  private Double price;
  private LocalDate publicationDate;
  private String isbn;
  private CategoryDTO category;
  private List<AuthorDTO> authors;
  private List<GenderDTO> genders;

  public BookDTO() {}

  public BookDTO(
      int id,
      String title,
      String description,
      Double price,
      LocalDate publicationDate,
      String isbn,
      CategoryDTO category,
      List<AuthorDTO> authors,
      List<GenderDTO> genders) {
    this.id = id;
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
