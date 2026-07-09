package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.SaleDTO;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Sale;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SaleRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SaleService {


    private final SaleRepository saleRepository;
    private final BookCopyRepository bookCopyRepository;
    private final CustomerRepository customerRepository;


    public SaleService(
            SaleRepository saleRepository,
            BookCopyRepository bookCopyRepository,
            CustomerRepository customerRepository) {

        this.saleRepository = saleRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.customerRepository = customerRepository;
    }



    // CREATE
    public SaleDTO create(SaleDTO dto) {


        Customer customer = customerRepository.findById(
                dto.getCustomer().getId()
        ).orElseThrow(() ->
                new RuntimeException("Customer not found")
        );


        BookCopy bookCopy = bookCopyRepository.findById(
                dto.getBookCopy().getId()
        ).orElseThrow(() ->
                new RuntimeException("BookCopy not found")
        );


        Sale sale = new Sale();

        sale.setCustomer(customer);
        sale.setBookCopy(bookCopy);

        sale.setQuantity(dto.getQuantity());
        sale.setTotalAmount(dto.getTotalAmount());

        sale.setSaleDate(LocalDate.now());


        // Changer le statut de l'exemplaire
        bookCopy.setStatus(CopyStatus.SOLD);
        bookCopyRepository.save(bookCopy);


        Sale saved = saleRepository.save(sale);


        return toDTO(saved);
    }



    // GET ALL
    public List<SaleDTO> getAll() {

        return saleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }



    // GET BY ID
    public SaleDTO getById(String id) {

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sale not found")
                );

        return toDTO(sale);
    }



    // GET BY DATE
    public List<SaleDTO> getSalesByDate(LocalDate date) {

        return saleRepository.findAll()
                .stream()
                .filter(sale ->
                        sale.getSaleDate() != null
                        &&
                        sale.getSaleDate().equals(date)
                )
                .map(this::toDTO)
                .toList();
    }



    // TOTAL REVENUE
    public double getTotalRevenueByDate(LocalDate date) {

        return saleRepository.findAll()
                .stream()
                .filter(sale ->
                        sale.getSaleDate() != null
                        &&
                        sale.getSaleDate().equals(date)
                )
                .mapToDouble(Sale::getTotalAmount)
                .sum();
    }



    // ENTITY -> DTO
    private SaleDTO toDTO(Sale sale) {

        SaleDTO dto = new SaleDTO();


        dto.setId(sale.getId());
        dto.setSaleDate(sale.getSaleDate());
        dto.setQuantity(sale.getQuantity());
        dto.setTotalAmount(sale.getTotalAmount());



        if(sale.getCustomer() != null){

            Customer customer = sale.getCustomer();

            CustomerDTO customerDTO =
                    new CustomerDTO();

            customerDTO.setId(customer.getId());
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setPhone(customer.getPhone());
            customerDTO.setAddress(customer.getAddress());


            dto.setCustomer(customerDTO);
        }



        if(sale.getBookCopy() != null){

            BookCopy copy = sale.getBookCopy();


            BookCopyDTO copyDTO =
                    new BookCopyDTO();

            copyDTO.setId(copy.getId());
            copyDTO.setStatus(copy.getStatus());
            copyDTO.setFormat(copy.getFormat());


            dto.setBookCopy(copyDTO);
        }


        return dto;
    }
}