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

  public AuthorDTO create(AuthorDTO dto) {
    Author author = new Author();
    author.setFirstName(dto.getFirstName());
    author.setLastName(dto.getLastName());
    author.setBiography(dto.getBiography());
    author.setNationality(dto.getNationality());
    Author saved = authorRepository.save(author);
    return toDTO(saved);
  }

  public List<AuthorDTO> getAll() {
    return authorRepository.findAll().stream().map(this::toDTO).toList();
  }

  public AuthorDTO getById(Integer id) {
    Author author =
        authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));
    return toDTO(author);
  }

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

  public void delete(Integer id) {
    if (!authorRepository.existsById(id)) {
      throw new RuntimeException("Author not found");
    }
    authorRepository.deleteById(id);
  }

  private AuthorDTO toDTO(Author author) {
    AuthorDTO dto = new AuthorDTO();
    dto.setId(author.getId());
    dto.setFirstName(author.getFirstName());
    dto.setLastName(author.getLastName());
    dto.setBiography(author.getBiography());
    dto.setNationality(author.getNationality());
    return dto;
  }
}
