-- Database: medicare_appointment_db
CREATE DATABASE IF NOT EXISTS medicare_appointment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medicare_appointment_db;

-- Bảng appointments: Quản lý lịch khám bệnh
CREATE TABLE appointments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id      BIGINT NOT NULL,            -- Tham chiếu đến Patient Service (chỉ lưu ID)
    doctor_id       BIGINT NOT NULL,            -- Tham chiếu đến Doctor Service (chỉ lưu ID)
    appointment_date DATETIME NOT NULL,         -- Ngày giờ khám
    status          ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED') DEFAULT 'PENDING',
    reason          VARCHAR(500),               -- Lý do khám
    notes           VARCHAR(1000),              -- Ghi chú thêm
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_status (status)
);

-- Bảng appointment_status_history: Lịch sử thay đổi trạng thái lịch khám
CREATE TABLE appointment_status_history (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id  BIGINT NOT NULL,
    old_status      ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED'),
    new_status      ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED') NOT NULL,
    changed_by      VARCHAR(100),               -- Ai thay đổi (system, doctor, patient, admin)
    changed_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    reason          VARCHAR(255),
    INDEX idx_appointment_id (appointment_id),
    CONSTRAINT fk_history_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);