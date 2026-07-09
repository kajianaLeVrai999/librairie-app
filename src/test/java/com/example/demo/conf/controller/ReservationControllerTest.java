package com.example.demo.conf.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.endpoint.rest.controller.ReservationController;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ReservationService reservationService;

  @Autowired private ObjectMapper objectMapper;

  private ReservationDTO createReservationDTO() {
    ReservationDTO dto = new ReservationDTO();
    dto.setId("res-1");
    dto.setReservationDate(LocalDate.now());
    dto.setExpirationDate(LocalDate.now().plusDays(7));
    dto.setStatus(ReservationStatus.CONFIRMED);
    return dto;
  }

  @Test
  void shouldGetAllReservations() throws Exception {
    when(reservationService.getAll()).thenReturn(List.of(createReservationDTO()));

    mockMvc
        .perform(get("/reservations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("res-1"));
  }

  @Test
  void shouldGetReservationById() throws Exception {
    when(reservationService.getById("res-1")).thenReturn(createReservationDTO());

    mockMvc
        .perform(get("/reservations/res-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("res-1"));
  }

  @Test
  void shouldCreateReservation() throws Exception {
    ReservationDTO dto = createReservationDTO();
    when(reservationService.create(any(ReservationDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("res-1"));
  }

  @Test
  void shouldConfirmReservation() throws Exception {
    when(reservationService.confirm("res-1")).thenReturn(createReservationDTO());

    mockMvc
        .perform(patch("/reservations/res-1/confirm"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("res-1"));
  }

  @Test
  void shouldCancelReservation() throws Exception {
    when(reservationService.cancel("res-1")).thenReturn(createReservationDTO());

    mockMvc
        .perform(patch("/reservations/res-1/cancel"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("res-1"));
  }

  @Test
  void shouldGetActiveReservations() throws Exception {
    when(reservationService.getActiveReservations()).thenReturn(List.of(createReservationDTO()));

    mockMvc
        .perform(get("/reservations/active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("res-1"));
  }

  @Test
  void shouldReturn404WhenReservationNotFound() throws Exception {
    when(reservationService.getById("res-1"))
        .thenThrow(new RuntimeException("Reservation not found"));

    mockMvc
        .perform(get("/reservations/res-1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Reservation not found"));
  }
}
