package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "customer-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Integer id, Model model) {
        Customer customer = customerRepository.findById(id).orElse(null);
        model.addAttribute("customer", customer);
        return "customer-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id) {
        customerRepository.deleteById(id);
        return "redirect:/customers";
    }
}