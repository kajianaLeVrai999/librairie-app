package com.example.demo.service;

import com.example.demo.dto.GenderDTO;
import com.example.demo.entity.Gender;
import com.example.demo.repository.GenderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenderService {

    private final GenderRepository genderRepository;


    public GenderService(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }



    // CREATE
    public GenderDTO create(GenderDTO dto) {

        Gender gender = new Gender();

        gender.setName(dto.getName());


        Gender saved = genderRepository.save(gender);

        return toDTO(saved);
    }



    // GET ALL
    public List<GenderDTO> getAll() {

        return genderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }



    // GET BY ID
    public GenderDTO getById(Long id) {

        Gender gender = genderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Gender not found")
                );

        return toDTO(gender);
    }



    // UPDATE
    public GenderDTO update(Long id, GenderDTO dto) {

        Gender existing = genderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Gender not found")
                );


        existing.setName(dto.getName());


        Gender updated = genderRepository.save(existing);

        return toDTO(updated);
    }



    // DELETE
    public void delete(Long id) {

        Gender gender = genderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Gender not found")
                );

        genderRepository.delete(gender);
    }



    // ENTITY -> DTO
    private GenderDTO toDTO(Gender gender) {

        return new GenderDTO(
                gender.getId(),
                gender.getName()
        );
    }
}