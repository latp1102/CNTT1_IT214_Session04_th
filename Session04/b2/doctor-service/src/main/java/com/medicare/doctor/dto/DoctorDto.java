package com.medicare.doctor.dto;

import com.medicare.doctor.model.Doctor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    private Long id;
    private String fullName;
    private String specialization;
    private String phone;
    private String email;
    private String licenseNumber;
    private String department;

    public static DoctorDto fromEntity(Doctor doctor) {
        if (doctor == null) return null;
        return DoctorDto.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .licenseNumber(doctor.getLicenseNumber())
                .department(doctor.getDepartment())
                .build();
    }

    public Doctor toEntity() {
        Doctor doctor = new Doctor();
        doctor.setId(this.id);
        doctor.setFullName(this.fullName);
        doctor.setSpecialization(this.specialization);
        doctor.setPhone(this.phone);
        doctor.setEmail(this.email);
        doctor.setLicenseNumber(this.licenseNumber);
        doctor.setDepartment(this.department);
        return doctor;
    }
}