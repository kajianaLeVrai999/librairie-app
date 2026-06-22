package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Customer;
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
public ResponseEntity<List<Customer>> getAllCustomers() {
    return ResponseEntity.ok(customerService.getAll());
}

// GET /customers/{id}
@GetMapping("/{id}")
public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
    try {
        return ResponseEntity.ok(customerService.getById(id));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}

// GET /customers/email/{email}
@GetMapping("/email/{email}")
public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
    Customer customer = customerService.findByEmail(email);

    if (customer == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Customer not found");
    }

    return ResponseEntity.ok(customer);
}

// POST /customers
@PostMapping
public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
    try {
        Customer created = customerService.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// PUT /customers/{id}
@PutMapping("/{id}")
public ResponseEntity<?> updateCustomer(
        @PathVariable Integer id,
        @RequestBody Customer customer) {

    try {
        Customer updated = customerService.update(id, customer);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

// DELETE /customers/{id}
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
    try {
        customerService.getById(id);
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}


}