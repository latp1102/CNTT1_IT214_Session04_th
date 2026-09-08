package com.medicare.appointmentservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDetailDto {
    private Long appointmentId;
    private LocalDateTime appointmentDate;
    private String status;

    // Thông tin bệnh nhân (lấy từ Patient Service)
    private Long patientId;
    private String patientName;
    private String patientPhone;

    // Thông tin bác sĩ (lấy từ Doctor Service)
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialty;
}