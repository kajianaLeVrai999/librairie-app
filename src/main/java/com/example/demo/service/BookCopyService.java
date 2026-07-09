package com.example.demo.service;

import com.example.demo.dto.BookCopyDTO;
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

  private final BookCopyRepository bookCopyRepository;
  private final BookRepository bookRepository;

  public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
    this.bookCopyRepository = bookCopyRepository;
    this.bookRepository = bookRepository;
  }

  public BookCopyDTO create(Integer bookId, BookCopyDTO dto) {
    Book book =
        bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

    BookCopy copy = new BookCopy();
    copy.setBook(book);
    copy.setStatus(dto.getStatus());
    copy.setFormat(dto.getFormat());

    BookCopy saved = bookCopyRepository.save(copy);
    return toDTO(saved);
  }

  public List<BookCopyDTO> createMultiple(Integer bookId, int quantity) {
    Book book =
        bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

    List<BookCopy> copies = new ArrayList<>();

    for (int i = 0; i < quantity; i++) {
      BookCopy copy = new BookCopy();
      copy.setBook(book);
      copy.setStatus(CopyStatus.AVAILABLE);
      copies.add(bookCopyRepository.save(copy));
    }

    return copies.stream().map(this::toDTO).toList();
  }

  public List<BookCopyDTO> getAll() {
    return bookCopyRepository.findAll().stream().map(this::toDTO).toList();
  }

  public BookCopyDTO getById(Integer id) {
    BookCopy copy =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("BookCopy not found"));

    return toDTO(copy);
  }

  public List<BookCopyDTO> getByBookId(Integer bookId) {
    return bookCopyRepository.findAll().stream()
        .filter(copy -> copy.getBook() != null && copy.getBook().getId() == bookId)
        .map(this::toDTO)
        .toList();
  }

  public List<BookCopyDTO> getAvailableCopies() {
    return bookCopyRepository.findAll().stream()
        .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
        .map(this::toDTO)
        .toList();
  }

  public BookCopyDTO updateStatus(Integer id, CopyStatus status) {
    BookCopy copy =
        bookCopyRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("BookCopy not found"));

    copy.setStatus(status);

    BookCopy updated = bookCopyRepository.save(copy);

    return toDTO(updated);
  }

  public void delete(Integer id) {
    if (!bookCopyRepository.existsById(id)) {
      throw new RuntimeException("BookCopy not found");
    }

    bookCopyRepository.deleteById(id);
  }

  public long countAvailableByBookId(Integer bookId) {
    return getByBookId(bookId).stream()
        .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
        .count();
  }

  // Stock total d'un livre (hors exemplaires vendus)
  public long getBookStock(Integer bookId) {
    return bookCopyRepository.countStockByBookId(bookId);
  }

  // Stock disponible d'une édition
  public long getEditionStock(Integer bookId) {
    return bookCopyRepository.countAvailableStock(bookId);
  }

  // Livres dont le stock est inférieur ou égal à 3
  public List<BookCopyDTO> getLowStockBooks() {

    List<Book> books = bookCopyRepository.findBooksWithLowStock();

    return books.stream()
        .map(
            book -> {
              BookCopyDTO dto = new BookCopyDTO();
              dto.setBookId(book.getId());
              dto.setBookTitle(book.getTitle());
              return dto;
            })
        .toList();
  }

  private BookCopyDTO toDTO(BookCopy copy) {
    BookCopyDTO dto = new BookCopyDTO();

    dto.setId(copy.getId());
    dto.setStatus(copy.getStatus());
    dto.setFormat(copy.getFormat());

    if (copy.getBook() != null) {
      dto.setBookId(copy.getBook().getId());
      dto.setBookTitle(copy.getBook().getTitle());
    }

    return dto;
  }
}
