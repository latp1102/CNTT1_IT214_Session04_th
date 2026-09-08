package com.medicare.pharmacy.service;

import com.medicare.pharmacy.model.Medication;
import com.medicare.pharmacy.repository.MedicationRepository;
import com.medicare.pharmacy.dto.MedicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationDto create(MedicationDto dto) {
        if (medicationRepository.findByNameAndStrengthAndDosageForm(
                dto.getName(), dto.getStrength(), dto.getDosageForm()).isPresent()) {
            throw new RuntimeException("Thuốc đã tồn tại: " + dto.getName() + " " + dto.getStrength() + " " + dto.getDosageForm());
        }
        Medication medication = dto.toEntity();
        Medication saved = medicationRepository.save(medication);
        return MedicationDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getAll() {
        return medicationRepository.findAll().stream()
                .map(MedicationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicationDto getById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));
        return MedicationDto.fromEntity(medication);
    }

    public MedicationDto update(Long id, MedicationDto dto) {
        Medication existing = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));

        existing.setName(dto.getName());
        existing.setGenericName(dto.getGenericName());
        existing.setStrength(dto.getStrength());
        existing.setDosageForm(dto.getDosageForm());
        existing.setManufacturer(dto.getManufacturer());
        existing.setUnit(dto.getUnit());
        existing.setPrice(dto.getPrice());
        existing.setIsPrescriptionRequired(dto.getIsPrescriptionRequired());
        existing.setIsActive(dto.getIsActive());

        Medication updated = medicationRepository.save(existing);
        return MedicationDto.fromEntity(updated);
    }

    public void delete(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy thuốc với ID: " + id);
        }
        medicationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> searchByName(String name) {
        return medicationRepository.findByNameContainingIgnoreCase(name).stream()
                .map(MedicationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> searchByGenericName(String genericName) {
        return medicationRepository.findByGenericNameContainingIgnoreCase(genericName).stream()
                .map(MedicationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getActiveMedications() {
        return medicationRepository.findByIsActiveTrue().stream()
                .map(MedicationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getByDosageForm(Medication.DosageForm dosageForm) {
        return medicationRepository.findByDosageForm(dosageForm).stream()
                .map(MedicationDto::fromEntity)
                .collect(Collectors.toList());
    }
}