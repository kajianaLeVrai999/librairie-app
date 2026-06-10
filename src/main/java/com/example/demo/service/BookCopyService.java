package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookCopy;
import com.example.demo.entity.CopyStatus;
import com.example.demo.repository.BookCopyRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCopyService {

    private final BookCopyRepository copyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(BookCopyRepository copyRepository, BookRepository bookRepository) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    // Créer un exemplaire pour un livre
    public BookCopy create(Integer bookId, BookCopy copy) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        copy.setBook(book);
        return copyRepository.save(copy);
    }

    // Créer plusieurs exemplaires (utile pour les arrivages)
    public List<BookCopy> createMultiple(Integer bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        List<BookCopy> copies = new java.util.ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setStatus(CopyStatus.AVAILABLE);
            copies.add(copyRepository.save(copy));
        }
        return copies;
    }

    // Récupérer tous les exemplaires
    public List<BookCopy> getAll() {
        return copyRepository.findAll();
    }

    // Récupérer un exemplaire par ID
    public BookCopy getById(Integer id) {
        return copyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookCopy not found"));
    }

    // Récupérer les exemplaires d'un livre
    public List<BookCopy> getByBookId(Integer bookId) {
        return copyRepository.findAll().stream()
                .filter(copy -> copy.getBook() != null && copy.getBook().getId().equals(bookId))
                .toList();
    }

    // Récupérer les exemplaires disponibles
    public List<BookCopy> getAvailableCopies() {
        return copyRepository.findAll().stream()
                .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                .toList();
    }

    // Mettre à jour le statut d'un exemplaire
    public BookCopy updateStatus(Integer id, CopyStatus status) {
        BookCopy copy = getById(id);
        copy.setStatus(status);
        return copyRepository.save(copy);
    }

    // Supprimer un exemplaire
    public void delete(Integer id) {
        copyRepository.deleteById(id);
    }

    // Compter les exemplaires disponibles d'un livre
    public long countAvailableByBookId(Integer bookId) {
        return getByBookId(bookId).stream()
                .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                .count();
    }
}