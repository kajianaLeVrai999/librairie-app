package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @Mock private AuthorRepository authorRepository;

  @InjectMocks private AuthorService authorService;

  @Test
  void shouldCreateAuthor() {
    Author author = new Author();
    author.setFirstName("Victor");
    author.setLastName("Hugo");

    when(authorRepository.save(author)).thenReturn(author);

    Author result = authorService.create(author);

    assertEquals("Victor", result.getFirstName());
    assertEquals("Hugo", result.getLastName());

    verify(authorRepository).save(author);
  }

  @Test
  void shouldGetAllAuthors() {
    List<Author> authors = List.of(new Author(), new Author());

    when(authorRepository.findAll()).thenReturn(authors);

    List<Author> result = authorService.getAll();

    assertEquals(2, result.size());

    verify(authorRepository).findAll();
  }

  @Test
  void shouldGetAuthorById() {
    Author author = new Author();
    author.setId(1);

    when(authorRepository.findById(1)).thenReturn(Optional.of(author));

    Author result = authorService.getById(1);

    assertEquals(1, result.getId());

    verify(authorRepository).findById(1);
  }

  @Test
  void shouldThrowExceptionWhenAuthorNotFound() {
    when(authorRepository.findById(1)).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> authorService.getById(1));

    assertEquals("Author not found", exception.getMessage());

    verify(authorRepository).findById(1);
  }

  @Test
  void shouldUpdateAuthor() {
    Author existing = new Author();
    existing.setId(1);
    existing.setFirstName("Old");
    existing.setLastName("Name");

    Author updatedData = new Author();
    updatedData.setFirstName("Victor");
    updatedData.setLastName("Hugo");
    updatedData.setBiography("Writer");
    updatedData.setNationality("French");

    when(authorRepository.findById(1)).thenReturn(Optional.of(existing));

    when(authorRepository.save(any(Author.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Author result = authorService.update(1, updatedData);

    assertEquals("Victor", result.getFirstName());
    assertEquals("Hugo", result.getLastName());
    assertEquals("Writer", result.getBiography());
    assertEquals("French", result.getNationality());

    verify(authorRepository).save(existing);
  }

  @Test
  void shouldDeleteAuthor() {
    authorService.delete(1);

    verify(authorRepository).deleteById(1);
  }
}
