package com.medicare.patient.controller;

import com.medicare.patient.dto.PatientDto;
import com.medicare.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDto> create(@Valid @RequestBody PatientDto dto) {
        PatientDto created = patientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAll() {
        List<PatientDto> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getById(@PathVariable Long id) {
        PatientDto patient = patientService.getById(id);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> update(@PathVariable Long id, @Valid @RequestBody PatientDto dto) {
        PatientDto updated = patientService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchByName(@RequestParam String name) {
        List<PatientDto> patients = patientService.searchByName(name);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<PatientDto>> getByGender(@PathVariable Patient.Gender gender) {
        List<PatientDto> patients = patientService.getByGender(gender);
        return ResponseEntity.ok(patients);
    }
}