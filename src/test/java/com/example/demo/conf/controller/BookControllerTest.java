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

import com.example.demo.dto.BookDTO;
import com.example.demo.endpoint.rest.controller.BookController;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @Autowired private ObjectMapper objectMapper;

  private BookDTO createBookDTO() {
    BookDTO dto = new BookDTO();
    dto.setId(1);
    dto.setTitle("Dune");
    dto.setPublicationDate(LocalDate.of(1965, 8, 1));
    return dto;
  }

  @Test
  void shouldGetAllBooks() throws Exception {
    when(bookService.getAll()).thenReturn(List.of(createBookDTO()));

    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetBookById() throws Exception {
    when(bookService.getById(1)).thenReturn(createBookDTO());

    mockMvc
        .perform(get("/books/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldReturn404WhenBookNotFound() throws Exception {
    when(bookService.getById(1)).thenThrow(new RuntimeException("Book not found"));

    mockMvc
        .perform(get("/books/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Book not found"));
  }

  @Test
  void shouldCreateBook() throws Exception {
    BookDTO dto = createBookDTO();
    when(bookService.create(any(BookDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldUpdateBook() throws Exception {
    BookDTO dto = createBookDTO();
    when(bookService.update(eq(1), any(BookDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteBook() throws Exception {
    when(bookService.getById(1)).thenReturn(createBookDTO());
    doNothing().when(bookService).delete(1);

    mockMvc.perform(delete("/books/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldSearchBooks() throws Exception {
    when(bookService.search("dune")).thenReturn(List.of(createBookDTO()));

    mockMvc
        .perform(get("/books/search").param("keyword", "dune"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Dune"));
  }
}
