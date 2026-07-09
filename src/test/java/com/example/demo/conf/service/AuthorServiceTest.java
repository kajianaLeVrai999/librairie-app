package com.example.demo.conf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.service.AuthorService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @Mock private AuthorRepository authorRepository;

  @InjectMocks private AuthorService authorService;

  private Author author;
  private AuthorDTO authorDTO;

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId(1);
    author.setFirstName("J.R.R.");
    author.setLastName("Tolkien");
    author.setBiography("Écrivain britannique");
    author.setNationality("Britannique");

    authorDTO = new AuthorDTO();
    authorDTO.setId(1);
    authorDTO.setFirstName("J.R.R.");
    authorDTO.setLastName("Tolkien");
    authorDTO.setBiography("Écrivain britannique");
    authorDTO.setNationality("Britannique");
  }

  @Test
  void shouldCreateAuthor() {
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    AuthorDTO result = authorService.create(authorDTO);

    assertNotNull(result);
    assertEquals("J.R.R.", result.getFirstName());
    assertEquals("Tolkien", result.getLastName());
    verify(authorRepository).save(any(Author.class));
  }

  @Test
  void shouldGetAllAuthors() {
    List<Author> authors = List.of(author, author);
    when(authorRepository.findAll()).thenReturn(authors);

    List<AuthorDTO> result = authorService.getAll();

    assertEquals(2, result.size());
    verify(authorRepository).findAll();
  }

  @Test
  void shouldGetAuthorById() {
    when(authorRepository.findById(1)).thenReturn(Optional.of(author));

    AuthorDTO result = authorService.getById(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
    assertEquals("J.R.R.", result.getFirstName());
  }

  @Test
  void shouldThrowWhenAuthorNotFound() {
    when(authorRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> authorService.getById(999));
  }

  @Test
  void shouldUpdateAuthor() {
    AuthorDTO updatedData = new AuthorDTO();
    updatedData.setFirstName("J.R.R.");
    updatedData.setLastName("Tolkien");
    updatedData.setBiography("Écrivain, philologue et universitaire britannique");
    updatedData.setNationality("Britannique");

    when(authorRepository.findById(1)).thenReturn(Optional.of(author));
    when(authorRepository.save(any(Author.class))).thenReturn(author);

    AuthorDTO result = authorService.update(1, updatedData);

    assertNotNull(result);
    assertEquals("Écrivain, philologue et universitaire britannique", result.getBiography());
  }

  @Test
  void shouldDeleteAuthor() {
    // Simuler que l'auteur existe
    when(authorRepository.existsById(1)).thenReturn(true);
    doNothing().when(authorRepository).deleteById(1);

    authorService.delete(1);

    verify(authorRepository).deleteById(1);
  }

  @Test
  void shouldThrowWhenDeletingAuthorNotFound() {
    // Simuler que l'auteur n'existe pas
    when(authorRepository.existsById(999)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> authorService.delete(999));
    verify(authorRepository, never()).deleteById(999);
  }
}
