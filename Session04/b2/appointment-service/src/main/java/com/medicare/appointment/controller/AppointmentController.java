package com.medicare.appointment.controller;

import com.medicare.appointment.dto.AppointmentDto;
import com.medicare.appointment.model.Appointment;
import com.medicare.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentDto dto) {
        AppointmentDto created = appointmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAll() {
        List<AppointmentDto> appointments = appointmentService.getAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getById(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.getById(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> update(@PathVariable Long id, @Valid @RequestBody AppointmentDto dto) {
        AppointmentDto updated = appointmentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable Long id, @RequestParam Appointment.Status status) {
        AppointmentDto updated = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getByPatientId(@PathVariable Long patientId) {
        List<AppointmentDto> appointments = appointmentService.getByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getByDoctorId(@PathVariable Long doctorId) {
        List<AppointmentDto> appointments = appointmentService.getByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDto>> getByStatus(@PathVariable Appointment.Status status) {
        List<AppointmentDto> appointments = appointmentService.getByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentDto>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<AppointmentDto> appointments = appointmentService.getByDateRange(start, end);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}/date-range")
    public ResponseEntity<List<AppointmentDto>> getByDoctorAndDateRange(
            @PathVariable Long doctorId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<AppointmentDto> appointments = appointmentService.getByDoctorAndDateRange(doctorId, start, end);
        return ResponseEntity.ok(appointments);
    }
}