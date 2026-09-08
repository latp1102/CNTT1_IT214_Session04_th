package com.example.subjects.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "semesters")
@Data
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer year;
}
