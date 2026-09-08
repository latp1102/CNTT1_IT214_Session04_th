package com.medicare.appointment.service;

import com.medicare.appointment.model.Appointment;
import com.medicare.appointment.repository.AppointmentRepository;
import com.medicare.appointment.dto.AppointmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentDto create(AppointmentDto dto) {
        if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                dto.getDoctorId(), dto.getAppointmentDate(), Appointment.Status.CANCELLED)) {
            throw new RuntimeException("Bác sĩ đã có lịch khám vào thời gian này");
        }
        Appointment appointment = dto.toEntity();
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getAll() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDto getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch khám với ID: " + id));
        return AppointmentDto.fromEntity(appointment);
    }

    public AppointmentDto update(Long id, AppointmentDto dto) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch khám với ID: " + id));

        if (!existing.getDoctorId().equals(dto.getDoctorId()) ||
            !existing.getAppointmentDate().equals(dto.getAppointmentDate())) {
            if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                    dto.getDoctorId(), dto.getAppointmentDate(), Appointment.Status.CANCELLED)) {
                throw new RuntimeException("Bác sĩ đã có lịch khám vào thời gian này");
            }
        }

        existing.setPatientId(dto.getPatientId());
        existing.setDoctorId(dto.getDoctorId());
        existing.setAppointmentDate(dto.getAppointmentDate());
        existing.setStatus(dto.getStatus());
        existing.setReason(dto.getReason());
        existing.setNotes(dto.getNotes());

        Appointment updated = appointmentRepository.save(existing);
        return AppointmentDto.fromEntity(updated);
    }

    public AppointmentDto updateStatus(Long id, Appointment.Status status) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch khám với ID: " + id));
        existing.setStatus(status);
        Appointment updated = appointmentRepository.save(existing);
        return AppointmentDto.fromEntity(updated);
    }

    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy lịch khám với ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByStatus(Appointment.Status status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end).stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByDoctorAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateBetween(doctorId, start, end).stream()
                .map(AppointmentDto::fromEntity)
                .collect(Collectors.toList());
    }
}