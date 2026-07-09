package com.example.demo.service;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.dto.BookDTO;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.GenderDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }


    // CREATE
    public BookDTO create(BookDTO dto) {

        Book book = new Book();

        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setPublicationDate(dto.getPublicationDate());
        book.setIsbn(dto.getIsbn());

        Book saved = repository.save(book);

        return toDTO(saved);
    }


    // GET ALL
    public List<BookDTO> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }


    // GET BY ID
    public BookDTO getById(Integer id) {

        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return toDTO(book);
    }


    // UPDATE
    public BookDTO update(Integer id, BookDTO dto) {

        Book existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));


        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setPublicationDate(dto.getPublicationDate());
        existing.setIsbn(dto.getIsbn());


        Book updated = repository.save(existing);

        return toDTO(updated);
    }


    // DELETE
    public void delete(Integer id) {

        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        repository.delete(book);
    }


    // SEARCH
    public List<BookDTO> search(String keyword) {

        return repository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::toDTO)
                .toList();
    }


    // ENTITY -> DTO
    private BookDTO toDTO(Book book) {

        BookDTO dto = new BookDTO();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setIsbn(book.getIsbn());


        if (book.getCategory() != null) {

            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(book.getCategory().getId());
            categoryDTO.setName(book.getCategory().getName());

            dto.setCategory(categoryDTO);
        }


        if (book.getAuthors() != null) {

            dto.setAuthors(
                    book.getAuthors()
                    .stream()
                    .map(author -> {
                        AuthorDTO authorDTO = new AuthorDTO();

                        authorDTO.setId(author.getId());
                        authorDTO.setFirstName(author.getFirstName());
                        authorDTO.setLastName(author.getLastName());
                        authorDTO.setBiography(author.getBiography());
                        authorDTO.setNationality(author.getNationality());

                        return authorDTO;
                    })
                    .toList()
            );
        }


        return dto;
    }
}