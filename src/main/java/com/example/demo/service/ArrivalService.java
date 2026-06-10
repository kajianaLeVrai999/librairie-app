package com.example.demo.service;

import com.example.demo.entity.Arrival;
import com.example.demo.repository.ArrivalRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ArrivalService {

    private final ArrivalRepository arrivalRepository;
    private final BookRepository bookRepository;
    private final BookCopyService bookCopyService;

    public ArrivalService(ArrivalRepository arrivalRepository,
                          BookRepository bookRepository,
                          BookCopyService bookCopyService) {
        this.arrivalRepository = arrivalRepository;
        this.bookRepository = bookRepository;
        this.bookCopyService = bookCopyService;
    }

    // Enregistrer un arrivage (crée automatiquement les exemplaires)
    public Arrival create(Arrival arrival) {
        arrival.setArrivalDate(LocalDate.now());
        Arrival saved = arrivalRepository.save(arrival);

        // Crée les exemplaires
        bookCopyService.createMultiple(saved.getBook().getId(), saved.getQuantity());

        return saved;
    }

    // Récupérer tous les arrivages
    public List<Arrival> getAll() {
        return arrivalRepository.findAll();
    }

    // Récupérer un arrivage par ID
    public Arrival getById(Integer id) {
        return arrivalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrival not found"));
    }

    // Supprimer un arrivage
    public void delete(Integer id) {
        arrivalRepository.deleteById(id);
    }
}