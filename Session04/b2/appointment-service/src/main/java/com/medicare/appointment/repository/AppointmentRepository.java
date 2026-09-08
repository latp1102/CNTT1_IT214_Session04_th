package com.medicare.appointment.repository;

import com.medicare.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByStatus(Appointment.Status status);

    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByDoctorIdAndAppointmentDateBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, LocalDateTime date, Appointment.Status excludeStatus);
}