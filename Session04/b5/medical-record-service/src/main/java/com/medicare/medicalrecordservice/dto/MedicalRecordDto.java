package com.medicare.medicalrecordservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDto {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}