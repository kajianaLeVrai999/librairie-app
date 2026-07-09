package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.ReservationDTO;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final BookCopyRepository bookCopyRepository;
  private final CustomerRepository customerRepository;

  public ReservationService(
      ReservationRepository reservationRepository,
      BookCopyRepository bookCopyRepository,
      CustomerRepository customerRepository) {
    this.reservationRepository = reservationRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.customerRepository = customerRepository;
  }

  public ReservationDTO create(ReservationDTO dto) {
    Customer customer =
        customerRepository
            .findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    BookCopy bookCopy =
        bookCopyRepository
            .findById(dto.getBookCopyId())
            .orElseThrow(() -> new RuntimeException("BookCopy not found"));

    Reservation reservation = new Reservation();
    reservation.setCustomer(customer);
    reservation.setBookCopy(bookCopy);
    reservation.setReservationDate(LocalDate.now());
    reservation.setExpirationDate(LocalDate.now().plusDays(7));
    reservation.setStatus(ReservationStatus.PENDING);

    bookCopy.setStatus(CopyStatus.RESERVED);
    bookCopyRepository.save(bookCopy);

    Reservation saved = reservationRepository.save(reservation);
    return toDTO(saved);
  }

  public List<ReservationDTO> getAll() {
    return reservationRepository.findAll().stream().map(this::toDTO).toList();
  }

  public ReservationDTO getById(String id) {
    Reservation reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
    return toDTO(reservation);
  }

  public ReservationDTO confirm(String id) {
    Reservation reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
    reservation.setStatus(ReservationStatus.CONFIRMED);
    Reservation saved = reservationRepository.save(reservation);
    return toDTO(saved);
  }

  public ReservationDTO cancel(String id) {
    Reservation reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
    reservation.setStatus(ReservationStatus.CANCELLED);

    if (reservation.getBookCopy() != null) {
      reservation.getBookCopy().setStatus(CopyStatus.AVAILABLE);
      bookCopyRepository.save(reservation.getBookCopy());
    }

    Reservation saved = reservationRepository.save(reservation);
    return toDTO(saved);
  }

  public List<ReservationDTO> getActiveReservations() {
    return reservationRepository.findAll().stream()
        .filter(
            r ->
                r.getStatus() == ReservationStatus.PENDING
                    || r.getStatus() == ReservationStatus.CONFIRMED)
        .map(this::toDTO)
        .toList();
  }

  private ReservationDTO toDTO(Reservation reservation) {
    ReservationDTO dto = new ReservationDTO();
    dto.setId(reservation.getId());
    dto.setReservationDate(reservation.getReservationDate());
    dto.setExpirationDate(reservation.getExpirationDate());
    dto.setStatus(reservation.getStatus());

    if (reservation.getCustomer() != null) {
      dto.setCustomerId(reservation.getCustomer().getId());
      CustomerDTO customerDTO = new CustomerDTO();
      Customer customer = reservation.getCustomer();
      customerDTO.setId(customer.getId());
      customerDTO.setFirstName(customer.getFirstName());
      customerDTO.setLastName(customer.getLastName());
      customerDTO.setEmail(customer.getEmail());
      customerDTO.setPhone(customer.getPhone());
      customerDTO.setAddress(customer.getAddress());
      dto.setCustomer(customerDTO);
    }

    if (reservation.getBookCopy() != null) {
      dto.setBookCopyId(reservation.getBookCopy().getId());
      BookCopyDTO copyDTO = new BookCopyDTO();
      BookCopy copy = reservation.getBookCopy();
      copyDTO.setId(copy.getId());
      copyDTO.setStatus(copy.getStatus());
      copyDTO.setFormat(copy.getFormat());
      dto.setBookCopy(copyDTO);
    }

    return dto;
  }
}
