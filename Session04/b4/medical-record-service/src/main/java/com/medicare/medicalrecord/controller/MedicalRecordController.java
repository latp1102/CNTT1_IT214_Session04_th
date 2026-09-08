package com.medicare.medicalrecord.controller;

import com.medicare.medicalrecord.entity.MedicalRecord;
import com.medicare.medicalrecord.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord recordDetails) {
        return medicalRecordRepository.findById(id)
                .map(record -> {
                    record.setPatientId(recordDetails.getPatientId());
                    record.setDoctorId(recordDetails.getDoctorId());
                    record.setDiagnosis(recordDetails.getDiagnosis());
                    record.setTreatment(recordDetails.getTreatment());
                    record.setPrescription(recordDetails.getPrescription());
                    record.setRecordDate(recordDetails.getRecordDate());
                    return ResponseEntity.ok(medicalRecordRepository.save(record));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(record -> {
                    medicalRecordRepository.delete(record);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}