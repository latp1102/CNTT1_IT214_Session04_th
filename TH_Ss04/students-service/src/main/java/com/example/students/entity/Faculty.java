package com.example.students.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "faculties")
@Data
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String name;
}
