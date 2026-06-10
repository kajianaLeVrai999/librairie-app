package com.example.demo.service;

import com.example.demo.entity.Gender;
import com.example.demo.repository.GenderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderService {

    private final GenderRepository genderRepository;

    public GenderService(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    public Gender create(Gender gender) {
        return genderRepository.save(gender);
    }

    public List<Gender> getAll() {
        return genderRepository.findAll();
    }

    public Gender getById(Long id) {
        return genderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gender not found"));
    }

    public Gender update(Long id, Gender gender) {
        Gender existing = getById(id);
        existing.setName(gender.getName());
        return genderRepository.save(existing);
    }

    public void delete(Long id) {
        genderRepository.deleteById(id);
    }
}