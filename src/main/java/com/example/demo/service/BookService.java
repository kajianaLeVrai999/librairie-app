package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public BookDTO create(BookDTO dto) {
    Book book = new Book();
    book.setTitle(dto.getTitle());
    book.setDescription(dto.getDescription());
    book.setPrice(dto.getPrice());
    book.setPublicationDate(dto.getPublicationDate());
    book.setIsbn(dto.getIsbn());
    // Ne pas définir category, authors, genders ici pour simplifier
    Book saved = bookRepository.save(book);
    return toDTO(saved);
  }

  public List<BookDTO> getAll() {
    return bookRepository.findAll().stream().map(this::toDTO).toList();
  }

  public BookDTO getById(Integer id) {
    Book book =
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    return toDTO(book);
  }

  public BookDTO update(Integer id, BookDTO dto) {
    Book existing =
        bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    existing.setTitle(dto.getTitle());
    existing.setDescription(dto.getDescription());
    existing.setPrice(dto.getPrice());
    existing.setPublicationDate(dto.getPublicationDate());
    existing.setIsbn(dto.getIsbn());
    // Ne pas mettre à jour category, authors, genders ici pour simplifier
    Book updated = bookRepository.save(existing);
    return toDTO(updated);
  }

  public void delete(Integer id) {
    if (!bookRepository.existsById(id)) {
      throw new RuntimeException("Book not found");
    }
    bookRepository.deleteById(id);
  }

  public List<BookDTO> search(String keyword) {
    return bookRepository.findByTitleContainingIgnoreCase(keyword).stream()
        .map(this::toDTO)
        .toList();
  }

  private BookDTO toDTO(Book book) {
    BookDTO dto = new BookDTO();
    dto.setId(book.getId());
    dto.setTitle(book.getTitle());
    dto.setDescription(book.getDescription());
    dto.setPrice(book.getPrice());
    dto.setPublicationDate(book.getPublicationDate());
    dto.setIsbn(book.getIsbn());
    dto.setCategory(book.getCategory());
    dto.setAuthors(book.getAuthors());
    dto.setGenders(book.getGenders());
    return dto;
  }
}
