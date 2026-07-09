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

import com.example.demo.dto.CustomerDTO;
import com.example.demo.endpoint.rest.controller.CustomerController;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CustomerService customerService;

  @Autowired private ObjectMapper objectMapper;

  private CustomerDTO createCustomerDTO() {
    CustomerDTO dto = new CustomerDTO();
    dto.setId(1);
    dto.setFirstName("Alice");
    dto.setLastName("Martin");
    dto.setEmail("alice@example.com");
    return dto;
  }

  @Test
  void shouldGetAllCustomers() throws Exception {
    when(customerService.getAll()).thenReturn(List.of(createCustomerDTO()));

    mockMvc
        .perform(get("/customers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void shouldGetCustomerById() throws Exception {
    when(customerService.getById(1)).thenReturn(createCustomerDTO());

    mockMvc
        .perform(get("/customers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldGetCustomerByEmail() throws Exception {
    when(customerService.findByEmail("alice@example.com")).thenReturn(createCustomerDTO());

    mockMvc
        .perform(get("/customers/email/alice@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("alice@example.com"));
  }

  @Test
  void shouldCreateCustomer() throws Exception {
    CustomerDTO dto = createCustomerDTO();
    when(customerService.create(any(CustomerDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldUpdateCustomer() throws Exception {
    CustomerDTO dto = createCustomerDTO();
    when(customerService.update(eq(1), any(CustomerDTO.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteCustomer() throws Exception {
    doNothing().when(customerService).delete(1);

    mockMvc.perform(delete("/customers/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenCustomerNotFound() throws Exception {
    when(customerService.getById(1)).thenThrow(new RuntimeException("Customer not found"));

    mockMvc
        .perform(get("/customers/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Customer not found"));
  }
}
