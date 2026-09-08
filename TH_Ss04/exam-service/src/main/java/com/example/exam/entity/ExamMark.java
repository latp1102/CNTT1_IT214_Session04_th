package com.example.exam.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "exam_marks")
@Data
public class ExamMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String studentCode;
    
    @Column(nullable = false)
    private Long subjectId;
    
    @Column(nullable = false)
    private Double midTermMark;
    
    @Column(nullable = false)
    private Double finalMark;
    
    @Column(nullable = false)
    private LocalDate examDate;
}
