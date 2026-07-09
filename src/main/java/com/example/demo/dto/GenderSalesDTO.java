package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenderSalesDTO {
  private Long genderId;
  private String genderName;
  private Long totalSales;
  private Double totalRevenue;
}
