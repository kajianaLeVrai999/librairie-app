package com.example.demo.dto;

import com.example.demo.entity.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
  private Integer id;
  private String firstName;
  private String lastName;
  private String biography;
  private String nationality;

  @JsonIgnore private List<Book> books;

  public AuthorDTO() {}

  public AuthorDTO(
      Integer id, String firstName, String lastName, String biography, String nationality) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.biography = biography;
    this.nationality = nationality;
  }
}
