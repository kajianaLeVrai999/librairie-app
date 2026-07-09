package com.example.demo.conf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepository bookRepository;

  @InjectMocks private BookService bookService;

  private Book book;
  private BookDTO bookDTO;

  @BeforeEach
  void setUp() {
    book = new Book();
    book.setId(1);
    book.setTitle("Le Seigneur des Anneaux");
    book.setDescription("Un grand classique de la fantasy");
    book.setPrice(19.99);
    book.setPublicationDate(LocalDate.of(1954, 7, 29));
    book.setIsbn("978-2266286260");

    bookDTO = new BookDTO();
    bookDTO.setTitle("Le Seigneur des Anneaux");
    bookDTO.setDescription("Un grand classique de la fantasy");
    bookDTO.setPrice(19.99);
    bookDTO.setPublicationDate(LocalDate.of(1954, 7, 29));
    bookDTO.setIsbn("978-2266286260");
  }

  @Test
  void shouldCreateBook() {
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    BookDTO result = bookService.create(bookDTO);

    assertNotNull(result);
    assertEquals("Le Seigneur des Anneaux", result.getTitle());
    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void shouldGetAllBooks() {
    List<Book> books = List.of(book, book);
    when(bookRepository.findAll()).thenReturn(books);

    List<BookDTO> result = bookService.getAll();

    assertEquals(2, result.size());
    verify(bookRepository).findAll();
  }

  @Test
  void shouldGetBookById() {
    when(bookRepository.findById(1)).thenReturn(Optional.of(book));

    BookDTO result = bookService.getById(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
    assertEquals("Le Seigneur des Anneaux", result.getTitle());
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    when(bookRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> bookService.getById(999));
  }

  @Test
  void shouldUpdateBook() {
    BookDTO updatedData = new BookDTO();
    updatedData.setTitle("Le Seigneur des Anneaux - Édition Spéciale");
    updatedData.setDescription("Un grand classique de la fantasy");
    updatedData.setPrice(24.99);
    updatedData.setPublicationDate(LocalDate.of(1954, 7, 29));
    updatedData.setIsbn("978-2266286260");

    when(bookRepository.findById(1)).thenReturn(Optional.of(book));
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    BookDTO result = bookService.update(1, updatedData);

    assertNotNull(result);
    assertEquals("Le Seigneur des Anneaux - Édition Spéciale", result.getTitle());
    assertEquals(24.99, result.getPrice());
  }

  @Test
  void shouldDeleteBook() {
    // Simuler que le livre existe
    when(bookRepository.existsById(1)).thenReturn(true);
    doNothing().when(bookRepository).deleteById(1);

    bookService.delete(1);

    verify(bookRepository).deleteById(1);
  }

  @Test
  void shouldThrowWhenDeletingBookNotFound() {
    // Simuler que le livre n'existe pas
    when(bookRepository.existsById(999)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> bookService.delete(999));
    verify(bookRepository, never()).deleteById(999);
  }

  @Test
  void shouldSearchBooksByTitle() {
    String keyword = "Seigneur";
    when(bookRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(List.of(book));

    List<BookDTO> result = bookService.search(keyword);

    assertEquals(1, result.size());
    assertTrue(result.get(0).getTitle().contains(keyword));
  }
}
