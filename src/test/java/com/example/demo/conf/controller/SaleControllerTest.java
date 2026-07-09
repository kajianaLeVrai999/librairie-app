package com.example.demo.conf.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.SaleDTO;
import com.example.demo.endpoint.rest.controller.SaleController;
import com.example.demo.service.SaleService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

  @Mock private SaleService saleService;

  @InjectMocks private SaleController saleController;

  @Test
  void shouldGetAllSales() {
    List<SaleDTO> sales = List.of(new SaleDTO(), new SaleDTO());
    when(saleService.getAll()).thenReturn(sales);

    ResponseEntity<List<SaleDTO>> response = saleController.getAllSales();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
  }

  @Test
  void shouldGetSaleById() {
    SaleDTO sale = new SaleDTO();
    sale.setId("sale-001");
    when(saleService.getById("sale-001")).thenReturn(sale);

    ResponseEntity<?> response = saleController.getSaleById("sale-001");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldReturnNotFoundWhenSaleNotFound() {
    when(saleService.getById("invalid-id")).thenThrow(new RuntimeException("Sale not found"));

    ResponseEntity<?> response = saleController.getSaleById("invalid-id");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldCreateSale() {
    SaleDTO sale = new SaleDTO();
    when(saleService.create(any(SaleDTO.class))).thenReturn(sale);

    ResponseEntity<?> response = saleController.createSale(sale);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void shouldGetSalesByDate() {
    LocalDate date = LocalDate.now();
    List<SaleDTO> sales = List.of(new SaleDTO());
    when(saleService.getSalesByDate(date)).thenReturn(sales);

    ResponseEntity<?> response = saleController.getSalesByDate(date);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void shouldGetRevenueByDate() {
    LocalDate date = LocalDate.now();
    when(saleService.getTotalRevenueByDate(date)).thenReturn(100.0);

    ResponseEntity<?> response = saleController.getRevenueByDate(date);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(100.0, response.getBody());
  }
}
