package com.medicare.doctor.repository;

import com.medicare.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    List<Doctor> findByDepartment(String department);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findByFullNameContainingIgnoreCase(String name);
}