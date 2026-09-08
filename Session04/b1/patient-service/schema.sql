-- Database: medicare_patient_db
CREATE DATABASE IF NOT EXISTS medicare_patient_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medicare_patient_db;

-- Bảng patients: Quản lý thông tin bệnh nhân
CREATE TABLE patients (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    date_of_birth   DATE NOT NULL,
    gender          ENUM('MALE','FEMALE','OTHER') NOT NULL,
    phone           VARCHAR(15),
    address         VARCHAR(255),
    insurance_id    VARCHAR(20),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng patient_medical_history: Lịch sử khám bệnh của bệnh nhân (tóm tắt)
CREATE TABLE patient_medical_history (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id      BIGINT NOT NULL,
    appointment_id  BIGINT NOT NULL,        -- Tham chiếu đến Appointment Service (chỉ lưu ID)
    doctor_id       BIGINT NOT NULL,        -- Tham chiếu đến Doctor Service (chỉ lưu ID)
    visit_date      DATETIME NOT NULL,
    chief_complaint VARCHAR(500),           -- Triệu chứng chính
    diagnosis       VARCHAR(500),           -- Chẩn đoán sơ bộ
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_patient_id (patient_id),
    INDEX idx_appointment_id (appointment_id),
    INDEX idx_doctor_id (doctor_id)
);