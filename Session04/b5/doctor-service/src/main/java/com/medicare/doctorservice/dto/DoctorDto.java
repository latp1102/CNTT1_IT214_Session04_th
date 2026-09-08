package com.medicare.doctorservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {
    private Long id;
    private String name;
    private String specialty;
    private String phone;
    private String email;
    private String licenseNumber;
}