package com.medicare.doctorservice.service;

import com.medicare.doctorservice.dto.DoctorDto;
import com.medicare.doctorservice.entity.Doctor;
import com.medicare.doctorservice.exception.DoctorNotFoundException;
import com.medicare.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorDto createDoctor(DoctorDto doctorDto) {
        Doctor doctor = Doctor.builder()
                .name(doctorDto.getName())
                .specialty(doctorDto.getSpecialty())
                .phone(doctorDto.getPhone())
                .email(doctorDto.getEmail())
                .licenseNumber(doctorDto.getLicenseNumber())
                .build();
        Doctor saved = doctorRepository.save(doctor);
        return toDto(saved);
    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        return toDto(doctor);
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        doctor.setName(doctorDto.getName());
        doctor.setSpecialty(doctorDto.getSpecialty());
        doctor.setPhone(doctorDto.getPhone());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setLicenseNumber(doctorDto.getLicenseNumber());
        Doctor updated = doctorRepository.save(doctor);
        return toDto(updated);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return doctorRepository.existsById(id);
    }

    private DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .licenseNumber(doctor.getLicenseNumber())
                .build();
    }
}