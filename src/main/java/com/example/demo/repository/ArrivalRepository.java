package com.example.demo.repository;

import com.example.demo.entity.Arrival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalRepository extends JpaRepository<Arrival, Integer> {}
