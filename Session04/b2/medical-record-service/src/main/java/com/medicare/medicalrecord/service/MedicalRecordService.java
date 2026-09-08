package com.medicare.medicalrecord.service;

import com.medicare.medicalrecord.model.MedicalRecord;
import com.medicare.medicalrecord.repository.MedicalRecordRepository;
import com.medicare.medicalrecord.dto.MedicalRecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordDto create(MedicalRecordDto dto) {
        if (medicalRecordRepository.findByAppointmentId(dto.getAppointmentId()).size() > 0) {
            throw new RuntimeException("Đã tồn tại hồ sơ bệnh án cho lịch khám này");
        }
        MedicalRecord record = dto.toEntity();
        MedicalRecord saved = medicalRecordRepository.save(record);
        return MedicalRecordDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getAll() {
        return medicalRecordRepository.findAll().stream()
                .map(MedicalRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicalRecordDto getById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + id));
        return MedicalRecordDto.fromEntity(record);
    }

    public MedicalRecordDto update(Long id, MedicalRecordDto dto) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + id));

        existing.setPatientId(dto.getPatientId());
        existing.setDoctorId(dto.getDoctorId());
        existing.setAppointmentId(dto.getAppointmentId());
        existing.setVisitDate(dto.getVisitDate());
        existing.setChiefComplaint(dto.getChiefComplaint());
        existing.setPresentIllness(dto.getPresentIllness());
        existing.setPastHistory(dto.getPastHistory());
        existing.setFamilyHistory(dto.getFamilyHistory());
        existing.setClinicalExamination(dto.getClinicalExamination());
        existing.setDiagnosis(dto.getDiagnosis());
        existing.setIcd10Code(dto.getIcd10Code());
        existing.setTreatmentPlan(dto.getTreatmentPlan());
        existing.setFollowUpDate(dto.getFollowUpDate());

        MedicalRecord updated = medicalRecordRepository.save(existing);
        return MedicalRecordDto.fromEntity(updated);
    }

    public void delete(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(MedicalRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getByDoctorId(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(MedicalRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicalRecordDto getByAppointmentId(Long appointmentId) {
        List<MedicalRecord> records = medicalRecordRepository.findByAppointmentId(appointmentId);
        if (records.isEmpty()) {
            throw new RuntimeException("Không tìm thấy hồ sơ bệnh án cho lịch khám: " + appointmentId);
        }
        return MedicalRecordDto.fromEntity(records.get(0));
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return medicalRecordRepository.findByVisitDateBetween(start, end).stream()
                .map(MedicalRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getByIcd10Code(String icd10Code) {
        return medicalRecordRepository.findByIcd10Code(icd10Code).stream()
                .map(MedicalRecordDto::fromEntity)
                .collect(Collectors.toList());
    }
}