package com.example.demo.conf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.BookCopyDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.GenderSalesDTO;
import com.example.demo.dto.SaleDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SaleRepository;
import com.example.demo.service.SaleService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

  @Mock private SaleRepository saleRepository;

  @Mock private BookCopyRepository bookCopyRepository;

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private SaleService saleService;

  private Customer customer;
  private BookCopy bookCopy;
  private Sale sale;
  private SaleDTO saleDTO;

  @BeforeEach
  void setUp() {
    // Créer un client
    customer = new Customer();
    customer.setId(1);
    customer.setFirstName("Jean");
    customer.setLastName("Dupont");
    customer.setEmail("jean.dupont@email.com");
    customer.setPhone("0612345678");
    customer.setAddress("12 rue de Paris, 75001 Paris");

    // Créer une copie de livre
    bookCopy = new BookCopy();
    bookCopy.setId(1);
    bookCopy.setStatus(CopyStatus.AVAILABLE);
    bookCopy.setFormat(BookFormat.PAPERBACK);

    // Créer une vente
    sale = new Sale();
    sale.setId("sale-001");
    sale.setSaleDate(LocalDate.now());
    sale.setQuantity(2);
    sale.setTotalAmount(39.98);
    sale.setCustomer(customer);
    sale.setBookCopy(bookCopy);

    // Créer un DTO de vente
    saleDTO = new SaleDTO();
    saleDTO.setQuantity(2);
    saleDTO.setTotalAmount(39.98);

    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setId(1);
    saleDTO.setCustomer(customerDTO);

    BookCopyDTO bookCopyDTO = new BookCopyDTO();
    bookCopyDTO.setId(1);
    saleDTO.setBookCopy(bookCopyDTO);
  }

  @Test
  void shouldCreateSale() {
    when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
    when(bookCopyRepository.findById(1)).thenReturn(Optional.of(bookCopy));
    when(saleRepository.save(any(Sale.class))).thenReturn(sale);

    SaleDTO result = saleService.create(saleDTO);

    assertNotNull(result);
    assertEquals(2, result.getQuantity());
    assertEquals(39.98, result.getTotalAmount());
    verify(saleRepository).save(any(Sale.class));
    verify(bookCopyRepository).save(bookCopy);
  }

  @Test
  void shouldThrowWhenCustomerNotFound() {
    when(customerRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> saleService.create(saleDTO));
    verify(saleRepository, never()).save(any(Sale.class));
  }

  @Test
  void shouldThrowWhenBookCopyNotFound() {
    when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
    when(bookCopyRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> saleService.create(saleDTO));
    verify(saleRepository, never()).save(any(Sale.class));
  }

  @Test
  void shouldGetAllSales() {
    List<Sale> sales = List.of(sale, sale);
    when(saleRepository.findAll()).thenReturn(sales);

    List<SaleDTO> result = saleService.getAll();

    assertEquals(2, result.size());
    verify(saleRepository).findAll();
  }

  @Test
  void shouldGetSaleById() {
    when(saleRepository.findById("sale-001")).thenReturn(Optional.of(sale));

    SaleDTO result = saleService.getById("sale-001");

    assertNotNull(result);
    assertEquals("sale-001", result.getId());
    assertEquals(39.98, result.getTotalAmount());
  }

  @Test
  void shouldThrowWhenSaleNotFound() {
    when(saleRepository.findById("invalid-id")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> saleService.getById("invalid-id"));
  }

  @Test
  void shouldGetSalesByDate() {
    LocalDate today = LocalDate.now();
    List<Sale> sales = List.of(sale);
    when(saleRepository.findAll()).thenReturn(sales);

    List<SaleDTO> result = saleService.getSalesByDate(today);

    assertEquals(1, result.size());
  }

  @Test
  void shouldGetTotalRevenueByDate() {
    LocalDate today = LocalDate.now();
    List<Sale> sales = List.of(sale);
    when(saleRepository.findAll()).thenReturn(sales);

    double revenue = saleService.getTotalRevenueByDate(today);

    assertEquals(39.98, revenue);
  }

  @Test
  void shouldGetSalesByGender() {
    List<Object[]> results =
        Arrays.asList(
            new Object[] {1L, "Classique", 2L, 50000.0},
            new Object[] {2L, "Programmation", 1L, 70000.0});
    when(saleRepository.findSalesByGender()).thenReturn(results);

    List<GenderSalesDTO> result = saleService.getSalesByGender();

    assertEquals(2, result.size());
    assertEquals("Classique", result.get(0).getGenderName());
    assertEquals(50000.0, result.get(0).getTotalRevenue());
    assertEquals(2, result.get(0).getTotalSales());
  }

  @Test
  void shouldReturnEmptyListWhenNoSales() {
    when(saleRepository.findSalesByGender()).thenReturn(List.of());

    List<GenderSalesDTO> result = saleService.getSalesByGender();

    assertTrue(result.isEmpty());
  }
}
