package com.medicare.patient.repository;

import com.medicare.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByFullNameContainingIgnoreCase(String name);

    boolean existsByInsuranceId(String insuranceId);

    List<Patient> findByGender(Patient.Gender gender);
}