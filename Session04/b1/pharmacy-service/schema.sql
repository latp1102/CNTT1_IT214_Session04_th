-- Database: medicare_pharmacy_db
CREATE DATABASE IF NOT EXISTS medicare_pharmacy_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medicare_pharmacy_db;

-- Bảng medications: Danh mục thuốc
CREATE TABLE medications (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(200) NOT NULL,
    generic_name        VARCHAR(200),                       -- Tên quốc tế (INN)
    strength            VARCHAR(100),                       -- Hàm lượng (ví dụ: 500mg)
    dosage_form         ENUM('TABLET','CAPSULE','SYRUP','INJECTION','CREAM','DROPS','SUPPOSITORY','OTHER'),
    manufacturer        VARCHAR(200),
    unit                VARCHAR(20) DEFAULT 'VIEN',         -- Đơn vị: VIEN, CHAI, HOP, AMP
    price               DECIMAL(12,2) NOT NULL,             -- Giá bán
    is_prescription_required BOOLEAN DEFAULT TRUE,          -- Cần đơn thuốc hay không
    is_active           BOOLEAN DEFAULT TRUE,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_generic_name (generic_name),
    INDEX idx_is_active (is_active)
);

-- Bảng inventory: Tồn kho thuốc
CREATE TABLE inventory (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medication_id       BIGINT NOT NULL,
    batch_number        VARCHAR(50) NOT NULL,               -- Số lô
    expiry_date         DATE NOT NULL,                      -- Hạn sử dụng
    quantity_in_stock   INT NOT NULL DEFAULT 0,             -- Số lượng tồn
    unit_cost           DECIMAL(12,2),                      -- Giá nhập
    supplier_name       VARCHAR(200),
    received_date       DATE,
    location            VARCHAR(100),                       -- Vị trí trong kho (kệ, ngăn)
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_medication_id (medication_id),
    INDEX idx_batch_number (batch_number),
    INDEX idx_expiry_date (expiry_date),
    CONSTRAINT fk_inventory_medication FOREIGN KEY (medication_id) REFERENCES medications(id) ON DELETE CASCADE,
    UNIQUE KEY uk_medication_batch (medication_id, batch_number)
);

-- Bảng prescriptions_dispensed: Thuốc đã cấp phát (xuất kho theo đơn)
CREATE TABLE prescriptions_dispensed (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id     BIGINT NOT NULL,                    -- Tham chiếu đến Prescription trong Medical Record Service (chỉ lưu ID)
    patient_id          BIGINT NOT NULL,                    -- Tham chiếu đến Patient Service (chỉ lưu ID)
    medication_id       BIGINT NOT NULL,
    inventory_id        BIGINT NOT NULL,                    -- Lô thuốc xuất
    quantity_dispensed  INT NOT NULL,
    dispensed_price     DECIMAL(12,2) NOT NULL,             -- Giá tại thời điểm xuất
    dispensed_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    dispensed_by        VARCHAR(100),                       -- Người cấp thuốc (dược sĩ)
    notes               VARCHAR(255),
    INDEX idx_prescription_id (prescription_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_medication_id (medication_id),
    INDEX idx_inventory_id (inventory_id),
    INDEX idx_dispensed_at (dispensed_at),
    CONSTRAINT fk_dispensed_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE RESTRICT
);

-- Bảng inventory_transactions: Nhật ký nhập/xuất kho
CREATE TABLE inventory_transactions (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medication_id       BIGINT NOT NULL,
    inventory_id        BIGINT,
    transaction_type    ENUM('IMPORT','EXPORT','ADJUSTMENT','RETURN','EXPIRED') NOT NULL,
    quantity            INT NOT NULL,
    unit_price          DECIMAL(12,2),
    reference_id        BIGINT,                             -- ID tham chiếu (prescription_id, import_order_id...)
    reference_type      VARCHAR(50),                        -- PRESCRIPTION, IMPORT_ORDER, ADJUSTMENT...
    performed_by        VARCHAR(100),
    performed_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes               VARCHAR(500),
    INDEX idx_medication_id (medication_id),
    INDEX idx_inventory_id (inventory_id),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_performed_at (performed_at),
    CONSTRAINT fk_transaction_medication FOREIGN KEY (medication_id) REFERENCES medications(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE SET NULL
);