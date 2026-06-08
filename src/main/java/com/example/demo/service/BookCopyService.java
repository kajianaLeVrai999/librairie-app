package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

    private final BookCopyRepository copyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(
            BookCopyRepository copyRepository,
            BookRepository bookRepository) {

        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    public BookCopy create(Integer bookId, BookCopy copy) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        copy.setBook(book);

        return copyRepository.save(copy);
    }
}