package com.example.demo.endpoint.rest.controller;

import com.example.demo.entity.Gender;
import com.example.demo.service.GenderService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genders")
public class GenderController {

  private final GenderService genderService;

  public GenderController(GenderService genderService) {
    this.genderService = genderService;
  }

  // GET /genders
  @GetMapping
  public ResponseEntity<List<Gender>> getAllGenders() {
    return ResponseEntity.ok(genderService.getAll());
  }

  // GET /genders/{id}
  @GetMapping("/{id}")
  public ResponseEntity<?> getGenderById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(genderService.getById(id));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  // POST /genders
  @PostMapping
  public ResponseEntity<?> createGender(@RequestBody Gender gender) {
    try {
      Gender created = genderService.create(gender);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // PUT /genders/{id}
  @PutMapping("/{id}")
  public ResponseEntity<?> updateGender(@PathVariable Long id, @RequestBody Gender gender) {

    try {
      Gender updated = genderService.update(id, gender);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // DELETE /genders/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteGender(@PathVariable Long id) {
    try {
      genderService.getById(id);
      genderService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
