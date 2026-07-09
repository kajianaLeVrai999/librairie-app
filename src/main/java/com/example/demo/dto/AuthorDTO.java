package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
  private int id;
  private String firstName;
  private String lastName;
  private String biography;
  private String nationality;

  public AuthorDTO() {}

  public AuthorDTO(int id, String firstName, String lastName, String biography, String nationality) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.biography = biography;
    this.nationality = nationality;
  }
}
