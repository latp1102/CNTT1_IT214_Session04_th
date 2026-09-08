package com.medicare.patient.service;

import com.medicare.patient.model.Patient;
import com.medicare.patient.repository.PatientRepository;
import com.medicare.patient.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientDto create(PatientDto dto) {
        if (patientRepository.existsByInsuranceId(dto.getInsuranceId())) {
            throw new RuntimeException("Số BHYT đã tồn tại: " + dto.getInsuranceId());
        }
        Patient patient = dto.toEntity();
        Patient saved = patientRepository.save(patient);
        return PatientDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<PatientDto> getAll() {
        return patientRepository.findAll().stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDto getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID: " + id));
        return PatientDto.fromEntity(patient);
    }

    public PatientDto update(Long id, PatientDto dto) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID: " + id));

        if (!existing.getInsuranceId().equals(dto.getInsuranceId()) &&
            patientRepository.existsByInsuranceId(dto.getInsuranceId())) {
            throw new RuntimeException("Số BHYT đã tồn tại: " + dto.getInsuranceId());
        }

        existing.setFullName(dto.getFullName());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setGender(dto.getGender());
        existing.setPhone(dto.getPhone());
        existing.setAddress(dto.getAddress());
        existing.setInsuranceId(dto.getInsuranceId());

        Patient updated = patientRepository.save(existing);
        return PatientDto.fromEntity(updated);
    }

    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bệnh nhân với ID: " + id);
        }
        patientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PatientDto> searchByName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PatientDto> getByGender(Patient.Gender gender) {
        return patientRepository.findByGender(gender).stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());
    }
}