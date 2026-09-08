package com.example.subjects.controller;

import com.example.subjects.entity.Subject;
import com.example.subjects.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
    
    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }
    
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id).orElse(null);
    }
}
