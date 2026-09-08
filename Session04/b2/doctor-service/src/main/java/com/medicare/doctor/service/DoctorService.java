package com.medicare.doctor.service;

import com.medicare.doctor.model.Doctor;
import com.medicare.doctor.repository.DoctorRepository;
import com.medicare.doctor.dto.DoctorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorDto create(DoctorDto dto) {
        if (doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("Số phép hành nghề đã tồn tại: " + dto.getLicenseNumber());
        }
        Doctor doctor = dto.toEntity();
        Doctor saved = doctorRepository.save(doctor);
        return DoctorDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> getAll() {
        return doctorRepository.findAll().stream()
                .map(DoctorDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDto getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + id));
        return DoctorDto.fromEntity(doctor);
    }

    public DoctorDto update(Long id, DoctorDto dto) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + id));

        if (!existing.getLicenseNumber().equals(dto.getLicenseNumber()) &&
            doctorRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("Số phép hành nghề đã tồn tại: " + dto.getLicenseNumber());
        }

        existing.setFullName(dto.getFullName());
        existing.setSpecialization(dto.getSpecialization());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setDepartment(dto.getDepartment());

        Doctor updated = doctorRepository.save(existing);
        return DoctorDto.fromEntity(updated);
    }

    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bác sĩ với ID: " + id);
        }
        doctorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> searchBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization).stream()
                .map(DoctorDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> getByDepartment(String department) {
        return doctorRepository.findByDepartment(department).stream()
                .map(DoctorDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> searchByName(String name) {
        return doctorRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(DoctorDto::fromEntity)
                .collect(Collectors.toList());
    }
}