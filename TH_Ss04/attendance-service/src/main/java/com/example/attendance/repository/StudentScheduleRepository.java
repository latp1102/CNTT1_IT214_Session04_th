package com.example.attendance.repository;

import com.example.attendance.entity.StudentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentScheduleRepository extends JpaRepository<StudentSchedule, Long> {
}
