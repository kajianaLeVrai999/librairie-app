package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gender")
@Getter
@Setter
public class Gender {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany(mappedBy = "genders")
  private List<Book> books;

  public Gender() {}

  public Gender(String name) {
    this.name = name;
  }
}
