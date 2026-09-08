# Student Management Microservices System

## Architecture Overview

This is a microservices-based Student Management Application with the following services:

### Infrastructure Services
1. **config-service** (Port 8888) - Centralized configuration management
2. **eureka-service** (Port 8761) - Service discovery and registration

### Business Services
3. **students-service** (Port 8081) - Student, Class, and Faculty management
   - Tables: faculties, classes, students
   - Database: students_db

4. **subjects-service** (Port 8082) - Subject and Semester management
   - Tables: semesters, subjects
   - Database: subjects_db

5. **schedules-service** (Port 8083) - Room and Room Schedule management
   - Tables: rooms, room_schedules
   - Database: schedules_db

6. **attendance-service** (Port 8084) - Student attendance tracking
   - Tables: student_schedules
   - Database: attendance_db

7. **exam-service** (Port 8085) - Exam marks management
   - Tables: exam_marks
   - Database: exam_db

## Prerequisites

- Java 17 or higher
- MySQL Server (running on localhost:3306)
- Gradle 8.x

## Database Setup

Ensure MySQL is running with the following credentials:
- Username: root
- Password: 123456

The databases will be created automatically when services start (using `createDatabaseIfNotExist=true`).

## Configuration Files

Each service has its own `application.yml` with:
- Server port configuration
- Database connection settings
- Eureka client configuration
- Config server connection (via bootstrap.yml)

## Starting the Services

### Option 1: Start services individually

1. **Start Eureka Service** (must start first)
   ```bash
   cd eureka-service
   ../gradlew bootRun
   ```

2. **Start Config Service** (must start second)
   ```bash
   cd config-service
   ../gradlew bootRun
   ```

3. **Start Business Services** (in any order)
   ```bash
   cd students-service
   ../gradlew bootRun
   
   cd subjects-service
   ../gradlew bootRun
   
   cd schedules-service
   ../gradlew bootRun
   
   cd attendance-service
   ../gradlew bootRun
   
   cd exam-service
   ../gradlew bootRun
   ```

### Option 2: Build all services
```bash
./gradlew build
```

## Testing the Services

### Students Service
- GET http://localhost:8081/api/students
- POST http://localhost:8081/api/students
- GET http://localhost:8081/api/students/{id}

### Subjects Service
- GET http://localhost:8082/api/subjects
- POST http://localhost:8082/api/subjects
- GET http://localhost:8082/api/subjects/{id}

### Schedules Service
- GET http://localhost:8083/api/room-schedules
- POST http://localhost:8083/api/room-schedules
- GET http://localhost:8083/api/room-schedules/{id}

### Attendance Service
- GET http://localhost:8084/api/student-schedules
- POST http://localhost:8084/api/student-schedules
- GET http://localhost:8084/api/student-schedules/{id}

### Exam Service
- GET http://localhost:8085/api/exam-marks
- POST http://localhost:8085/api/exam-marks
- GET http://localhost:8085/api/exam-marks/{id}

## Eureka Dashboard

Access the Eureka dashboard at: http://localhost:8761

You should see all registered services after they start successfully.

## Technology Stack

- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Data JPA
- MySQL
- Netflix Eureka
- Spring Cloud Config
- Lombok
- Gradle

## Database Schema

### Students Service
- **faculties**: id, code, name
- **classes**: id, code, name, faculty_id
- **students**: id, student_code, first_name, last_name, email, class_id

### Subjects Service
- **semesters**: id, code, name, year
- **subjects**: id, code, name, credits, semester_id

### Schedules Service
- **rooms**: id, code, name, capacity
- **room_schedules**: id, room_id, day_of_week, start_time, end_time

### Attendance Service
- **student_schedules**: id, student_code, subject_id, date, present

### Exam Service
- **exam_marks**: id, student_code, subject_id, mid_term_mark, final_mark, exam_date
