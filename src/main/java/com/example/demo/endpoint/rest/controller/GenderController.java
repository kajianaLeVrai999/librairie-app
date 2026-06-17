package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Gender;
import com.example.demo.repository.GenderRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genders")
public class GenderController {

  private final GenderRepository genderRepository;

  public GenderController(GenderRepository genderRepository) {
    this.genderRepository = genderRepository;
  }

  @GetMapping
  public String listGenders(Model model) {
    List<Gender> genders = genderRepository.findAll();
    model.addAttribute("genders", genders);
    return "gender-list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("gender", new Gender());
    return "gender-form";
  }

  @PostMapping("/save")
  public String saveGender(@ModelAttribute Gender gender) {
    genderRepository.save(gender);
    return "redirect:/genders";
  }

  @GetMapping("/delete/{id}")
  public String deleteGender(@PathVariable Long id) {
    genderRepository.deleteById(id);
    return "redirect:/genders";
  }
}
