package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.service.ReservationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {

    this.reservationService = reservationService;
  }

  // GET /reservations
  @GetMapping
  public ResponseEntity<List<ReservationDTO>> getAllReservations() {

    return ResponseEntity.ok(reservationService.getAll());
  }

  // GET /reservations/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getReservationById(@PathVariable String id) {

    try {

      return ResponseEntity.ok(reservationService.getById(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /reservations
  @PostMapping
  public ResponseEntity<?> createReservation(@RequestBody ReservationDTO dto) {

    try {

      ReservationDTO created = reservationService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(created);

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // PATCH /reservations/{id}/confirm
  @PatchMapping("/{id}/confirm")
  public ResponseEntity<?> confirmReservation(@PathVariable String id) {

    try {

      return ResponseEntity.ok(reservationService.confirm(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // PATCH /reservations/{id}/cancel
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<?> cancelReservation(@PathVariable String id) {

    try {

      return ResponseEntity.ok(reservationService.cancel(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // GET /reservations/active
  @GetMapping("/active")
  public ResponseEntity<List<ReservationDTO>> getActiveReservations() {

    return ResponseEntity.ok(reservationService.getActiveReservations());
  }
}
