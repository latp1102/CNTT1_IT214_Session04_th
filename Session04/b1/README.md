# MediCare Microservices - Hệ thống Quản lý Bệnh viện

## 1. Sơ đồ Kiến trúc Tổng thể

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            API GATEWAY (Port 8080)                          │
│                        (Spring Cloud Gateway / Netflix Zuul)                │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        ▼                         ▼                         ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│Patient Service│         │Doctor Service │         │Appointment Svc│
│   Port: 8081  │         │   Port: 8082  │         │   Port: 8083  │
│ medicare_     │         │ medicare_     │         │ medicare_     │
│ patient_db    │         │ doctor_db     │         │ appointment_db│
└───────┬───────┘         └───────┬───────┘         └───────┬───────┘
        │                         │                         │
        ▼                         ▼                         ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│Medical Record │         │Pharmacy Serv. │         │  Service      │
│   Service     │◄────────│   Port: 8085  │         │  Discovery    │
│   Port: 8084  │         │ medicare_     │         │  (Eureka)     │
│ medicare_     │         │ pharmacy_db   │         │  Port: 8761   │
│ medical_rec_db│         └───────────────┘         └───────────────┘
└───────────────┘
```

## 2. Danh sách Microservices

| Service | Port | Database | Mô tả |
|---------|------|----------|-------|
| **Patient Service** | 8081 | `medicare_patient_db` | Quản lý thông tin bệnh nhân, lịch sử khám |
| **Doctor Service** | 8082 | `medicare_doctor_db` | Quản lý bác sĩ, lịch làm việc, nghỉ phép |
| **Appointment Service** | 8083 | `medicare_appointment_db` | Đặt lịch khám, quản lý trạng thái lịch |
| **Medical Record Service** | 8084 | `medicare_medical_record_db` | Hồ sơ bệnh án, đơn thuốc, tệp đính kèm |
| **Pharmacy Service** | 8085 | `medicare_pharmacy_db` | Danh mục thuốc, tồn kho, xuất thuốc |

## 3. Giải thích lý do tách Service

### Patient Service (Port 8081)
Tách riêng vì thông tin bệnh nhân là dữ liệu cốt lõi, thay đổi ít và được nhiều service khác tham chiếu. Việc tách biệt đảm bảo bảo mật thông tin cá nhân (PII) và tuân thủ quy định bảo vệ dữ liệu y tế (HIPAA/Vietnamese law). Service này chỉ quản lý identity và profile, không chứa logic nghiệp vụ phức tạp.

### Doctor Service (Port 8082)
Bác sĩ là tài nguyên quan trọng với lịch làm việc phức tạp (ca, phòng, nghỉ phép). Tách riêng cho phép khoa nhân sự quản lý độc lập, hỗ trợ tìm kiếm bác sĩ theo chuyên khoa/khả dụng. Logic scheduling phức tạp được cô lập, không ảnh hưởng patient/appointment.

### Appointment Service (Port 8083)
Lịch khám là trung tâm điều phối giữa Patient và Doctor. Tách riêng cho phép tối ưu thuật toán booking, conflict detection, waitlist management. Service này chỉ lưu `patient_id` và `doctor_id` làm reference, không JOIN cross-database.

### Medical Record Service (Port 8084)
Hồ sơ bệnh án chứa dữ liệu nhạy cảm, quy định lưu trữ lâu dài (10-20 năm). Tách biệt cho phép scale storage độc lập, áp dụng encryption/masking riêng. Chỉ lưu `appointment_id`, `patient_id`, `doctor_id` làm khóa ngoại logic.

### Pharmacy Service (Port 8085)
Kho dược có logic nghiệp vụ riêng: nhập/xuất kho, quản lý lô/hạn dùng, cảnh báo thuốc sắp hết. Tách biệt cho phép tích hợp với nhà cung cấp, tự động hóa đặt hàng. Chỉ lưu `prescription_id`, `patient_id`, `medication_id` làm reference.

## 4. Xử lý tham chiếu dữ liệu Cross-Service

**Nguyên tắc: Chỉ lưu ID tham chiếu (Reference ID), KHÔNG dùng JOIN cross-database**

| Cần dữ liệu từ | Cách xử lý |
|----------------|------------|
| Appointment cần tên bệnh nhân | Appointment lưu `patient_id` → Gọi Patient Service API `/api/patients/{id}` khi cần hiển thị tên |
| Medical Record cần tên bác sĩ | Medical Record lưu `doctor_id` → Gọi Doctor Service API `/api/doctors/{id}` |
| Pharmacy cần thông tin đơn thuốc | Pharmacy lưu `prescription_id` → Gọi Medical Record Service API `/api/prescriptions/{id}` |
| Patient cần lịch sử khám | Patient lưu `appointment_id` → Gọi Appointment Service API `/api/appointments/patient/{patientId}` |

**Công nghệ gợi ý cho komunikasi service-to-service:**
- **Sync:** REST API (OpenFeign/WebClient) hoặc gRPC
- **Async:** Apache Kafka / RabbitMQ (event-driven: `AppointmentCreated`, `MedicalRecordCompleted`, `PrescriptionIssued`)

## 5. Cấu trúc thư mục dự án

```
medicare-microservices/
├── docs/
│   ├── architecture.png
│   ├── patient-erd.png
│   ├── doctor-erd.png
│   ├── appointment-erd.png
│   ├── medical-record-erd.png
│   └── pharmacy-erd.png
├── patient-service/
│   ├── src/main/resources/application.yml
│   └── schema.sql
├── doctor-service/
│   ├── src/main/resources/application.yml
│   └── schema.sql
├── appointment-service/
│   ├── src/main/resources/application.yml
│   └── schema.sql
├── medical-record-service/
│   ├── src/main/resources/application.yml
│   └── schema.sql
├── pharmacy-service/
│   ├── src/main/resources/application.yml
│   └── schema.sql
└── README.md
```

## 6. ERD Mô tả (Text-based)

### Patient Service - `medicare_patient_db`
```
patients
├── id (PK)
├── full_name
├── date_of_birth
├── gender
├── phone
├── address
├── insurance_id
├── created_at
└── updated_at

