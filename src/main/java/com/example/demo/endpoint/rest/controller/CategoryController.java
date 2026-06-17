package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryRepository categoryRepository;

  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @GetMapping
  public String listCategories(Model model) {
    List<Category> categories = categoryRepository.findAll();
    model.addAttribute("categories", categories);
    return "category-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("category", new Category());
    return "category-form";
  }

  @PostMapping("/save")
  public String saveCategory(@ModelAttribute Category category) {
    categoryRepository.save(category);
    return "redirect:/categories";
  }

  @GetMapping("/{id}")
  public String viewCategory(@PathVariable Long id, Model model) {
    Category category = categoryRepository.findById(id).orElse(null);
    model.addAttribute("category", category);
    return "category-detail";
  }

  @GetMapping("/delete/{id}")
  public String deleteCategory(@PathVariable Long id) {
    categoryRepository.deleteById(id);
    return "redirect:/categories";
  }
}
