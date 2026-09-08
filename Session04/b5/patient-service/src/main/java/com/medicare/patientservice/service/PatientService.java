package com.medicare.patientservice.service;

import com.medicare.patientservice.dto.PatientDto;
import com.medicare.patientservice.entity.Patient;
import com.medicare.patientservice.exception.PatientNotFoundException;
import com.medicare.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientDto createPatient(PatientDto patientDto) {
        Patient patient = Patient.builder()
                .name(patientDto.getName())
                .phone(patientDto.getPhone())
                .email(patientDto.getEmail())
                .address(patientDto.getAddress())
                .dateOfBirth(patientDto.getDateOfBirth())
                .build();
        Patient saved = patientRepository.save(patient);
        return toDto(saved);
    }

    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        return toDto(patient);
    }

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        patient.setName(patientDto.getName());
        patient.setPhone(patientDto.getPhone());
        patient.setEmail(patientDto.getEmail());
        patient.setAddress(patientDto.getAddress());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        Patient updated = patientRepository.save(patient);
        return toDto(updated);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }

    private PatientDto toDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .build();
    }
}