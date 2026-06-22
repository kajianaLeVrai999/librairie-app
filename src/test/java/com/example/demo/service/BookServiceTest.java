package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import java.time.LocalDate;
import java.util.ArrayList;
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

  @Mock private BookRepository repository;

  @InjectMocks private BookService bookService;

  private Book sampleBook;

  @BeforeEach
  void setUp() {
    sampleBook =
        new Book(
            "Le Seigneur des Anneaux",
            "Un grand classique de la fantasy",
            19.99,
            LocalDate.of(1954, 7, 29),
            "978-2266286260",
            null,
            new ArrayList<>(),
            new ArrayList<>());
    sampleBook.setId(1);
  }

  @Test
  void create_ShouldReturnSavedBook() {
    when(repository.save(any(Book.class))).thenReturn(sampleBook);

    Book createdBook = bookService.create(sampleBook);

    assertNotNull(createdBook);
    assertEquals("Le Seigneur des Anneaux", createdBook.getTitle());
    verify(repository, times(1)).save(sampleBook);
  }

  @Test
  void getAll_ShouldReturnListOfBooks() {
    List<Book> books = List.of(sampleBook);
    when(repository.findAll()).thenReturn(books);

    List<Book> result = bookService.getAll();

    assertEquals(1, result.size());
    assertEquals("Le Seigneur des Anneaux", result.get(0).getTitle());
    verify(repository, times(1)).findAll();
  }

  @Test
  void getById_WhenIdExists_ShouldReturnBook() {
    when(repository.findById(1)).thenReturn(Optional.of(sampleBook));

    Book foundBook = bookService.getById(1);

    assertNotNull(foundBook);
    assertEquals(1, foundBook.getId());
    verify(repository, times(1)).findById(1);
  }

  @Test
  void getById_WhenIdDoesNotExist_ShouldThrowException() {
    when(repository.findById(99)).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              bookService.getById(99);
            });

    assertEquals("Book not found", exception.getMessage());
    verify(repository, times(1)).findById(99);
  }

  @Test
  void update_WhenBookExists_ShouldUpdateAndSaveBook() {
    Book updatedInfo =
        new Book(
            "Nouveau Titre",
            "Nouvelle Description",
            25.0,
            LocalDate.now(),
            "123-456789",
            null,
            new ArrayList<>(),
            new ArrayList<>());

    when(repository.findById(1)).thenReturn(Optional.of(sampleBook));
    when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Book result = bookService.update(1, updatedInfo);

    assertNotNull(result);
    assertEquals("Nouveau Titre", result.getTitle());
    assertEquals(25.0, result.getPrice());
    verify(repository, times(1)).findById(1);
    verify(repository, times(1)).save(sampleBook);
  }

  @Test
  void delete_ShouldCallRepositoryDelete() {
    doNothing().when(repository).deleteById(1);

    bookService.delete(1);

    verify(repository, times(1)).deleteById(1);
  }

  @Test
  void search_ShouldReturnMatchingBooks() {
    String keyword = "Seigneur";
    when(repository.findByTitleContainingIgnoreCase(keyword)).thenReturn(List.of(sampleBook));

    List<Book> result = bookService.search(keyword);

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertTrue(result.get(0).getTitle().contains(keyword));
    verify(repository, times(1)).findByTitleContainingIgnoreCase(keyword);
  }
}
