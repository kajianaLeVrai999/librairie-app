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

  // CREATE
  public ReservationDTO create(ReservationDTO dto) {

    Customer customer =
        customerRepository
            .findById(dto.getCustomer().getId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    BookCopy bookCopy =
        bookCopyRepository
            .findById(dto.getBookCopy().getId())
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

  // GET ALL
  public List<ReservationDTO> getAll() {

    return reservationRepository.findAll().stream().map(this::toDTO).toList();
  }

  // GET BY ID
  public ReservationDTO getById(String id) {

    Reservation reservation =
        reservationRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

    return toDTO(reservation);
  }

  // CONFIRM
  public ReservationDTO confirm(String id) {

    Reservation reservation = getEntityById(id);

    reservation.setStatus(ReservationStatus.CONFIRMED);

    return toDTO(reservationRepository.save(reservation));
  }

  // CANCEL
  public ReservationDTO cancel(String id) {

    Reservation reservation = getEntityById(id);

    reservation.setStatus(ReservationStatus.CANCELLED);

    if (reservation.getBookCopy() != null) {

      reservation.getBookCopy().setStatus(CopyStatus.AVAILABLE);

      bookCopyRepository.save(reservation.getBookCopy());
    }

    return toDTO(reservationRepository.save(reservation));
  }

  // ACTIVE RESERVATIONS
  public List<ReservationDTO> getActiveReservations() {

    return reservationRepository.findAll().stream()
        .filter(
            r ->
                r.getStatus() == ReservationStatus.PENDING
                    || r.getStatus() == ReservationStatus.CONFIRMED)
        .map(this::toDTO)
        .toList();
  }

  private Reservation getEntityById(String id) {

    return reservationRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));
  }

  // ENTITY -> DTO
  private ReservationDTO toDTO(Reservation reservation) {

    ReservationDTO dto = new ReservationDTO();

    dto.setId(reservation.getId());
    dto.setReservationDate(reservation.getReservationDate());
    dto.setExpirationDate(reservation.getExpirationDate());
    dto.setStatus(reservation.getStatus());

    if (reservation.getCustomer() != null) {

      Customer customer = reservation.getCustomer();

      CustomerDTO customerDTO = new CustomerDTO();

      customerDTO.setId(customer.getId());
      customerDTO.setFirstName(customer.getFirstName());
      customerDTO.setLastName(customer.getLastName());
      customerDTO.setEmail(customer.getEmail());
      customerDTO.setPhone(customer.getPhone());
      customerDTO.setAddress(customer.getAddress());

      dto.setCustomer(customerDTO);
    }

    if (reservation.getBookCopy() != null) {

      BookCopy copy = reservation.getBookCopy();

      BookCopyDTO copyDTO = new BookCopyDTO();

      copyDTO.setId(copy.getId());
      copyDTO.setStatus(copy.getStatus());
      copyDTO.setFormat(copy.getFormat());

      dto.setBookCopy(copyDTO);
    }

    return dto;
  }
}
