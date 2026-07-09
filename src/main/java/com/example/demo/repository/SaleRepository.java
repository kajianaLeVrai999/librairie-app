package com.example.demo.repository;

import com.example.demo.entity.Sale;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String> {

  List<Sale> findBySaleDate(LocalDate date);

  List<Sale> findByCustomerId(Integer customerId);

  // NOUVEAU : Ventes par genre
  @Query(
      "SELECT g.id, g.name, COUNT(s), SUM(s.totalAmount) "
          + "FROM Sale s "
          + "JOIN s.bookCopy bc "
          + "JOIN bc.book b "
          + "JOIN b.genders g "
          + "GROUP BY g.id, g.name "
          + "ORDER BY COUNT(s) DESC")
  List<Object[]> findSalesByGender();
}
