package com.medicare.doctor.controller;

import com.medicare.doctor.dto.DoctorDto;
import com.medicare.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorDto> create(@Valid @RequestBody DoctorDto dto) {
        DoctorDto created = doctorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAll() {
        List<DoctorDto> doctors = doctorService.getAll();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getById(@PathVariable Long id) {
        DoctorDto doctor = doctorService.getById(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> update(@PathVariable Long id, @Valid @RequestBody DoctorDto dto) {
        DoctorDto updated = doctorService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialization")
    public ResponseEntity<List<DoctorDto>> searchBySpecialization(@RequestParam String specialization) {
        List<DoctorDto> doctors = doctorService.searchBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<DoctorDto>> getByDepartment(@PathVariable String department) {
        List<DoctorDto> doctors = doctorService.getByDepartment(department);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDto>> searchByName(@RequestParam String name) {
        List<DoctorDto> doctors = doctorService.searchByName(name);
        return ResponseEntity.ok(doctors);
    }
}