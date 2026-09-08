# MediCare Microservices - Config Server Exercise

Hệ thống Microservices MediCare với Spring Cloud Config Server quản lý cấu hình tập trung.

## Cấu trúc dự án

```
b3/
├── config-server/              # Config Server (port 8888)
├── patient-service/            # Patient Service (port 8081)
├── doctor-service/             # Doctor Service (port 8082)
├── appointment-service/        # Appointment Service (port 8083)
├── medical-record-service/     # Medical Record Service (port 8084)
├── pharmacy-service/           # Pharmacy Service (port 8085)
├── settings.gradle             # Root settings
└── README.md                   # This file
```

## Yêu cầu hệ thống

- Java 17+
- Gradle 8+
- MySQL 8.0+ (chạy trên localhost:3306, user: root, password: root)
- Eureka Server (port 8761) - cần chạy riêng

## Thứ tự khởi động

### 1. Khởi động MySQL
Đảm bảo MySQL đang chạy trên `localhost:3306` với user `root` và password `root`.

### 2. Khởi động Eureka Server
Cần có Eureka Server chạy trên `http://localhost:8761/eureka/` (từ Session 02/03).

### 3. Khởi động Config Server (QUAN TRỌNG: Khởi động đầu tiên)
```bash
cd config-server
./gradlew bootRun
```
Config Server sẽ chạy trên **port 8888**.

Kiểm tra Config Server hoạt động:
- Mở trình duyệt truy cập: `http://localhost:8888/patient-service/default`
- Nên thấy JSON trả về cấu hình của patient-service

### 4. Khởi động các Microservice (theo bất kỳ thứ tự nào)
```bash
# Terminal 1
cd patient-service
./gradlew bootRun

# Terminal 2
cd doctor-service
./gradlew bootRun

# Terminal 3
cd appointment-service
./gradlew bootRun

# Terminal 4
cd medical-record-service
./gradlew bootRun

# Terminal 5
cd pharmacy-service
./gradlew bootRun
```

Mỗi service sẽ:
1. Kết nối đến Config Server (localhost:8888) để lấy cấu hình
2. Đăng ký với Eureka Server (localhost:8761)
3. Khởi động trên port tương ứng (8081-8085)

### 5. Kiểm tra log khởi động
Khi service khởi động, bạn sẽ thấy log giống như:
```
Fetching config from server at: http://localhost:8888
Located environment: name=patient-service, profiles=[default], label=null
Located property source: CompositePropertySource {name='configService', propertySources=[...]}
```

### 6. Test API trên Postman

**Patient Service (port 8081):**
- GET    `http://localhost:8081/api/patients`
- POST   `http://localhost:8081/api/patients` (body: {name, email, phone, address, dateOfBirth})
- GET    `http://localhost:8081/api/patients/{id}`
- PUT    `http://localhost:8081/api/patients/{id}`
- DELETE `http://localhost:8081/api/patients/{id}`

**Doctor Service (port 8082):**
- GET    `http://localhost:8082/api/doctors`
- POST   `http://localhost:8082/api/doctors` (body: {name, specialty, email, phone, department, experienceYears})
- GET    `http://localhost:8082/api/doctors/{id}`
- PUT    `http://localhost:8082/api/doctors/{id}`
- DELETE `http://localhost:8082/api/doctors/{id}`

**Appointment Service (port 8083):**
- GET    `http://localhost:8083/api/appointments`
- POST   `http://localhost:8083/api/appointments` (body: {patientId, doctorId, appointmentDate, appointmentTime, status, reason})
- GET    `http://localhost:8083/api/appointments/{id}`
- PUT    `http://localhost:8083/api/appointments/{id}`
- DELETE `http://localhost:8083/api/appointments/{id}`

**Medical Record Service (port 8084):**
- GET    `http://localhost:8084/api/medical-records`
- POST   `http://localhost:8084/api/medical-records` (body: {patientId, doctorId, appointmentId, diagnosis, treatment, prescription, notes, recordDate})
- GET    `http://localhost:8084/api/medical-records/{id}`
- PUT    `http://localhost:8084/api/medical-records/{id}`
- DELETE `http://localhost:8084/api/medical-records/{id}`

**Pharmacy Service (port 8085):**
- GET    `http://localhost:8085/api/medicines`
- POST   `http://localhost:8085/api/medicines` (body: {name, description, price, stockQuantity, manufacturer, expiryDate})
- GET    `http://localhost:8085/api/medicines/{id}`
- PUT    `http://localhost:8085/api/medicines/{id}`
- DELETE `http://localhost:8085/api/medicines/{id}`

## Kiểm tra Config Server

Các endpoint Config Server:
- `http://localhost:8888/patient-service/default`
- `http://localhost:8888/doctor-service/default`
- `http://localhost:8888/appointment-service/default`
- `http://localhost:8888/medical-record-service/default`
- `http://localhost:8888/pharmacy-service/default`

Tất cả đều trả về JSON với cấu hình đầy đủ (database, port, eureka, jpa...).

## Lưu ý quan trọng

1. **Config Server PHẢI chạy trước** tất cả các microservice
2. Các microservice **KHÔNG CÓ** file `application.yml` với cấu hình database - chúng dùng `bootstrap.yml` để trỏ đến Config Server
3. Cấu hình database nằm tập trung trong `config-server/src/main/resources/config-repo/*.yml`
4. Khi cần đổi password database: chỉ sửa file trong `config-repo/` và restart các service (hoặc dùng Spring Cloud Bus + Actuator /refresh)

## Troubleshooting

**Service không kết nối được Config Server:**
- Kiểm tra Config Server đã chạy trên port 8888 chưa
- Kiểm tra log: `Fetching config from server at: http://localhost:8888`

**Lỗi database:**
- Đảm bảo MySQL chạy và user root/root có quyền tạo database
- Config Server tạo tự động database nếu chưa tồn tại (`createDatabaseIfNotExist=true`)

**Eureka không thấy service:**
- Đảm bảo Eureka Server chạy trên port 8761
- Kiểm tra `eureka.client.service-url.defaultZone` trong config-repo