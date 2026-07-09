package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenderDTO {
  private Long id;
  private String name;

  public GenderDTO() {}

  public GenderDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
