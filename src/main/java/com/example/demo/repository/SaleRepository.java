package com.example.demo.repository;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, String> {
    List<Sale> findBySaleDate(LocalDate date);
    List<Sale> findByCustomerId(Integer customerId);
}