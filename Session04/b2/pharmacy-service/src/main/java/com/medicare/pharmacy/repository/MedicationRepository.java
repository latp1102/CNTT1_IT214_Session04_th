package com.medicare.pharmacy.repository;

import com.medicare.pharmacy.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByNameContainingIgnoreCase(String name);

    List<Medication> findByGenericNameContainingIgnoreCase(String genericName);

    List<Medication> findByIsActiveTrue();

    List<Medication> findByDosageForm(Medication.DosageForm dosageForm);

    Optional<Medication> findByNameAndStrengthAndDosageForm(String name, String strength, Medication.DosageForm dosageForm);
}