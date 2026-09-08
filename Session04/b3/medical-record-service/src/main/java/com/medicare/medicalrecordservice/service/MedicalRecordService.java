package com.medicare.medicalrecordservice.service;

import com.medicare.medicalrecordservice.entity.MedicalRecord;
import com.medicare.medicalrecordservice.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalRecord not found with id: " + id));
        
        record.setPatientId(recordDetails.getPatientId());
        record.setDoctorId(recordDetails.getDoctorId());
        record.setAppointmentId(recordDetails.getAppointmentId());
        record.setDiagnosis(recordDetails.getDiagnosis());
        record.setTreatment(recordDetails.getTreatment());
        record.setPrescription(recordDetails.getPrescription());
        record.setNotes(recordDetails.getNotes());
        record.setRecordDate(recordDetails.getRecordDate());
        
        return medicalRecordRepository.save(record);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}