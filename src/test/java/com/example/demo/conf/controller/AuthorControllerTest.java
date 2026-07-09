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

import com.example.demo.dto.AuthorDTO;
import com.example.demo.endpoint.rest.controller.AuthorController;
import com.example.demo.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthorService authorService;

  @Autowired private ObjectMapper objectMapper;

  private AuthorDTO createAuthorDTO() {
    AuthorDTO dto = new AuthorDTO();
    dto.setId(1);
    dto.setFirstName("Jane");
    dto.setLastName("Austen");
    return dto;
  }

  @Test
  void shouldGetAllAuthors() throws Exception {
    when(authorService.getAll()).thenReturn(List.of(createAuthorDTO()));

    mockMvc
        .perform(get("/authors"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetAuthorById() throws Exception {
    when(authorService.getById(1)).thenReturn(createAuthorDTO());

    mockMvc
        .perform(get("/authors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturn404WhenAuthorNotFound() throws Exception {
    when(authorService.getById(1)).thenThrow(new RuntimeException("Author not found"));

    mockMvc
        .perform(get("/authors/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Author not found"));
  }

  @Test
  void shouldCreateAuthor() throws Exception {
    AuthorDTO dto = createAuthorDTO();
    when(authorService.create(any(AuthorDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldUpdateAuthor() throws Exception {
    AuthorDTO dto = createAuthorDTO();
    when(authorService.update(eq(1), any(AuthorDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteAuthor() throws Exception {
    when(authorService.getById(1)).thenReturn(createAuthorDTO());
    doNothing().when(authorService).delete(1);

    mockMvc.perform(delete("/authors/1")).andExpect(status().isNoContent());
  }
}
