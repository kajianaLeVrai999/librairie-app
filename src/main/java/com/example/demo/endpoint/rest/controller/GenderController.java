package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.GenderDTO;
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
    public ResponseEntity<List<GenderDTO>> getAllGenders() {

        return ResponseEntity.ok(
                genderService.getAll()
        );
    }



    // GET /genders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getGenderById(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    genderService.getById(id)
            );


        } catch(RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    // POST /genders
    @PostMapping
    public ResponseEntity<?> createGender(
            @RequestBody GenderDTO dto) {


        try {

            GenderDTO created =
                    genderService.create(dto);


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);


        } catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }



    // PUT /genders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGender(
            @PathVariable Long id,
            @RequestBody GenderDTO dto) {


        try {

            GenderDTO updated =
                    genderService.update(id, dto);


            return ResponseEntity.ok(updated);


        } catch(RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());


        } catch(Exception e){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }



    // DELETE /genders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGender(
            @PathVariable Long id) {


        try {

            genderService.delete(id);

            return ResponseEntity
                    .noContent()
                    .build();


        } catch(RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}