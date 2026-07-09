package com.example.demo.service;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  // Créer un auteur
  public AuthorDTO create(AuthorDTO dto) {

    Author author = new Author();

    author.setFirstName(dto.getFirstName());
    author.setLastName(dto.getLastName());
    author.setBiography(dto.getBiography());
    author.setNationality(dto.getNationality());

    Author saved = authorRepository.save(author);

    return toDTO(saved);
  }

  // Récupérer tous les auteurs
  public List<AuthorDTO> getAll() {

    return authorRepository.findAll().stream().map(this::toDTO).toList();
  }

  // Récupérer un auteur par ID
  public AuthorDTO getById(Integer id) {

    Author author =
        authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));

    return toDTO(author);
  }

  // Modifier un auteur
  public AuthorDTO update(Integer id, AuthorDTO dto) {

    Author existing =
        authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));

    existing.setFirstName(dto.getFirstName());
    existing.setLastName(dto.getLastName());
    existing.setBiography(dto.getBiography());
    existing.setNationality(dto.getNationality());

    Author updated = authorRepository.save(existing);

    return toDTO(updated);
  }

  // Supprimer un auteur
  public void delete(Integer id) {

    Author author =
        authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));

    authorRepository.delete(author);
  }

  // Conversion Entity -> DTO
  private AuthorDTO toDTO(Author author) {

    return new AuthorDTO(
        author.getId(),
        author.getFirstName(),
        author.getLastName(),
        author.getBiography(),
        author.getNationality());
  }
}
