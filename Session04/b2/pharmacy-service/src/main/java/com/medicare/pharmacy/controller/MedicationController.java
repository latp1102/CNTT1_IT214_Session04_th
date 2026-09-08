package com.medicare.pharmacy.controller;

import com.medicare.pharmacy.dto.MedicationDto;
import com.medicare.pharmacy.model.Medication;
import com.medicare.pharmacy.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<MedicationDto> create(@Valid @RequestBody MedicationDto dto) {
        MedicationDto created = medicationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MedicationDto>> getAll() {
        List<MedicationDto> medications = medicationService.getAll();
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationDto> getById(@PathVariable Long id) {
        MedicationDto medication = medicationService.getById(id);
        return ResponseEntity.ok(medication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationDto> update(@PathVariable Long id, @Valid @RequestBody MedicationDto dto) {
        MedicationDto updated = medicationService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicationDto>> searchByName(@RequestParam String name) {
        List<MedicationDto> medications = medicationService.searchByName(name);
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/generic-name")
    public ResponseEntity<List<MedicationDto>> searchByGenericName(@RequestParam String genericName) {
        List<MedicationDto> medications = medicationService.searchByGenericName(genericName);
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/active")
    public ResponseEntity<List<MedicationDto>> getActiveMedications() {
        List<MedicationDto> medications = medicationService.getActiveMedications();
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/dosage-form/{dosageForm}")
    public ResponseEntity<List<MedicationDto>> getByDosageForm(@PathVariable Medication.DosageForm dosageForm) {
        List<MedicationDto> medications = medicationService.getByDosageForm(dosageForm);
        return ResponseEntity.ok(medications);
    }
}