# MediCare Microservices - Bài Tập 5: Giao tiếp giữa các Microservice bằng RestTemplate

## Tổng quan

Dự án này triển khai hệ thống MediCare với 6 microservices giao tiếp với nhau thông qua **RestTemplate** và **Eureka Discovery**:

1. **Config Server** (Port 8888) - Cấu hình tập trung từ Git
2. **Eureka Server** (Port 8761) - Service Discovery
3. **Patient Service** (Port 8081) - Quản lý bệnh nhân
4. **Doctor Service** (Port 8082) - Quản lý bác sĩ
5. **Appointment Service** (Port 8083) - Quản lý lịch khám (gọi Patient & Doctor Service)
6. **Medical Record Service** (Port 8084) - Quản lý hồ sơ bệnh án (gọi Appointment Service)

## Kiến trúc

```
┌─────────────┐     ┌─────────────┐
│ Config Server│────▶│  Git Repo   │
└─────────────┘     └─────────────┘
       │
       ▼
┌─────────────┐     ┌──────────────────┐
│ Eureka Server│◀───│  All Services    │
└─────────────┘     └──────────────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
    ┌───────────┐    ┌───────────┐    ┌───────────┐
    │  Patient  │    │  Doctor   │    │Appointment│
    │  Service  │    │  Service  │    │  Service  │
    │  (8081)   │    │  (8082)   │    │  (8083)   │
    └───────────┘    └───────────┘    └─────┬─────┘
                                             │
                                             ▼
                                     ┌───────────────┐
                                     │ Medical Record│
                                     │   Service     │
                                     │   (8084)      │
                                     └───────────────┘
```

## API Endpoints

### Patient Service (http://localhost:8081)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | /api/patients | Tạo bệnh nhân mới |
| GET | /api/patients/{id} | Lấy thông tin bệnh nhân |
| GET | /api/patients | Lấy tất cả bệnh nhân |
| PUT | /api/patients/{id} | Cập nhật bệnh nhân |
| DELETE | /api/patients/{id} | Xóa bệnh nhân |

### Doctor Service (http://localhost:8082)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | /api/doctors | Tạo bác sĩ mới |
| GET | /api/doctors/{id} | Lấy thông tin bác sĩ |
| GET | /api/doctors | Lấy tất cả bác sĩ |
| PUT | /api/doctors/{id} | Cập nhật bác sĩ |
| DELETE | /api/doctors/{id} | Xóa bác sĩ |

### Appointment Service (http://localhost:8083)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | /api/appointments | Tạo lịch khám (kiểm tra patient & doctor) |
| GET | /api/appointments/{id} | Lấy thông tin lịch khám |
| GET | /api/appointments/{id}/detail | **Lấy chi tiết kèm Patient & Doctor** |
| GET | /api/appointments | Lấy tất cả lịch khám |
| GET | /api/appointments/patient/{patientId} | Lấy lịch khám theo bệnh nhân |
| GET | /api/appointments/doctor/{doctorId} | Lấy lịch khám theo bác sĩ |
| PUT | /api/appointments/{id} | Cập nhật lịch khám |
| DELETE | /api/appointments/{id} | Xóa lịch khám |

### Medical Record Service (http://localhost:8084)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | /api/medical-records | Tạo hồ sơ bệnh án (kiểm tra appointment COMPLETED) |
| GET | /api/medical-records/{id} | Lấy hồ sơ bệnh án |
| GET | /api/medical-records | Lấy tất cả hồ sơ |
| GET | /api/medical-records/patient/{patientId} | Lấy hồ sơ theo bệnh nhân |
| GET | /api/medical-records/doctor/{doctorId} | Lấy hồ sơ theo bác sĩ |
| GET | /api/medical-records/appointment/{appointmentId} | Lấy hồ sơ theo lịch khám |
| PUT | /api/medical-records/{id} | Cập nhật hồ sơ |
| DELETE | /api/medical-records/{id} | Xóa hồ sơ |

## Thứ tự khởi động (QUAN TRỌNG)

```bash
# 1. Khởi động Config Server trước
cd config-server
mvn spring-boot:run

# 2. Khởi động Eureka Server
cd eureka-server
mvn spring-boot:run

# 3. Khởi động các Service (có thể chạy song song)
cd patient-service
mvn spring-boot:run

cd doctor-service
mvn spring-boot:run

cd appointment-service
mvn spring-boot:run

cd medical-record-service
mvn spring-boot:run
```

## Kiểm thử với Postman

### 1. Tạo dữ liệu mẫu

**Tạo 3 bệnh nhân:**
```bash
POST http://localhost:8081/api/patients
{
  "name": "Nguyễn Văn An",
  "phone": "0901234567",
  "email": "an.nguyen@email.com",
  "address": "Hà Nội",
  "dateOfBirth": "1990-01-15"
}

POST http://localhost:8081/api/patients
{
  "name": "Trần Thị Bình",
  "phone": "0902345678",
  "email": "binh.tran@email.com",
  "address": "TP.HCM",
  "dateOfBirth": "1985-05-20"
}

POST http://localhost:8081/api/patients
{
  "name": "Lê Văn Cường",
  "phone": "0903456789",
  "email": "cuong.le@email.com",
  "address": "Đà Nẵng",
  "dateOfBirth": "1992-11-30"
}
```

