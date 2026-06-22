package com.example.demo.endpoint.rest.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.example.demo.entity.Reservation;
import com.example.demo.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

private final ReservationService reservationService;

public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
}

// GET /reservations
@GetMapping
public ResponseEntity<List<Reservation>> getAllReservations() {
    return ResponseEntity.ok(reservationService.getAll());
}

// GET /reservations/{id}
@GetMapping("/{id}")
public ResponseEntity<?> getReservationById(@PathVariable String id) {
    try {
        return ResponseEntity.ok(reservationService.getById(id));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// POST /reservations
@PostMapping
public ResponseEntity<?> createReservation(
        @RequestBody Reservation reservation) {

    try {
        Reservation created = reservationService.create(reservation);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// PATCH /reservations/{id}/confirm
@PatchMapping("/{id}/confirm")
public ResponseEntity<?> confirmReservation(
        @PathVariable String id) {

    try {
        Reservation reservation = reservationService.confirm(id);
        return ResponseEntity.ok(reservation);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// PATCH /reservations/{id}/cancel
@PatchMapping("/{id}/cancel")
public ResponseEntity<?> cancelReservation(
        @PathVariable String id) {

    try {
        Reservation reservation = reservationService.cancel(id);
        return ResponseEntity.ok(reservation);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// GET /reservations/active
@GetMapping("/active")
public ResponseEntity<List<Reservation>> getActiveReservations() {
    return ResponseEntity.ok(
            reservationService.getActiveReservations());
}


}
