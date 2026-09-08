-- Database: medicare_medical_record_db
CREATE DATABASE IF NOT EXISTS medicare_medical_record_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medicare_medical_record_db;

-- Bảng medical_records: Hồ sơ bệnh án chi tiết
CREATE TABLE medical_records (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,                    -- Tham chiếu đến Patient Service (chỉ lưu ID)
    doctor_id           BIGINT NOT NULL,                    -- Tham chiếu đến Doctor Service (chỉ lưu ID)
    appointment_id      BIGINT NOT NULL,                    -- Tham chiếu đến Appointment Service (chỉ lưu ID)
    visit_date          DATETIME NOT NULL,
    chief_complaint     VARCHAR(1000),                      -- Triệu chứng chính
    present_illness     TEXT,                               -- Bệnh sử hiện tại
    past_history        TEXT,                               -- Tiền sử bệnh
    family_history      TEXT,                               -- Tiền sử gia đình
    clinical_examination TEXT,                              -- Khám lâm sàng
    diagnosis           VARCHAR(500) NOT NULL,              -- Chẩn đoán
    icd10_code          VARCHAR(20),                        -- Mã ICD-10
    treatment_plan      TEXT,                               -- Kế hoạch điều trị
    follow_up_date      DATE,                               -- Ngày tái khám
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_appointment_id (appointment_id),
    INDEX idx_visit_date (visit_date),
    INDEX idx_icd10_code (icd10_code)
);

-- Bảng prescriptions: Đơn thuốc
CREATE TABLE prescriptions (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_record_id   BIGINT NOT NULL,
    medication_name     VARCHAR(200) NOT NULL,              -- Tên thuốc
    dosage              VARCHAR(100),                       -- Liều lượng
    frequency           VARCHAR(100),                       -- Tần suất uống
    duration_days       INT,                                -- Số ngày uống
    instructions        VARCHAR(500),                       -- Hướng dẫn sử dụng
    quantity            INT DEFAULT 1,                      -- Số lượng
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_medical_record_id (medical_record_id),
    CONSTRAINT fk_prescription_record FOREIGN KEY (medical_record_id) REFERENCES medical_records(id) ON DELETE CASCADE
);

-- Bảng medical_attachments: Tệp đính kèm (X-quang, MRI, kết quả xét nghiệm...)
CREATE TABLE medical_attachments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_record_id   BIGINT NOT NULL,
    file_name           VARCHAR(255) NOT NULL,
    file_path           VARCHAR(500) NOT NULL,
    file_type           VARCHAR(50),                        -- image/pdf/document
    description         VARCHAR(500),
    uploaded_at         DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_medical_record_id (medical_record_id),
    CONSTRAINT fk_attachment_record FOREIGN KEY (medical_record_id) REFERENCES medical_records(id) ON DELETE CASCADE
);