**Tạo 2 bác sĩ:**
```bash
POST http://localhost:8082/api/doctors
{
  "name": "BS. Nguyễn Minh Đức",
  "specialty": "Tim mạch",
  "phone": "0911111111",
  "email": "duc.nguyen@medicare.vn",
  "licenseNumber": "MD001"
}

POST http://localhost:8082/api/doctors
{
  "name": "BS. Phạm Thị Hương",
  "specialty": "Nhi khoa",
  "phone": "0922222222",
  "email": "huong.pham@medicare.vn",
  "licenseNumber": "MD002"
}
```

### 2. Test tạo lịch khám (thành công)
```bash
POST http://localhost:8083/api/appointments
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-09-15T10:00:00",
  "status": "PENDING",
  "notes": "Khám tim mạch định kỳ"
}
```
**Expected:** 200 OK với appointment được tạo

### 3. Test tạo lịch khám với patientId không tồn tại (lỗi)
```bash
POST http://localhost:8083/api/appointments
{
  "patientId": 999,
  "doctorId": 1,
  "appointmentDate": "2026-09-15T10:00:00",
  "status": "PENDING"
}
```
**Expected:** 500 Internal Server Error - "Patient not found with id: 999"

### 4. Test tạo lịch khám với doctorId không tồn tại (lỗi)
```bash
POST http://localhost:8083/api/appointments
{
  "patientId": 1,
  "doctorId": 999,
  "appointmentDate": "2026-09-15T10:00:00",
  "status": "PENDING"
}
```
**Expected:** 500 Internal Server Error - "Doctor not found with id: 999"

### 5. Test xem chi tiết lịch khám
```bash
GET http://localhost:8083/api/appointments/1/detail
```
**Expected:** 200 OK với đầy đủ thông tin Appointment + Patient + Doctor

```json
{
  "appointmentId": 1,
  "appointmentDate": "2026-09-15T10:00:00",
  "status": "PENDING",
  "patientId": 1,
  "patientName": "Nguyễn Văn An",
  "patientPhone": "0901234567",
  "doctorId": 1,
  "doctorName": "BS. Nguyễn Minh Đức",
  "doctorSpecialty": "Tim mạch"
}
```

### 6. Cập nhật appointment thành COMPLETED
```bash
PUT http://localhost:8083/api/appointments/1
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-09-15T10:00:00",
  "status": "COMPLETED",
  "notes": "Khám tim mạch định kỳ - đã xong"
}
```

### 7. Test tạo hồ sơ bệnh án (thành công - appointment COMPLETED)
```bash
POST http://localhost:8084/api/medical-records
{
  "appointmentId": 1,
  "patientId": 1,
  "doctorId": 1,
  "diagnosis": "Rối loạn nhịp tim nhẹ",
  "treatment": "Theo dõi, uống thuốc điều hòa nhịp tim",
  "prescription": "Bisohexal 2.5mg x 1 viên/ngày",
  "notes": "Tái khám sau 1 tháng"
}
```
**Expected:** 200 OK với medical record được tạo

### 8. Test tạo hồ sơ bệnh án với appointment chưa COMPLETED (lỗi)
```bash
POST http://localhost:8084/api/medical-records
{
  "appointmentId": 2,
  "patientId": 2,
  "doctorId": 2,
  "diagnosis": "Test",
  "treatment": "Test",
  "prescription": "Test"
}
```
**Expected:** 500 - "Appointment must be COMPLETED to create medical record"

### 9. Test tạo hồ sơ bệnh án với appointment không tồn tại (lỗi)
```bash
POST http://localhost:8084/api/medical-records
{
  "appointmentId": 999,
  "patientId": 1,
  "doctorId": 1,
  "diagnosis": "Test"
}
```
**Expected:** 500 - "Appointment not found with id: 999"

## Cấu trúc thư mục

```
b5/
├── pom.xml                          # Parent POM
├── config-server/                   # Config Server (8888)
├── eureka-server/                   # Eureka Server (8761)
├── patient-service/                 # Patient Service (8081)
├── doctor-service/                  # Doctor Service (8082)
├── appointment-service/             # Appointment Service (8083)
├── medical-record-service/          # Medical Record Service (8084)
├── README.md
└── screenshots/                     # Thư mục chứa ảnh Postman test
```

## Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Spring Cloud Config Server** - Cấu hình tập trung
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Data JPA** - ORM
- **H2 Database** - In-memory database
- **RestTemplate với @LoadBalanced** - Inter-service communication
- **Lombok** - Boilerplate code reduction

## Điểm nổi bật

1. **@LoadBalanced RestTemplate** - Gọi service bằng tên (service name) thay vì hard-code URL
2. **Eureka Discovery** - Tự động discover service instances
3. **Config Server** - Cấu hình tập trung từ Git repository
4. **Error Handling** - Xử lý lỗi khi service đích không khả dụng
5. **DTO Mapping** - Chuyển đổi giữa Entity và DTO
6. **Validation** - Kiểm tra tồn tại entity trước khi thực hiện nghiệp vụ

## Screenshots

Thư mục `screenshots/` chứa các ảnh test Postman:
- Tạo bệnh nhân/bác sĩ thành công
- Tạo appointment thành công
- Tạo appointment với patientId/doctorId không tồn tại (lỗi)
- Xem chi tiết appointment (ghép Patient + Doctor)
- Tạo medical record thành công
- Tạo medical record với appointment chưa COMPLETED (lỗi)