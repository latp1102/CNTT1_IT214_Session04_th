package com.medicare.pharmacy.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "generic_name", length = 200)
    private String genericName;

    @Column(name = "strength", length = 100)
    private String strength;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_form")
    private DosageForm dosageForm;

    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    @Column(name = "unit", length = 20)
    private String unit = "VIEN";

    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "is_prescription_required")
    private Boolean isPrescriptionRequired = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum DosageForm {
        TABLET, CAPSULE, SYRUP, INJECTION, CREAM, DROPS, SUPPOSITORY, OTHER
    }
}