package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  // CREATE
  public CustomerDTO create(CustomerDTO dto) {

    Customer customer = new Customer();

    customer.setFirstName(dto.getFirstName());
    customer.setLastName(dto.getLastName());
    customer.setEmail(dto.getEmail());
    customer.setPhone(dto.getPhone());
    customer.setAddress(dto.getAddress());

    Customer saved = customerRepository.save(customer);

    return toDTO(saved);
  }

  // GET ALL
  public List<CustomerDTO> getAll() {

    return customerRepository.findAll().stream().map(this::toDTO).toList();
  }

  // GET BY ID
  public CustomerDTO getById(Integer id) {

    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    return toDTO(customer);
  }

  // GET BY EMAIL
  public CustomerDTO findByEmail(String email) {

    Customer customer =
        customerRepository.findAll().stream()
            .filter(c -> c.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    return toDTO(customer);
  }

  // UPDATE
  public CustomerDTO update(Integer id, CustomerDTO dto) {

    Customer existing =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    existing.setFirstName(dto.getFirstName());
    existing.setLastName(dto.getLastName());
    existing.setEmail(dto.getEmail());
    existing.setPhone(dto.getPhone());
    existing.setAddress(dto.getAddress());

    Customer updated = customerRepository.save(existing);

    return toDTO(updated);
  }

  // DELETE
  public void delete(Integer id) {

    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    customerRepository.delete(customer);
  }

  // ENTITY -> DTO
  private CustomerDTO toDTO(Customer customer) {

    return new CustomerDTO(
        customer.getId(),
        customer.getFirstName(),
        customer.getLastName(),
        customer.getEmail(),
        customer.getPhone(),
        customer.getAddress());
  }
}
