-- Database: medicare_doctor_db
CREATE DATABASE IF NOT EXISTS medicare_doctor_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medicare_doctor_db;

-- Bảng doctors: Quản lý thông tin bác sĩ
CREATE TABLE doctors (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    specialization  VARCHAR(100) NOT NULL,      -- Chuyên khoa
    phone           VARCHAR(15),
    email           VARCHAR(100),
    license_number  VARCHAR(50) UNIQUE,         -- Số phép hành nghề
    department      VARCHAR(100),               -- Khoa/Phòng ban
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng doctor_schedules: Lịch làm việc của bác sĩ
CREATE TABLE doctor_schedules (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id       BIGINT NOT NULL,
    day_of_week     ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    room_number     VARCHAR(20),                -- Số phòng khám
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_day_of_week (day_of_week),
    CONSTRAINT fk_schedule_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- Bảng doctor_leave: Nghỉ phép của bác sĩ
CREATE TABLE doctor_leave (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id       BIGINT NOT NULL,
    leave_date      DATE NOT NULL,
    reason          VARCHAR(255),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_leave_date (leave_date),
    CONSTRAINT fk_leave_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);