patient_medical_history
├── id (PK)
├── patient_id (FK → patients.id)
├── appointment_id (Ref Appointment Service)
├── doctor_id (Ref Doctor Service)
├── visit_date
├── chief_complaint
├── diagnosis
└── created_at
```

### Doctor Service - `medicare_doctor_db`
```
doctors
├── id (PK)
├── full_name
├── specialization
├── phone
├── email
├── license_number (UK)
├── department
├── created_at
└── updated_at

doctor_schedules
├── id (PK)
├── doctor_id (FK → doctors.id)
├── day_of_week
├── start_time
├── end_time
├── room_number
├── is_active
├── created_at
└── updated_at

doctor_leave
├── id (PK)
├── doctor_id (FK → doctors.id)
├── leave_date
├── reason
└── created_at
```

### Appointment Service - `medicare_appointment_db`
```
appointments
├── id (PK)
├── patient_id (Ref Patient Service)
├── doctor_id (Ref Doctor Service)
├── appointment_date
├── status (PENDING/CONFIRMED/COMPLETED/CANCELLED)
├── reason
├── notes
├── created_at
└── updated_at

appointment_status_history
├── id (PK)
├── appointment_id (FK → appointments.id)
├── old_status
├── new_status
├── changed_by
├── changed_at
└── reason
```

### Medical Record Service - `medicare_medical_record_db`
```
medical_records
├── id (PK)
├── patient_id (Ref Patient Service)
├── doctor_id (Ref Doctor Service)
├── appointment_id (Ref Appointment Service)
├── visit_date
├── chief_complaint
├── present_illness
├── past_history
├── family_history
├── clinical_examination
├── diagnosis
├── icd10_code
├── treatment_plan
├── follow_up_date
├── created_at
└── updated_at

prescriptions
├── id (PK)
├── medical_record_id (FK → medical_records.id)
├── medication_name
├── dosage
├── frequency
├── duration_days
├── instructions
├── quantity
└── created_at

medical_attachments
├── id (PK)
├── medical_record_id (FK → medical_records.id)
├── file_name
├── file_path
├── file_type
├── description
└── uploaded_at
```

### Pharmacy Service - `medicare_pharmacy_db`
```
medications
├── id (PK)
├── name
├── generic_name
├── strength
├── dosage_form
├── manufacturer
├── unit
├── price
├── is_prescription_required
├── is_active
├── created_at
└── updated_at

inventory
├── id (PK)
├── medication_id (FK → medications.id)
├── batch_number
├── expiry_date
├── quantity_in_stock
├── unit_cost
├── supplier_name
├── received_date
├── location
├── created_at
└── updated_at
UK: (medication_id, batch_number)

prescriptions_dispensed
├── id (PK)
├── prescription_id (Ref Medical Record Service)
├── patient_id (Ref Patient Service)
├── medication_id (FK → medications.id)
├── inventory_id (FK → inventory.id)
├── quantity_dispensed
├── dispensed_price
├── dispensed_at
├── dispensed_by
└── notes

inventory_transactions
├── id (PK)
├── medication_id (FK → medications.id)
├── inventory_id (FK → inventory.id)
├── transaction_type (IMPORT/EXPORT/ADJUSTMENT/RETURN/EXPIRED)
├── quantity
├── unit_price
├── reference_id
├── reference_type
├── performed_by
├── performed_at
└── notes
```

## 7. Hướng dẫn chạy từng Service

Mỗi service là một Spring Boot project riêng biệt. Cấu hình database trong `src/main/resources/application.yml`.

Yêu cầu:
- Java 17+
- MySQL 8.0+
- Maven/Gradle

Chạy từng service:
```bash
cd patient-service
mvn spring-boot:run

cd doctor-service
mvn spring-boot:run

# ... tương tự cho các service khác
```

## 8. Lưu ý quan trọng

1. **Database-per-Service**: Mỗi service sở hữu database riêng, KHÔNG chia sẻ schema/table.
2. **Reference by ID**: Cross-service communication chỉ thông qua ID, không dùng foreign key vật lý.
3. **Event-driven**: Khuyến khích dùng Kafka/RabbitMQ cho eventual consistency (ví dụ: khi Appointment COMPLETED → publish event → Medical Record Service tạo record rỗng chờ bác sĩ điền).
4. **API Gateway**: Tất cả request từ client đi qua API Gateway (port 8080) để routing, auth, rate-limiting.
5. **Service Discovery**: Sử dụng Eureka/Consul để service tự đăng ký và khám phá nhau.