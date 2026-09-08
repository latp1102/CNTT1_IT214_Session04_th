package com.medicare.medicalrecord.controller;

import com.medicare.medicalrecord.dto.MedicalRecordDto;
import com.medicare.medicalrecord.model.MedicalRecord;
import com.medicare.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordDto> create(@Valid @RequestBody MedicalRecordDto dto) {
        MedicalRecordDto created = medicalRecordService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordDto>> getAll() {
        List<MedicalRecordDto> records = medicalRecordService.getAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getById(@PathVariable Long id) {
        MedicalRecordDto record = medicalRecordService.getById(id);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> update(@PathVariable Long id, @Valid @RequestBody MedicalRecordDto dto) {
        MedicalRecordDto updated = medicalRecordService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDto>> getByPatientId(@PathVariable Long patientId) {
        List<MedicalRecordDto> records = medicalRecordService.getByPatientId(patientId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordDto>> getByDoctorId(@PathVariable Long doctorId) {
        List<MedicalRecordDto> records = medicalRecordService.getByDoctorId(doctorId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordDto> getByAppointmentId(@PathVariable Long appointmentId) {
        MedicalRecordDto record = medicalRecordService.getByAppointmentId(appointmentId);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MedicalRecordDto>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<MedicalRecordDto> records = medicalRecordService.getByDateRange(start, end);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/icd10/{icd10Code}")
    public ResponseEntity<List<MedicalRecordDto>> getByIcd10Code(@PathVariable String icd10Code) {
        List<MedicalRecordDto> records = medicalRecordService.getByIcd10Code(icd10Code);
        return ResponseEntity.ok(records);
    }
}