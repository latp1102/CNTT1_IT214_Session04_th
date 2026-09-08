package com.medicare.medicalrecordservice.service;

import com.medicare.medicalrecordservice.client.AppointmentClientDto;
import com.medicare.medicalrecordservice.dto.MedicalRecordDto;
import com.medicare.medicalrecordservice.entity.MedicalRecord;
import com.medicare.medicalrecordservice.exception.MedicalRecordNotFoundException;
import com.medicare.medicalrecordservice.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final RestTemplate restTemplate;

    public MedicalRecordDto createMedicalRecord(MedicalRecordDto medicalRecordDto) {
        // Gọi Appointment Service để kiểm tra appointmentId
        String appointmentUrl = "http://appointment-service/api/appointments/" + medicalRecordDto.getAppointmentId();
        AppointmentClientDto appointment = null;
        try {
            ResponseEntity<AppointmentClientDto> response = restTemplate.getForEntity(appointmentUrl, AppointmentClientDto.class);
            appointment = response.getBody();
            
            if (appointment == null) {
                throw new RuntimeException("Appointment not found with id: " + medicalRecordDto.getAppointmentId());
            }
            
            // Kiểm tra appointment có COMPLETED không
            if (!"COMPLETED".equals(appointment.getStatus())) {
                throw new RuntimeException("Appointment must be COMPLETED to create medical record. Current status: " + appointment.getStatus());
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Appointment not found") || e.getMessage().contains("COMPLETED")) {
                throw e;
            }
            throw new RuntimeException("Failed to verify appointment: " + e.getMessage());
        }

        // Verify patientId and doctorId match appointment
        if (!appointment.getPatientId().equals(medicalRecordDto.getPatientId())) {
            throw new RuntimeException("Patient ID does not match appointment");
        }
        if (!appointment.getDoctorId().equals(medicalRecordDto.getDoctorId())) {
            throw new RuntimeException("Doctor ID does not match appointment");
        }

        // Tạo hồ sơ bệnh án
        MedicalRecord record = MedicalRecord.builder()
                .appointmentId(medicalRecordDto.getAppointmentId())
                .patientId(medicalRecordDto.getPatientId())
                .doctorId(medicalRecordDto.getDoctorId())
                .diagnosis(medicalRecordDto.getDiagnosis())
                .treatment(medicalRecordDto.getTreatment())
                .prescription(medicalRecordDto.getPrescription())
                .notes(medicalRecordDto.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);
        return toDto(saved);
    }

    public MedicalRecordDto getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with id: " + id));
        return toDto(record);
    }

    public List<MedicalRecordDto> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordDto> getMedicalRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordDto> getMedicalRecordsByDoctorId(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MedicalRecordDto getMedicalRecordByAppointmentId(Long appointmentId) {
        return medicalRecordRepository.findByAppointmentId(appointmentId).stream()
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found for appointment id: " + appointmentId));
    }

    public MedicalRecordDto updateMedicalRecord(Long id, MedicalRecordDto medicalRecordDto) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with id: " + id));

        record.setDiagnosis(medicalRecordDto.getDiagnosis());
        record.setTreatment(medicalRecordDto.getTreatment());
        record.setPrescription(medicalRecordDto.getPrescription());
        record.setNotes(medicalRecordDto.getNotes());
        record.setUpdatedAt(LocalDateTime.now());

        MedicalRecord updated = medicalRecordRepository.save(record);
        return toDto(updated);
    }

    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new MedicalRecordNotFoundException("Medical record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    private MedicalRecordDto toDto(MedicalRecord record) {
        return MedicalRecordDto.builder()
                .id(record.getId())
                .appointmentId(record.getAppointmentId())
                .patientId(record.getPatientId())
                .doctorId(record.getDoctorId())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .prescription(record.getPrescription())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}