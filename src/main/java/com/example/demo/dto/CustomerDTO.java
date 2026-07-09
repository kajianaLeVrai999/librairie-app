package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String address;

  public CustomerDTO() {}

  public CustomerDTO(
      int id, String firstName, String lastName, String email, String phone, String address) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }
}
