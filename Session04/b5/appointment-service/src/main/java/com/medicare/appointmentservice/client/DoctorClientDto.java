package com.medicare.appointmentservice.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorClientDto {
    private Long id;
    private String name;
    private String specialty;
    private String phone;
    private String email;
    private String licenseNumber;
}