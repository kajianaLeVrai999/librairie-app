package com.example.demo.endpoint.rest.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.time.LocalDate;
import com.example.demo.entity.Sale;
import com.example.demo.service.SaleService;

@RestController
@RequestMapping("/sales")
public class SaleController {


private final SaleService saleService;

public SaleController(SaleService saleService) {
    this.saleService = saleService;
}

// GET /sales
@GetMapping
public ResponseEntity<List<Sale>> getAllSales() {
    return ResponseEntity.ok(saleService.getAll());
}

// GET /sales/{id}
@GetMapping("/{id}")
public ResponseEntity<?> getSaleById(@PathVariable String id) {
    try {
        return ResponseEntity.ok(saleService.getById(id));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// POST /sales
@PostMapping
public ResponseEntity<?> createSale(@RequestBody Sale sale) {
    try {
        Sale created = saleService.create(sale);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// GET /sales/date?date=2026-06-22
@GetMapping("/date")
public ResponseEntity<?> getSalesByDate(
        @RequestParam LocalDate date) {

    try {
        return ResponseEntity.ok(
                saleService.getSalesByDate(date));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid date format. Use YYYY-MM-DD");
    }
}

// GET /sales/revenue?date=2026-06-22
@GetMapping("/revenue")
public ResponseEntity<?> getRevenueByDate(
        @RequestParam LocalDate date) {

    try {
        return ResponseEntity.ok(
                saleService.getTotalRevenueByDate(date));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid date format. Use YYYY-MM-DD");
    }
}

}
