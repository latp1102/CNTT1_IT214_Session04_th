package com.medicare.medicalrecordservice.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentClientDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentDate;
    private String status;
    private String notes;
}