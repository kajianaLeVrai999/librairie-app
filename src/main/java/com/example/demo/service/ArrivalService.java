package com.example.demo.service;

import com.example.demo.dto.ArrivalDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Arrival;
import com.example.demo.entity.Book;
import com.example.demo.repository.ArrivalRepository;
import com.example.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArrivalService {

    private final ArrivalRepository arrivalRepository;
    private final BookRepository bookRepository;
    private final BookCopyService bookCopyService;

    public ArrivalService(
            ArrivalRepository arrivalRepository,
            BookRepository bookRepository,
            BookCopyService bookCopyService) {

        this.arrivalRepository = arrivalRepository;
        this.bookRepository = bookRepository;
        this.bookCopyService = bookCopyService;
    }

    // Créer un arrivage + création automatique des exemplaires
    public ArrivalDTO create(ArrivalDTO dto) {

        Book book = bookRepository.findById(dto.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Arrival arrival = new Arrival();

        arrival.setBook(book);
        arrival.setQuantity(dto.getQuantity());
        arrival.setSupplier(dto.getSupplier());
        arrival.setArrivalDate(LocalDate.now());

        Arrival saved = arrivalRepository.save(arrival);

        // Création automatique des copies
        bookCopyService.createMultiple(
                saved.getBook().getId(),
                saved.getQuantity()
        );

        return toDTO(saved);
    }


    // Récupérer tous les arrivages
    public List<ArrivalDTO> getAll() {

        return arrivalRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }


    // Récupérer un arrivage par ID
    public ArrivalDTO getById(Integer id) {

        Arrival arrival = arrivalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrival not found"));

        return toDTO(arrival);
    }


    // Supprimer un arrivage
    public void delete(Integer id) {

        Arrival arrival = arrivalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrival not found"));

        arrivalRepository.delete(arrival);
    }


    // Conversion Entity -> DTO
    private ArrivalDTO toDTO(Arrival arrival) {

        ArrivalDTO dto = new ArrivalDTO();

        dto.setId(arrival.getId());
        dto.setArrivalDate(arrival.getArrivalDate());
        dto.setQuantity(arrival.getQuantity());
        dto.setSupplier(arrival.getSupplier());


        Book book = arrival.getBook();

        if (book != null) {

            BookDTO bookDTO = new BookDTO();

            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setDescription(book.getDescription());
            bookDTO.setPrice(book.getPrice());
            bookDTO.setPublicationDate(book.getPublicationDate());
            bookDTO.setIsbn(book.getIsbn());

            dto.setBook(bookDTO);
        }

        return dto;
    }
}