package com.medicare.medicalrecord.dto;

import com.medicare.medicalrecord.model.MedicalRecord;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDto {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private LocalDateTime visitDate;
    private String chiefComplaint;
    private String presentIllness;
    private String pastHistory;
    private String familyHistory;
    private String clinicalExamination;
    private String diagnosis;
    private String icd10Code;
    private String treatmentPlan;
    private LocalDate followUpDate;

    public static MedicalRecordDto fromEntity(MedicalRecord record) {
        if (record == null) return null;
        return MedicalRecordDto.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .doctorId(record.getDoctorId())
                .appointmentId(record.getAppointmentId())
                .visitDate(record.getVisitDate())
                .chiefComplaint(record.getChiefComplaint())
                .presentIllness(record.getPresentIllness())
                .pastHistory(record.getPastHistory())
                .familyHistory(record.getFamilyHistory())
                .clinicalExamination(record.getClinicalExamination())
                .diagnosis(record.getDiagnosis())
                .icd10Code(record.getIcd10Code())
                .treatmentPlan(record.getTreatmentPlan())
                .followUpDate(record.getFollowUpDate())
                .build();
    }

    public MedicalRecord toEntity() {
        MedicalRecord record = new MedicalRecord();
        record.setId(this.id);
        record.setPatientId(this.patientId);
        record.setDoctorId(this.doctorId);
        record.setAppointmentId(this.appointmentId);
        record.setVisitDate(this.visitDate);
        record.setChiefComplaint(this.chiefComplaint);
        record.setPresentIllness(this.presentIllness);
        record.setPastHistory(this.pastHistory);
        record.setFamilyHistory(this.familyHistory);
        record.setClinicalExamination(this.clinicalExamination);
        record.setDiagnosis(this.diagnosis);
        record.setIcd10Code(this.icd10Code);
        record.setTreatmentPlan(this.treatmentPlan);
        record.setFollowUpDate(this.followUpDate);
        return record;
    }
}