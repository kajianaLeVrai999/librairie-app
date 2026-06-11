package com.example.demo.service;

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

  public Customer create(Customer customer) {
    return customerRepository.save(customer);
  }

  public List<Customer> getAll() {
    return customerRepository.findAll();
  }

  public Customer getById(Integer id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }

  public Customer update(Integer id, Customer customer) {
    Customer existing = getById(id);
    existing.setFirstName(customer.getFirstName());
    existing.setLastName(customer.getLastName());
    existing.setEmail(customer.getEmail());
    existing.setPhone(customer.getPhone());
    existing.setAddress(customer.getAddress());
    return customerRepository.save(existing);
  }

  public void delete(Integer id) {
    customerRepository.deleteById(id);
  }

  public Customer findByEmail(String email) {
    return customerRepository.findAll().stream()
        .filter(c -> c.getEmail().equals(email))
        .findFirst()
        .orElse(null);
  }
}
