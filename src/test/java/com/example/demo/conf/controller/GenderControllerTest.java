package com.example.demo.conf.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.GenderDTO;
import com.example.demo.endpoint.rest.controller.GenderController;
import com.example.demo.service.GenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GenderController.class)
class GenderControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GenderService genderService;

  @Autowired private ObjectMapper objectMapper;

  private GenderDTO createGenderDTO() {
    GenderDTO dto = new GenderDTO();
    dto.setId(1L);
    dto.setName("Fantasy");
    return dto;
  }

  @Test
  void shouldGetAllGenders() throws Exception {
    when(genderService.getAll()).thenReturn(List.of(createGenderDTO()));

    mockMvc
        .perform(get("/genders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetGenderById() throws Exception {
    when(genderService.getById(1L)).thenReturn(createGenderDTO());

    mockMvc
        .perform(get("/genders/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldCreateGender() throws Exception {
    GenderDTO dto = createGenderDTO();
    when(genderService.create(any(GenderDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/genders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldUpdateGender() throws Exception {
    GenderDTO dto = createGenderDTO();
    when(genderService.update(eq(1L), any(GenderDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/genders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteGender() throws Exception {
    doNothing().when(genderService).delete(1L);

    mockMvc.perform(delete("/genders/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenGenderNotFound() throws Exception {
    when(genderService.getById(1L)).thenThrow(new RuntimeException("Gender not found"));

    mockMvc
        .perform(get("/genders/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Gender not found"));
  }
}
