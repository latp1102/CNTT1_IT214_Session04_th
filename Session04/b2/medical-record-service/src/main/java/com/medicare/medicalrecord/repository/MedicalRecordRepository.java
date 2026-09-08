package com.medicare.medicalrecord.repository;

import com.medicare.medicalrecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByDoctorId(Long doctorId);

    List<MedicalRecord> findByAppointmentId(Long appointmentId);

    List<MedicalRecord> findByVisitDateBetween(LocalDateTime start, LocalDateTime end);

    List<MedicalRecord> findByIcd10Code(String icd10Code);
}