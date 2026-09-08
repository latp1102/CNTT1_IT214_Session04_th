package com.medicare.appointmentservice.service;

import com.medicare.appointmentservice.client.DoctorClientDto;
import com.medicare.appointmentservice.client.PatientClientDto;
import com.medicare.appointmentservice.dto.AppointmentDetailDto;
import com.medicare.appointmentservice.dto.AppointmentDto;
import com.medicare.appointmentservice.entity.Appointment;
import com.medicare.appointmentservice.exception.AppointmentNotFoundException;
import com.medicare.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        // Gọi Patient Service để kiểm tra patientId
        String patientUrl = "http://patient-service/api/patients/" + appointmentDto.getPatientId();
        try {
            ResponseEntity<PatientClientDto> patientResponse = restTemplate.getForEntity(patientUrl, PatientClientDto.class);
            if (patientResponse.getStatusCode().isError() || patientResponse.getBody() == null) {
                throw new RuntimeException("Patient not found with id: " + appointmentDto.getPatientId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Patient not found with id: " + appointmentDto.getPatientId());
        }

        // Gọi Doctor Service để kiểm tra doctorId
        String doctorUrl = "http://doctor-service/api/doctors/" + appointmentDto.getDoctorId();
        try {
            ResponseEntity<DoctorClientDto> doctorResponse = restTemplate.getForEntity(doctorUrl, DoctorClientDto.class);
            if (doctorResponse.getStatusCode().isError() || doctorResponse.getBody() == null) {
                throw new RuntimeException("Doctor not found with id: " + appointmentDto.getDoctorId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Doctor not found with id: " + appointmentDto.getDoctorId());
        }

        // Nếu cả 2 đều tồn tại, lưu lịch khám
        Appointment appointment = Appointment.builder()
                .patientId(appointmentDto.getPatientId())
                .doctorId(appointmentDto.getDoctorId())
                .appointmentDate(appointmentDto.getAppointmentDate())
                .status(Appointment.AppointmentStatus.valueOf(appointmentDto.getStatus()))
                .notes(appointmentDto.getNotes())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return toDto(saved);
    }

    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        return toDto(appointment);
    }

    public AppointmentDetailDto getAppointmentDetail(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        // Gọi Patient Service để lấy thông tin chi tiết
        String patientUrl = "http://patient-service/api/patients/" + appointment.getPatientId();
        PatientClientDto patient = null;
        try {
            ResponseEntity<PatientClientDto> patientResponse = restTemplate.getForEntity(patientUrl, PatientClientDto.class);
            patient = patientResponse.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch patient info: " + e.getMessage());
        }

        // Gọi Doctor Service để lấy thông tin chi tiết
        String doctorUrl = "http://doctor-service/api/doctors/" + appointment.getDoctorId();
        DoctorClientDto doctor = null;
        try {
            ResponseEntity<DoctorClientDto> doctorResponse = restTemplate.getForEntity(doctorUrl, DoctorClientDto.class);
            doctor = doctorResponse.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch doctor info: " + e.getMessage());
        }

        // Ghép dữ liệu và trả về
        return AppointmentDetailDto.builder()
                .appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus().name())
                .patientId(patient != null ? patient.getId() : null)
                .patientName(patient != null ? patient.getName() : null)
                .patientPhone(patient != null ? patient.getPhone() : null)
                .doctorId(doctor != null ? doctor.getId() : null)
                .doctorName(doctor != null ? doctor.getName() : null)
                .doctorSpecialty(doctor != null ? doctor.getSpecialty() : null)
                .build();
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        // Verify patient and doctor exist if changed
        if (!appointment.getPatientId().equals(appointmentDto.getPatientId())) {
            String patientUrl = "http://patient-service/api/patients/" + appointmentDto.getPatientId();
            try {
                restTemplate.getForEntity(patientUrl, PatientClientDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Patient not found with id: " + appointmentDto.getPatientId());
            }
        }

        if (!appointment.getDoctorId().equals(appointmentDto.getDoctorId())) {
            String doctorUrl = "http://doctor-service/api/doctors/" + appointmentDto.getDoctorId();
            try {
                restTemplate.getForEntity(doctorUrl, DoctorClientDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Doctor not found with id: " + appointmentDto.getDoctorId());
            }
        }

        appointment.setPatientId(appointmentDto.getPatientId());
        appointment.setDoctorId(appointmentDto.getDoctorId());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(appointmentDto.getStatus()));
        appointment.setNotes(appointmentDto.getNotes());

        Appointment updated = appointmentRepository.save(appointment);
        return toDto(updated);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return appointmentRepository.existsById(id);
    }

    public boolean isAppointmentCompleted(Long id) {
        return appointmentRepository.findById(id)
                .map(a -> a.getStatus() == Appointment.AppointmentStatus.COMPLETED)
                .orElse(false);
    }

    private AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus().name())
                .notes(appointment.getNotes())
                .build();
    }
}