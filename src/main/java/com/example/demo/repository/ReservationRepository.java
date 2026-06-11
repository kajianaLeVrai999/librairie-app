package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
  List<Reservation> findByStatus(ReservationStatus status);

  List<Reservation> findByCustomerId(Integer customerId);

  List<Reservation> findByExpirationDateBefore(LocalDate date);
}
