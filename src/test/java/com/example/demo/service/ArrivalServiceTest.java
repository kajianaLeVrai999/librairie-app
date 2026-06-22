package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import com.example.demo.entity.Arrival;
import com.example.demo.entity.Book;
import com.example.demo.repository.ArrivalRepository;
import com.example.demo.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class ArrivalServiceTest {

@Mock
private ArrivalRepository arrivalRepository;

@Mock
private BookRepository bookRepository;

@Mock
private BookCopyService bookCopyService;

@InjectMocks
private ArrivalService arrivalService;

@Test
void shouldCreateArrival() {
    Book book = new Book();
    book.setId(1);

    Arrival arrival = new Arrival();
    arrival.setBook(book);
    arrival.setQuantity(5);

    when(arrivalRepository.save(any(Arrival.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Arrival result = arrivalService.create(arrival);

    assertNotNull(result.getArrivalDate());
    assertEquals(5, result.getQuantity());

    verify(arrivalRepository).save(arrival);
    verify(bookCopyService).createMultiple(1, 5);
}

@Test
void shouldGetAllArrivals() {
    List<Arrival> arrivals =
            List.of(new Arrival(), new Arrival());

    when(arrivalRepository.findAll())
            .thenReturn(arrivals);

    List<Arrival> result = arrivalService.getAll();

    assertEquals(2, result.size());
    verify(arrivalRepository).findAll();
}

@Test
void shouldGetArrivalById() {
    Arrival arrival = new Arrival();

    when(arrivalRepository.findById(1))
            .thenReturn(Optional.of(arrival));

    Arrival result = arrivalService.getById(1);

    assertEquals(arrival, result);
}

@Test
void shouldThrowWhenArrivalNotFound() {
    when(arrivalRepository.findById(1))
            .thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> arrivalService.getById(1));

    assertEquals(
            "Arrival not found",
            exception.getMessage());
}

@Test
void shouldDeleteArrival() {
    arrivalService.delete(1);

    verify(arrivalRepository).deleteById(1);
}

}
