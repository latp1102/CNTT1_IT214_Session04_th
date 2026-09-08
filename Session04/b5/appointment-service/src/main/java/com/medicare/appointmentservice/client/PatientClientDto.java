package com.medicare.appointmentservice.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientClientDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
}