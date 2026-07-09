package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

    private final BookCopyRepository copyRepository;
    private final BookRepository bookRepository;


    public BookCopyService(
            BookCopyRepository copyRepository,
            BookRepository bookRepository) {

        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }


    // Créer un exemplaire
    public BookCopyDTO create(Integer bookId, BookCopyDTO dto) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));


        BookCopy copy = new BookCopy();

        copy.setBook(book);
        copy.setStatus(dto.getStatus());
        copy.setFormat(dto.getFormat());


        BookCopy saved = copyRepository.save(copy);

        return toDTO(saved);
    }



    // Créer plusieurs exemplaires (arrivage)
    public List<BookCopyDTO> createMultiple(Integer bookId, int quantity) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));


        List<BookCopyDTO> result = new ArrayList<>();


        for (int i = 0; i < quantity; i++) {

            BookCopy copy = new BookCopy();

            copy.setBook(book);
            copy.setStatus(CopyStatus.AVAILABLE);

            BookCopy saved = copyRepository.save(copy);

            result.add(toDTO(saved));
        }


        return result;
    }



    // GET ALL
    public List<BookCopyDTO> getAll() {

        return copyRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }



    // GET BY ID
    public BookCopyDTO getById(Integer id) {

        BookCopy copy = copyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookCopy not found"));

        return toDTO(copy);
    }



    // Exemplaires d'un livre
    public List<BookCopyDTO> getByBookId(Integer bookId) {

        return copyRepository.findAll()
                .stream()
                .filter(copy ->
                        copy.getBook() != null
                        && copy.getBook().getId() == bookId)
                .map(this::toDTO)
                .toList();
    }



    // Exemplaires disponibles
    public List<BookCopyDTO> getAvailableCopies() {

        return copyRepository.findAll()
                .stream()
                .filter(copy ->
                        copy.getStatus() == CopyStatus.AVAILABLE)
                .map(this::toDTO)
                .toList();
    }



    // Modifier statut
    public BookCopyDTO updateStatus(Integer id, CopyStatus status) {

        BookCopy copy = copyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookCopy not found"));


        copy.setStatus(status);

        return toDTO(copyRepository.save(copy));
    }



    // DELETE
    public void delete(Integer id) {

        BookCopy copy = copyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookCopy not found"));

        copyRepository.delete(copy);
    }



    // Compter disponibles
    public long countAvailableByBookId(Integer bookId) {

        return copyRepository.findAll()
                .stream()
                .filter(copy ->
                        copy.getBook().getId() == bookId
                        && copy.getStatus() == CopyStatus.AVAILABLE)
                .count();
    }



    // ENTITY -> DTO
    private BookCopyDTO toDTO(BookCopy copy) {

        BookCopyDTO dto = new BookCopyDTO();

        dto.setId(copy.getId());
        dto.setStatus(copy.getStatus());
        dto.setFormat(copy.getFormat());


        if(copy.getBook() != null){

            BookDTO bookDTO = new BookDTO();

            bookDTO.setId(copy.getBook().getId());
            bookDTO.setTitle(copy.getBook().getTitle());
            bookDTO.setIsbn(copy.getBook().getIsbn());

            dto.setBook(bookDTO);
        }


        return dto;
    }
}