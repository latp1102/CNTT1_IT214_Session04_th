package com.example.attendance.controller;

import com.example.attendance.entity.StudentSchedule;
import com.example.attendance.repository.StudentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-schedules")
public class StudentScheduleController {
    
    @Autowired
    private StudentScheduleRepository studentScheduleRepository;
    
    @GetMapping
    public List<StudentSchedule> getAllStudentSchedules() {
        return studentScheduleRepository.findAll();
    }
    
    @PostMapping
    public StudentSchedule createStudentSchedule(@RequestBody StudentSchedule studentSchedule) {
        return studentScheduleRepository.save(studentSchedule);
    }
    
    @GetMapping("/{id}")
    public StudentSchedule getStudentScheduleById(@PathVariable Long id) {
        return studentScheduleRepository.findById(id).orElse(null);
    }
}
