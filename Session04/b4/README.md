# MediCare Microservices - Session 04 Exercise 4

## Project Structure

```
b4/
├── config-server/              # Spring Cloud Config Server (port 8888)
├── discovery-server/           # Eureka Discovery Server (port 8761)
├── patient-service/            # Patient Microservice (port 8081)
├── doctor-service/             # Doctor Microservice (port 8082)
├── appointment-service/        # Appointment Microservice (port 8083)
├── medical-record-service/     # Medical Record Microservice (port 8084)
├── pharmacy-service/           # Pharmacy Microservice (port 8085)
└── medicare-config-repo/       # Git repository for configuration files
```

## Prerequisites

- Java 17+
- MySQL 8.0+
- Gradle 8.x

## Database Setup

Create the following databases in MySQL:

```sql
CREATE DATABASE medicare_patient_db;
CREATE DATABASE medicare_doctor_db;
CREATE DATABASE medicare_appointment_db;
CREATE DATABASE medicare_medical_record_db;
CREATE DATABASE medicare_pharmacy_db;
```

## Startup Order (IMPORTANT)

Start services in the following order:

### 1. Config Server (Port 8888)
```bash
cd config-server
./gradlew bootRun
```
Verify: http://localhost:8888/patient-service/default

### 2. Discovery Server (Port 8761)
```bash
cd discovery-server
./gradlew bootRun
```
Verify: http://localhost:8761 (Eureka Dashboard)

### 3. Microservices (Start in any order)
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

## Verification

1. **Eureka Dashboard**: http://localhost:8761
   - Should show all 5 services registered: PATIENT-SERVICE, DOCTOR-SERVICE, APPOINTMENT-SERVICE, MEDICAL-RECORD-SERVICE, PHARMACY-SERVICE

2. **Config Server**: http://localhost:8888/patient-service/default
   - Should return configuration from Git repository

3. **API Endpoints** (test with Postman):
   - Patient Service: http://localhost:8081/api/patients
   - Doctor Service: http://localhost:8082/api/doctors
   - Appointment Service: http://localhost:8083/api/appointments
   - Medical Record Service: http://localhost:8084/api/medical-records
   - Pharmacy Service: http://localhost:8085/api/medicines

## Configuration

All service configurations are stored in `medicare-config-repo/` Git repository:
- patient-service.yml
- doctor-service.yml
- appointment-service.yml
- medical-record-service.yml
- pharmacy-service.yml

Each contains:
- Database configuration
- JPA/Hibernate settings
- Eureka Client configuration (service URL, prefer-ip-address)

## Key Features Implemented

1. **Config Server with Git Backend**: Centralized configuration management with version control
2. **Service Discovery (Eureka)**: Services register themselves and discover each other by name
3. **Config Client**: Each microservice fetches configuration from Config Server on startup
4. **Eureka Client**: Each microservice registers with Eureka Server
5. **Bootstrap Configuration**: Using bootstrap.yml to connect to Config Server before application context loads

## Architecture Flow

```
Config Server (8888) ← Git Repo (medicare-config-repo)
       ↑
       | fetches config on startup
Discovery Server (8761) ← Eureka Registry
       ↑
       | registers & discovers
Microservices (8081-8085) → Eureka Dashboard
```

## Screenshots

Place Eureka Dashboard screenshots in `screenshots/` directory.

## Troubleshooting

1. **Config Server can't clone repo**: Ensure Git is installed and the file:// URI is correct
2. **Services can't register with Eureka**: Check Discovery Server is running first
3. **Database connection failed**: Verify MySQL is running and databases exist
4. **Port conflicts**: Ensure ports 8761, 8888, 8081-8085 are available