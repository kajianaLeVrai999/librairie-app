package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    @Test
    void should_get_book_by_id() {

        BookRepository repository = mock(BookRepository.class);

        Book book = new Book();
        book.setTitle("Harry Potter");

        when(repository.findById(1))
                .thenReturn(Optional.of(book));

        BookService service =
                new BookService(repository);

        Book result = service.getById(1);

        assertEquals("Harry Potter", result.getTitle());
    }

    @Test
    void should_create_book() {

        BookRepository repository = mock(BookRepository.class);

        Book book = new Book();
        book.setTitle("Harry Potter");

        when(repository.save(book))
                .thenReturn(book);

        BookService service =
                new BookService(repository);

        Book saved = service.create(book);

        assertEquals("Harry Potter", saved.getTitle());

        verify(repository).save(book);
    }
}