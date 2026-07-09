package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.service.CategoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  // GET /categories
  @GetMapping
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {

    return ResponseEntity.ok(categoryService.getAll());
  }

  // GET /categories/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getCategoryById(@PathVariable Long id) {

    try {

      return ResponseEntity.ok(categoryService.getById(id));

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /categories
  @PostMapping
  public ResponseEntity<?> createCategory(@RequestBody CategoryDTO dto) {

    try {

      CategoryDTO created = categoryService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(created);

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // PUT /categories/{id}
  @PutMapping("/{id}")
  public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO dto) {

    try {

      CategoryDTO updated = categoryService.update(id, dto);

      return ResponseEntity.ok(updated);

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // DELETE /categories/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long id) {

    try {

      categoryService.delete(id);

      return ResponseEntity.noContent().build();

    } catch (RuntimeException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
