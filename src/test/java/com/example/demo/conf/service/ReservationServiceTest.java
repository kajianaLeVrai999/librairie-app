package com.example.demo.conf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  @Mock private ReservationRepository reservationRepository;

  @Mock private BookCopyRepository bookCopyRepository;

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private ReservationService reservationService;

  private Customer customer;
  private BookCopy bookCopy;
  private Reservation reservation;
  private ReservationDTO reservationDTO;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setId(1);
    customer.setFirstName("Jean");
    customer.setLastName("Dupont");

    bookCopy = new BookCopy();
    bookCopy.setId(1);
    bookCopy.setStatus(CopyStatus.AVAILABLE);

    reservation = new Reservation();
    reservation.setId("res-001");
    reservation.setCustomer(customer);
    reservation.setBookCopy(bookCopy);
    reservation.setStatus(ReservationStatus.PENDING);
    reservation.setReservationDate(LocalDate.now());
    reservation.setExpirationDate(LocalDate.now().plusDays(7));

    reservationDTO = new ReservationDTO();
    reservationDTO.setCustomerId(1); // ← Maintenant ça existe
    reservationDTO.setBookCopyId(1); // ← Maintenant ça existe
  }

  @Test
  void shouldCreateReservation() {
    when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
    when(bookCopyRepository.findById(1)).thenReturn(Optional.of(bookCopy));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

    ReservationDTO result = reservationService.create(reservationDTO);

    assertNotNull(result);
    assertEquals(ReservationStatus.PENDING, result.getStatus());
    assertNotNull(result.getReservationDate());
    assertNotNull(result.getExpirationDate());
    verify(reservationRepository).save(any(Reservation.class));
    verify(bookCopyRepository).save(bookCopy);
  }

  @Test
  void shouldThrowWhenCustomerNotFound() {
    when(customerRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> reservationService.create(reservationDTO));
  }

  @Test
  void shouldThrowWhenBookCopyNotFound() {
    when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
    when(bookCopyRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> reservationService.create(reservationDTO));
  }

  @Test
  void shouldGetAllReservations() {
    List<Reservation> reservations = List.of(reservation, reservation);
    when(reservationRepository.findAll()).thenReturn(reservations);

    List<ReservationDTO> result = reservationService.getAll();

    assertEquals(2, result.size());
  }

  @Test
  void shouldGetReservationById() {
    when(reservationRepository.findById("res-001")).thenReturn(Optional.of(reservation));

    ReservationDTO result = reservationService.getById("res-001");

    assertNotNull(result);
    assertEquals("res-001", result.getId());
  }

  @Test
  void shouldThrowWhenReservationNotFound() {
    when(reservationRepository.findById("invalid-id")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> reservationService.getById("invalid-id"));
  }

  @Test
  void shouldConfirmReservation() {
    when(reservationRepository.findById("res-001")).thenReturn(Optional.of(reservation));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

    ReservationDTO result = reservationService.confirm("res-001");

    assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
  }

  @Test
  void shouldCancelReservation() {
    when(reservationRepository.findById("res-001")).thenReturn(Optional.of(reservation));
    when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

    ReservationDTO result = reservationService.cancel("res-001");

    assertEquals(ReservationStatus.CANCELLED, result.getStatus());
    verify(bookCopyRepository).save(bookCopy);
  }

  @Test
  void shouldGetActiveReservations() {
    Reservation activeReservation = new Reservation();
    activeReservation.setStatus(ReservationStatus.PENDING);

    Reservation cancelledReservation = new Reservation();
    cancelledReservation.setStatus(ReservationStatus.CANCELLED);

    when(reservationRepository.findAll())
        .thenReturn(List.of(activeReservation, cancelledReservation));

    List<ReservationDTO> result = reservationService.getActiveReservations();

    assertEquals(1, result.size());
    assertEquals(ReservationStatus.PENDING, result.get(0).getStatus());
  }
}
