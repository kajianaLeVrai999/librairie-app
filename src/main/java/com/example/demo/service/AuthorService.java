package com.example.demo.service;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author getById(Integer id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public Author update(Integer id, Author author) {
        Author existing = getById(id);
        existing.setFirstName(author.getFirstName());
        existing.setLastName(author.getLastName());
        existing.setBiography(author.getBiography());
        existing.setNationality(author.getNationality());
        return authorRepository.save(existing);
    }

    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}