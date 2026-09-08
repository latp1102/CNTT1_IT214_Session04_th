package com.medicare.patient.dto;

import com.medicare.patient.model.Patient;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {

    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
    private String phone;
    private String address;
    private String insuranceId;

    public static PatientDto fromEntity(Patient patient) {
        if (patient == null) return null;
        return PatientDto.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .insuranceId(patient.getInsuranceId())
                .build();
    }

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id);
        patient.setFullName(this.fullName);
        patient.setDateOfBirth(this.dateOfBirth);
        patient.setGender(this.gender);
        patient.setPhone(this.phone);
        patient.setAddress(this.address);
        patient.setInsuranceId(this.insuranceId);
        return patient;
    }
}