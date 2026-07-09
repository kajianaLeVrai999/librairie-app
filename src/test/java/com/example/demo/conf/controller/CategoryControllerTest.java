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

import com.example.demo.dto.CategoryDTO;
import com.example.demo.endpoint.rest.controller.CategoryController;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryService categoryService;

  @Autowired private ObjectMapper objectMapper;

  private CategoryDTO createCategoryDTO() {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(1L);
    dto.setName("Science Fiction");
    return dto;
  }

  @Test
  void shouldGetAllCategories() throws Exception {
    when(categoryService.getAll()).thenReturn(List.of(createCategoryDTO()));

    mockMvc
        .perform(get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetCategoryById() throws Exception {
    when(categoryService.getById(1L)).thenReturn(createCategoryDTO());

    mockMvc
        .perform(get("/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldCreateCategory() throws Exception {
    CategoryDTO dto = createCategoryDTO();
    when(categoryService.create(any(CategoryDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldUpdateCategory() throws Exception {
    CategoryDTO dto = createCategoryDTO();
    when(categoryService.update(eq(1L), any(CategoryDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteCategory() throws Exception {
    doNothing().when(categoryService).delete(1L);

    mockMvc.perform(delete("/categories/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenCategoryNotFound() throws Exception {
    when(categoryService.getById(1L)).thenThrow(new RuntimeException("Category not found"));

    mockMvc
        .perform(get("/categories/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Category not found"));
  }
}
