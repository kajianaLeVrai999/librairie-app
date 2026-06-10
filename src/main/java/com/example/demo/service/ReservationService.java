package com.example.demo.service;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.BookCopyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              BookCopyRepository bookCopyRepository) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    public Reservation create(Reservation reservation) {
        reservation.setReservationDate(LocalDate.now());
        reservation.setExpirationDate(LocalDate.now().plusDays(7));
        reservation.setStatus(ReservationStatus.PENDING);

        if (reservation.getBookCopy() != null) {
            reservation.getBookCopy().setStatus(CopyStatus.RESERVED);
            bookCopyRepository.save(reservation.getBookCopy());
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation getById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public Reservation confirm(String id) {
        Reservation reservation = getById(id);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancel(String id) {
        Reservation reservation = getById(id);
        reservation.setStatus(ReservationStatus.CANCELLED);

        if (reservation.getBookCopy() != null) {
            reservation.getBookCopy().setStatus(CopyStatus.AVAILABLE);
            bookCopyRepository.save(reservation.getBookCopy());
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getActiveReservations() {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING ||
                            r.getStatus() == ReservationStatus.CONFIRMED)
                .toList();
    }
}