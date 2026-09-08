package com.medicare.appointment.dto;

import com.medicare.appointment.model.Appointment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentDate;
    private Appointment.Status status;
    private String reason;
    private String notes;

    public static AppointmentDto fromEntity(Appointment appointment) {
        if (appointment == null) return null;
        return AppointmentDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .build();
    }

    public Appointment toEntity() {
        Appointment appointment = new Appointment();
        appointment.setId(this.id);
        appointment.setPatientId(this.patientId);
        appointment.setDoctorId(this.doctorId);
        appointment.setAppointmentDate(this.appointmentDate);
        appointment.setStatus(this.status != null ? this.status : Appointment.Status.PENDING);
        appointment.setReason(this.reason);
        appointment.setNotes(this.notes);
        return appointment;
    }
}