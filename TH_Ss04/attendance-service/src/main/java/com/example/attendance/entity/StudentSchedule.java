package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "student_schedules")
@Data
public class StudentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String studentCode;
    
    @Column(nullable = false)
    private Long subjectId;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private Boolean present;
}
