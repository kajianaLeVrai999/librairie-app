package com.example.demo.conf.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class RepositoryTest {

  @Autowired private BookRepository bookRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Test
  void shouldSaveAndFindBook() {
    Category category = new Category();
    category.setName("Test Category");
    category.setDescription("Test Description");
    Category savedCategory = categoryRepository.save(category);

    Book book = new Book();
    book.setTitle("Test Book");
    book.setDescription("Test Description");
    book.setPrice(19.99);
    book.setPublicationDate(LocalDate.now());
    book.setIsbn("1234567890");
    book.setCategory(savedCategory);

    Book saved = bookRepository.save(book);

    assertNotNull(saved.getId());
    assertEquals("Test Book", saved.getTitle());

    Book found = bookRepository.findById(saved.getId()).orElse(null);
    assertNotNull(found);
    assertEquals("Test Book", found.getTitle());
  }

  @Test
  void shouldSearchBooksByTitle() {
    Book book1 = new Book();
    book1.setTitle("Java Programming");
    book1.setDescription("Learn Java");
    book1.setPrice(29.99);
    book1.setPublicationDate(LocalDate.now());
    book1.setIsbn("1234567890");
    bookRepository.save(book1);

    Book book2 = new Book();
    book2.setTitle("Python Programming");
    book2.setDescription("Learn Python");
    book2.setPrice(24.99);
    book2.setPublicationDate(LocalDate.now());
    book2.setIsbn("0987654321");
    bookRepository.save(book2);

    List<Book> results = bookRepository.findByTitleContainingIgnoreCase("java");

    assertEquals(1, results.size());
    assertEquals("Java Programming", results.get(0).getTitle());
  }
}
