package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }



    // CREATE
    public CategoryDTO create(CategoryDTO dto) {

        Category category = new Category();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());


        Category saved = categoryRepository.save(category);

        return toDTO(saved);
    }



    // GET ALL
    public List<CategoryDTO> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }



    // GET BY ID
    public CategoryDTO getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        return toDTO(category);
    }



    // UPDATE
    public CategoryDTO update(Long id, CategoryDTO dto) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );


        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());


        Category updated = categoryRepository.save(existing);

        return toDTO(updated);
    }



    // DELETE
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        categoryRepository.delete(category);
    }



    // ENTITY -> DTO
    private CategoryDTO toDTO(Category category) {

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}