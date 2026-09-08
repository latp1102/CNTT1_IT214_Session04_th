package com.medicare.pharmacy.dto;

import com.medicare.pharmacy.model.Medication;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationDto {

    private Long id;
    private String name;
    private String genericName;
    private String strength;
    private Medication.DosageForm dosageForm;
    private String manufacturer;
    private String unit;
    private BigDecimal price;
    private Boolean isPrescriptionRequired;
    private Boolean isActive;

    public static MedicationDto fromEntity(Medication medication) {
        if (medication == null) return null;
        return MedicationDto.builder()
                .id(medication.getId())
                .name(medication.getName())
                .genericName(medication.getGenericName())
                .strength(medication.getStrength())
                .dosageForm(medication.getDosageForm())
                .manufacturer(medication.getManufacturer())
                .unit(medication.getUnit())
                .price(medication.getPrice())
                .isPrescriptionRequired(medication.getIsPrescriptionRequired())
                .isActive(medication.getIsActive())
                .build();
    }

    public Medication toEntity() {
        Medication medication = new Medication();
        medication.setId(this.id);
        medication.setName(this.name);
        medication.setGenericName(this.genericName);
        medication.setStrength(this.strength);
        medication.setDosageForm(this.dosageForm);
        medication.setManufacturer(this.manufacturer);
        medication.setUnit(this.unit);
        medication.setPrice(this.price);
        medication.setIsPrescriptionRequired(this.isPrescriptionRequired);
        medication.setIsActive(this.isActive);
        return medication;
    }
}