package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.ArrivalDTO;
import com.example.demo.service.ArrivalService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arrivals")
public class ArrivalController {

    private final ArrivalService arrivalService;

    public ArrivalController(ArrivalService arrivalService) {
        this.arrivalService = arrivalService;
    }


    // GET /arrivals
    @GetMapping
    public ResponseEntity<List<ArrivalDTO>> getAllArrivals() {
        return ResponseEntity.ok(arrivalService.getAll());
    }


    // GET /arrivals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getArrivalById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(arrivalService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    // POST /arrivals
    @PostMapping
    public ResponseEntity<?> createArrival(@RequestBody ArrivalDTO dto) {
        try {
            ArrivalDTO created = arrivalService.create(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    // DELETE /arrivals/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArrival(@PathVariable Integer id) {
        try {
            arrivalService.getById(id);
            arrivalService.delete(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}