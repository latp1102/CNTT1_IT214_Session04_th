package com.medicare.patientservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
}