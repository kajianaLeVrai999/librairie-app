package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.BookFormat;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

    @Mock
    private BookCopyRepository copyRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    private Book sampleBook;
    private BookCopy sampleCopy;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1);
        sampleBook.setTitle("Le Seigneur des Anneaux");

        sampleCopy = new BookCopy(1, sampleBook, CopyStatus.AVAILABLE, BookFormat.PAPERBACK);
    }

    @Test
    void create_WhenBookExists_ShouldReturnSavedCopy() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        when(copyRepository.save(any(BookCopy.class))).thenReturn(sampleCopy);

        BookCopy createdCopy = bookCopyService.create(1, sampleCopy);

        assertNotNull(createdCopy);
        assertEquals(sampleBook, createdCopy.getBook());
        verify(bookRepository, times(1)).findById(1);
        verify(copyRepository, times(1)).save(sampleCopy);
    }

    @Test
    void create_WhenBookDoesNotExist_ShouldThrowException() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookCopyService.create(99, sampleCopy);
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(99);
        verify(copyRepository, never()).save(any(BookCopy.class));
    }

    @Test
    void createMultiple_WhenBookExists_ShouldCreateExactQuantity() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(sampleBook));
        when(copyRepository.save(any(BookCopy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<BookCopy> result = bookCopyService.createMultiple(1, 3);

        assertEquals(3, result.size());
        assertEquals(CopyStatus.AVAILABLE, result.get(0).getStatus());
        assertEquals(sampleBook, result.get(0).getBook());
        verify(bookRepository, times(1)).findById(1);
        verify(copyRepository, times(3)).save(any(BookCopy.class));
    }

    @Test
    void getAll_ShouldReturnAllCopies() {
        List<BookCopy> copies = List.of(sampleCopy);
        when(copyRepository.findAll()).thenReturn(copies);

        List<BookCopy> result = bookCopyService.getAll();

        assertEquals(1, result.size());
        verify(copyRepository, times(1)).findAll();
    }

    @Test
    void getById_WhenIdExists_ShouldReturnCopy() {
        when(copyRepository.findById(1)).thenReturn(Optional.of(sampleCopy));

        BookCopy foundCopy = bookCopyService.getById(1);

        assertNotNull(foundCopy);
        assertEquals(1, foundCopy.getId());
        verify(copyRepository, times(1)).findById(1);
    }

    @Test
    void getById_WhenIdDoesNotExist_ShouldThrowException() {
        when(copyRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookCopyService.getById(99);
        });

        assertEquals("BookCopy not found", exception.getMessage());
        verify(copyRepository, times(1)).findById(99);
    }

    @Test
    void getByBookId_ShouldReturnOnlyMatchingCopies() {
        Book otherBook = new Book();
        otherBook.setId(2);
        BookCopy otherCopy = new BookCopy(2, otherBook, CopyStatus.AVAILABLE, BookFormat.POCKET);

        when(copyRepository.findAll()).thenReturn(List.of(sampleCopy, otherCopy));

        List<BookCopy> result = bookCopyService.getByBookId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBook().getId());
        verify(copyRepository, times(1)).findAll();
    }

    @Test
    void getAvailableCopies_ShouldReturnOnlyAvailable() {
        BookCopy reservedCopy = new BookCopy(2, sampleBook, CopyStatus.RESERVED, BookFormat.PAPERBACK);
        when(copyRepository.findAll()).thenReturn(List.of(sampleCopy, reservedCopy));

        List<BookCopy> result = bookCopyService.getAvailableCopies();

        assertEquals(1, result.size());
        assertEquals(CopyStatus.AVAILABLE, result.get(0).getStatus());
        verify(copyRepository, times(1)).findAll();
    }

    @Test
    void updateStatus_ShouldChangeStatusAndSave() {
        when(copyRepository.findById(1)).thenReturn(Optional.of(sampleCopy));
        when(copyRepository.save(any(BookCopy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookCopy result = bookCopyService.updateStatus(1, CopyStatus.RESERVED);

        assertNotNull(result);
        assertEquals(CopyStatus.RESERVED, result.getStatus());
        verify(copyRepository, times(1)).findById(1);
        verify(copyRepository, times(1)).save(sampleCopy);
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(copyRepository).deleteById(1);

        bookCopyService.delete(1);

        verify(copyRepository, times(1)).deleteById(1);
    }

    @Test
    void countAvailableByBookId_ShouldReturnCorrectCount() {
        BookCopy secondAvailableCopy = new BookCopy(2, sampleBook, CopyStatus.AVAILABLE, BookFormat.PAPERBACK);
        BookCopy reservedCopy = new BookCopy(3, sampleBook, CopyStatus.RESERVED, BookFormat.PAPERBACK);
        
        when(copyRepository.findAll()).thenReturn(List.of(sampleCopy, secondAvailableCopy, reservedCopy));

        long count = bookCopyService.countAvailableByBookId(1);

        assertEquals(2, count);
        verify(copyRepository, times(1)).findAll();
    }
}