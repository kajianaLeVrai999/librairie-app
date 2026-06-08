package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book create(Book book) {
        return repository.save(book);
    }

    public List<Book> getAll() {
        return repository.findAll();
    }

    public Book getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book update(Integer id, Book book) {

        Book existing = getById(id);

        existing.setTitle(book.getTitle());
        existing.setDescription(book.getDescription());
        existing.setPrice(book.getPrice());
        existing.setPublicationDate(book.getPublicationDate());
        existing.setIsbn(book.getIsbn());
        existing.setCategory(book.getCategory());
        existing.setAuthors(book.getAuthors());
        existing.setGenders(book.getGenders());

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Book> search(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }
}