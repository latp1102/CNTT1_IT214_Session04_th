package com.example.exam.controller;

import com.example.exam.entity.ExamMark;
import com.example.exam.repository.ExamMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-marks")
public class ExamMarkController {
    
    @Autowired
    private ExamMarkRepository examMarkRepository;
    
    @GetMapping
    public List<ExamMark> getAllExamMarks() {
        return examMarkRepository.findAll();
    }
    
    @PostMapping
    public ExamMark createExamMark(@RequestBody ExamMark examMark) {
        return examMarkRepository.save(examMark);
    }
    
    @GetMapping("/{id}")
    public ExamMark getExamMarkById(@PathVariable Long id) {
        return examMarkRepository.findById(id).orElse(null);
    }
}
