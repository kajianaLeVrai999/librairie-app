package com.example.demo.conf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.BookFormat;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookCopyService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock private BookCopyRepository bookCopyRepository;

  @Mock private BookRepository bookRepository;

  @InjectMocks private BookCopyService bookCopyService;

  private Book book;
  private BookCopy bookCopy;
  private BookCopyDTO bookCopyDTO;

  @BeforeEach
  void setUp() {
    book = new Book();
    book.setId(1);
    book.setTitle("Le Seigneur des Anneaux");

    bookCopy = new BookCopy();
    bookCopy.setId(1);
    bookCopy.setBook(book);
    bookCopy.setStatus(CopyStatus.AVAILABLE);
    bookCopy.setFormat(BookFormat.PAPERBACK);

    bookCopyDTO = new BookCopyDTO();
    bookCopyDTO.setStatus(CopyStatus.AVAILABLE);
    bookCopyDTO.setFormat(BookFormat.PAPERBACK);
  }

  @Test
  void shouldCreateBookCopy() {
    when(bookRepository.findById(1)).thenReturn(Optional.of(book));
    when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

    BookCopyDTO result = bookCopyService.create(1, bookCopyDTO);

    assertNotNull(result);
    assertEquals(CopyStatus.AVAILABLE, result.getStatus());
    verify(bookCopyRepository).save(any(BookCopy.class));
  }

  @Test
  void shouldThrowWhenBookNotFoundForCopy() {
    when(bookRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> bookCopyService.create(999, bookCopyDTO));
  }

  @Test
  void shouldCreateMultipleCopies() {
    when(bookRepository.findById(1)).thenReturn(Optional.of(book));
    when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

    List<BookCopyDTO> results = bookCopyService.createMultiple(1, 3);

    assertEquals(3, results.size());
    verify(bookCopyRepository, times(3)).save(any(BookCopy.class));
  }

  @Test
  void shouldGetAllCopies() {
    List<BookCopy> copies = List.of(bookCopy, bookCopy);
    when(bookCopyRepository.findAll()).thenReturn(copies);

    List<BookCopyDTO> result = bookCopyService.getAll();

    assertEquals(2, result.size());
  }

  @Test
  void shouldGetCopyById() {
    when(bookCopyRepository.findById(1)).thenReturn(Optional.of(bookCopy));

    BookCopyDTO result = bookCopyService.getById(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
  }

  @Test
  void shouldThrowWhenCopyNotFound() {
    when(bookCopyRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> bookCopyService.getById(999));
  }

  @Test
  void shouldGetCopiesByBookId() {
    List<BookCopy> copies = List.of(bookCopy);
    when(bookCopyRepository.findAll()).thenReturn(copies);

    List<BookCopyDTO> result = bookCopyService.getByBookId(1);

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetAvailableCopies() {
    List<BookCopy> copies = List.of(bookCopy);
    when(bookCopyRepository.findAll()).thenReturn(copies);

    List<BookCopyDTO> result = bookCopyService.getAvailableCopies();

    assertEquals(1, result.size());
    assertEquals(CopyStatus.AVAILABLE, result.get(0).getStatus());
  }

  @Test
  void shouldUpdateCopyStatus() {
    when(bookCopyRepository.findById(1)).thenReturn(Optional.of(bookCopy));
    when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(bookCopy);

    BookCopyDTO result = bookCopyService.updateStatus(1, CopyStatus.RESERVED);

    assertEquals(CopyStatus.RESERVED, result.getStatus());
  }

  @Test
  void shouldDeleteCopy() {
    // Simuler que la copie existe
    when(bookCopyRepository.existsById(1)).thenReturn(true);
    doNothing().when(bookCopyRepository).deleteById(1);

    bookCopyService.delete(1);

    verify(bookCopyRepository).deleteById(1);
  }

  @Test
  void shouldThrowWhenDeletingCopyNotFound() {
    // Simuler que la copie n'existe pas
    when(bookCopyRepository.existsById(999)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> bookCopyService.delete(999));
    verify(bookCopyRepository, never()).deleteById(999);
  }

  @Test
  void shouldCountAvailableCopiesByBookId() {
    List<BookCopy> copies = List.of(bookCopy, bookCopy);
    when(bookCopyRepository.findAll()).thenReturn(copies);

    long count = bookCopyService.countAvailableByBookId(1);

    assertEquals(2, count);
  }
}
