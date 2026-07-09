package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.SaleDTO;
import com.example.demo.service.SaleService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {


    private final SaleService saleService;


    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }



    // GET /sales
    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {

        return ResponseEntity.ok(
                saleService.getAll()
        );
    }



    // GET /sales/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(
            @PathVariable String id) {


        try {

            return ResponseEntity.ok(
                    saleService.getById(id)
            );


        } catch(RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    // POST /sales
    @PostMapping
    public ResponseEntity<?> createSale(
            @RequestBody SaleDTO dto) {


        try {

            SaleDTO created =
                    saleService.create(dto);


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);


        } catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }



    // GET /sales/date?date=2026-06-22
    @GetMapping("/date")
    public ResponseEntity<?> getSalesByDate(
            @RequestParam LocalDate date) {


        try {

            return ResponseEntity.ok(
                    saleService.getSalesByDate(date)
            );


        } catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use YYYY-MM-DD");
        }
    }



    // GET /sales/revenue?date=2026-06-22
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueByDate(
            @RequestParam LocalDate date) {


        try {

            return ResponseEntity.ok(
                    saleService.getTotalRevenueByDate(date)
            );


        } catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use YYYY-MM-DD");
        }
    }
}