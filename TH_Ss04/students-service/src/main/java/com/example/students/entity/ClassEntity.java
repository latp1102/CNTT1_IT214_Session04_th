package com.example.students.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "classes")
@Data
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}
