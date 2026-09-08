package com.medicare.pharmacy.controller;

import com.medicare.pharmacy.entity.Medicine;
import com.medicare.pharmacy.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        return medicineRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medicine createMedicine(@RequestBody Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
        return medicineRepository.findById(id)
                .map(medicine -> {
                    medicine.setName(medicineDetails.getName());
                    medicine.setDescription(medicineDetails.getDescription());
                    medicine.setPrice(medicineDetails.getPrice());
                    medicine.setStockQuantity(medicineDetails.getStockQuantity());
                    return ResponseEntity.ok(medicineRepository.save(medicine));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        return medicineRepository.findById(id)
                .map(medicine -> {
                    medicineRepository.delete(medicine);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}