package com.example.demo.conf.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.endpoint.rest.controller.BookCopyController;
import com.example.demo.entity.BookFormat;
import com.example.demo.entity.CopyStatus;
import com.example.demo.service.BookCopyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookCopyController.class)
class BookCopyControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookCopyService bookCopyService;

  @Autowired private ObjectMapper objectMapper;

  private BookCopyDTO createBookCopyDTO() {
    BookCopyDTO dto = new BookCopyDTO();
    dto.setId(1);
    dto.setStatus(CopyStatus.AVAILABLE);
    dto.setFormat(BookFormat.PAPERBACK);
    dto.setBookId(10);
    return dto;
  }

  @Test
  void shouldGetAllCopies() throws Exception {
    when(bookCopyService.getAll()).thenReturn(List.of(createBookCopyDTO()));

    mockMvc
        .perform(get("/book-copies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetCopyById() throws Exception {
    when(bookCopyService.getById(1)).thenReturn(createBookCopyDTO());

    mockMvc
        .perform(get("/book-copies/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldCreateCopy() throws Exception {
    BookCopyDTO dto = createBookCopyDTO();
    when(bookCopyService.create(eq(10), any(BookCopyDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/book-copies/book/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldCreateMultipleCopies() throws Exception {
    when(bookCopyService.createMultiple(10, 2)).thenReturn(List.of(createBookCopyDTO()));

    mockMvc
        .perform(post("/book-copies/book/10/multiple").param("quantity", "2"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldUpdateStatus() throws Exception {
    when(bookCopyService.updateStatus(1, CopyStatus.RESERVED)).thenReturn(createBookCopyDTO());

    mockMvc
        .perform(patch("/book-copies/1/status").param("status", "RESERVED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteCopy() throws Exception {
    when(bookCopyService.getById(1)).thenReturn(createBookCopyDTO());
    org.mockito.Mockito.doNothing().when(bookCopyService).delete(1);

    mockMvc.perform(delete("/book-copies/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenCopyNotFound() throws Exception {
    when(bookCopyService.getById(1)).thenThrow(new RuntimeException("Book copy not found"));

    mockMvc
        .perform(get("/book-copies/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Book copy not found"));
  }
}
