package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  // GET /customers
  @GetMapping
  public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

    return ResponseEntity.ok(customerService.getAll());
  }

  // GET /customers/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {

    try {

      return ResponseEntity.ok(customerService.getById(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // GET /customers/email/{email}
  @GetMapping("/email/{email}")
  public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {

    try {

      return ResponseEntity.ok(customerService.findByEmail(email));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /customers
  @PostMapping
  public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO dto) {

    try {

      CustomerDTO created = customerService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(created);

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // PUT /customers/{id}
  @PutMapping("/{id}")
  public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO dto) {

    try {

      CustomerDTO updated = customerService.update(id, dto);

      return ResponseEntity.ok(updated);

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // DELETE /customers/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {

    try {

      customerService.delete(id);

      return ResponseEntity.noContent().build();

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
