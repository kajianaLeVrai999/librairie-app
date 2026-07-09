package com.example.demo.conf.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.ArrivalDTO;
import com.example.demo.endpoint.rest.controller.ArrivalController;
import com.example.demo.service.ArrivalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArrivalController.class)
class ArrivalControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ArrivalService arrivalService;

  @Autowired private ObjectMapper objectMapper;

  private ArrivalDTO createArrivalDTO() {
    ArrivalDTO dto = new ArrivalDTO();
    dto.setId(1);
    return dto;
  }

  @Test
  void shouldGetAllArrivals() throws Exception {

    ArrivalDTO dto = createArrivalDTO();

    when(arrivalService.getAll()).thenReturn(List.of(dto));

    mockMvc
        .perform(get("/arrivals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetArrivalById() throws Exception {

    ArrivalDTO dto = createArrivalDTO();

    when(arrivalService.getById(1)).thenReturn(dto);

    mockMvc
        .perform(get("/arrivals/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturn404WhenArrivalNotFound() throws Exception {

    when(arrivalService.getById(1)).thenThrow(new RuntimeException("Arrival not found"));

    mockMvc
        .perform(get("/arrivals/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Arrival not found"));
  }

  @Test
  void shouldCreateArrival() throws Exception {

    ArrivalDTO dto = createArrivalDTO();

    when(arrivalService.create(any(ArrivalDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturn400WhenCreateFails() throws Exception {

    ArrivalDTO dto = createArrivalDTO();

    when(arrivalService.create(any(ArrivalDTO.class)))
        .thenThrow(new RuntimeException("Invalid arrival"));

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid arrival"));
  }

  @Test
  void shouldDeleteArrival() throws Exception {

    ArrivalDTO dto = createArrivalDTO();

    when(arrivalService.getById(1)).thenReturn(dto);
    doNothing().when(arrivalService).delete(1);

    mockMvc.perform(delete("/arrivals/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenDeleteArrivalNotFound() throws Exception {

    when(arrivalService.getById(1)).thenThrow(new RuntimeException("Arrival not found"));

    mockMvc
        .perform(delete("/arrivals/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Arrival not found"));
  }
}
