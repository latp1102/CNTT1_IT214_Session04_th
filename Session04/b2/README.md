# MediCare Microservices - Bài tập 2: Xây dựng API cơ bản

## Cấu trúc dự án

```
b2/
├── patient-service/          # Port 8081 - medicare_patient_db
├── doctor-service/           # Port 8082 - medicare_doctor_db
├── appointment-service/      # Port 8083 - medicare_appointment_db
├── medical-record-service/   # Port 8084 - medicare_medical_record_db
└── pharmacy-service/         # Port 8085 - medicare_pharmacy_db
```

Mỗi service là dự án Spring Boot độc lập với Gradle, cấu trúc:
```
src/main/java/com/medicare/{service}/
├── {Service}Application.java    # Main class
├── controller/                  # REST Controller
├── service/                     # Business Logic
├── repository/                  # Data Access (JPA)
├── model/                       # Entity JPA
└── dto/                         # Data Transfer Object
```

## Danh sách Service & API Endpoints

| Service | Port | Database | Base Path |
|---------|------|----------|-----------|
| Patient Service | 8081 | medicare_patient_db | `/api/patients` |
| Doctor Service | 8082 | medicare_doctor_db | `/api/doctors` |
| Appointment Service | 8083 | medicare_appointment_db | `/api/appointments` |
| Medical Record Service | 8084 | medicare_medical_record_db | `/api/medical-records` |
| Pharmacy Service | 8085 | medicare_pharmacy_db | `/api/medications` |

## API CRUD chung cho tất cả service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/{resource}` | Tạo mới |
| GET | `/api/{resource}` | Lấy danh sách |
| GET | `/api/{resource}/{id}` | Lấy theo ID |
| PUT | `/api/{resource}/{id}` | Cập nhật toàn bộ |
| DELETE | `/api/{resource}/{id}` | Xóa |

### API mở rộng (ví dụ Patient Service)
- `GET /api/patients/search?name={name}` - Tìm kiếm theo tên
- `GET /api/patients/gender/{gender}` - Lọc theo giới tính

### API mở rộng (Appointment Service)
- `PATCH /api/appointments/{id}/status?status=CONFIRMED` - Cập nhật trạng thái
- `GET /api/appointments/patient/{patientId}` - Lịch khám của bệnh nhân
- `GET /api/appointments/doctor/{doctorId}` - Lịch khám của bác sĩ
- `GET /api/appointments/date-range?start=&end=` - Lịch khám theo khoảng thời gian

## Cách chạy

### 1. Chuẩn bị MySQL
```sql
-- Tạo 5 database (hoặc để JPA auto-create qua config createDatabaseIfNotExist=true)
CREATE DATABASE medicare_patient_db;
CREATE DATABASE medicare_doctor_db;
CREATE DATABASE medicare_appointment_db;
CREATE DATABASE medicare_medical_record_db;
CREATE DATABASE medicare_pharmacy_db;
```

### 2. Cấu hình kết nối DB
Mở `src/main/resources/application.yml` của từng service, sửa `username`/`password` MySQL của máy bạn.

### 3. Chạy từng service (mở 5 terminal riêng biệt)

```bash
# Terminal 1 - Patient Service
cd patient-service
./gradlew bootRun

# Terminal 2 - Doctor Service
cd doctor-service
./gradlew bootRun

# Terminal 3 - Appointment Service
cd appointment-service
./gradlew bootRun

# Terminal 4 - Medical Record Service
cd medical-record-service
./gradlew bootRun

# Terminal 5 - Pharmacy Service
cd pharmacy-service
./gradlew bootRun
```

Hoặc build jar rồi chạy:
```bash
./gradlew build
java -jar build/libs/*.jar
```

## Test bằng Postman

### Tạo bệnh nhân (POST http://localhost:8081/api/patients)
```json
{
    "fullName": "Nguyễn Văn A",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE",
    "phone": "0901234567",
    "address": "123 Trần Hưng Đạo, Hà Nội",
    "insuranceId": "BH123456789"
}
```

### Tạo bác sĩ (POST http://localhost:8082/api/doctors)
```json
{
    "fullName": "BS. Trần Thị B",
    "specialization": "Nội khoa",
    "phone": "0912345678",
    "email": "tranthib@medicare.vn",
    "licenseNumber": "MD123456",
    "department": "Khoa Nội"
}
```

### Tạo lịch khám (POST http://localhost:8083/api/appointments)
```json
{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDate": "2026-09-10T09:00:00",
    "reason": "Khám tổng quát",
    "notes": "Đem theo kết quả xét nghiệm cũ"
}
```

### Tạo hồ sơ bệnh án (POST http://localhost:8084/api/medical-records)
```json
{
    "patientId": 1,
    "doctorId": 1,
    "appointmentId": 1,
    "visitDate": "2026-09-10T09:00:00",
    "chiefComplaint": "Đau bụng, buồn nôn",
    "diagnosis": "Viêm dạ dày cấp",
    "icd10Code": "K29.0",
    "treatmentPlan": "Uống thuốc 7 ngày, tái khám 1 tuần",
    "followUpDate": "2026-09-17"
}
```

### Tạo thuốc (POST http://localhost:8085/api/medications)
```json
{
    "name": "Paracetamol",
    "genericName": "Acetaminophen",
    "strength": "500mg",
    "dosageForm": "TABLET",
    "manufacturer": "Dược phẩm Hà Nội",
    "unit": "VIEN",
    "price": 2500,
    "isPrescriptionRequired": false,
    "isActive": true
}
```

## Dependency chính (build.gradle)

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    runtimeOnly 'com.mysql:mysql-connector-j'
}
```

## Lưu ý
- Mỗi service chạy độc lập, không phụ thuộc lẫn nhau
- Cross-service reference chỉ lưu ID (patientId, doctorId, appointmentId...), không JOIN cross-database
- JPA `ddl-auto: update` tự tạo bảng khi khởi động
- Cần Java 17+, MySQL 8.0+, Gradle 8+