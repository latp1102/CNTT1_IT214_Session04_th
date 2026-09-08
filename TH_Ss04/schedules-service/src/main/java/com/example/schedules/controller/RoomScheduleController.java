package com.example.schedules.controller;

import com.example.schedules.entity.RoomSchedule;
import com.example.schedules.repository.RoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-schedules")
public class RoomScheduleController {
    
    @Autowired
    private RoomScheduleRepository roomScheduleRepository;
    
    @GetMapping
    public List<RoomSchedule> getAllRoomSchedules() {
        return roomScheduleRepository.findAll();
    }
    
    @PostMapping
    public RoomSchedule createRoomSchedule(@RequestBody RoomSchedule roomSchedule) {
        return roomScheduleRepository.save(roomSchedule);
    }
    
    @GetMapping("/{id}")
    public RoomSchedule getRoomScheduleById(@PathVariable Long id) {
        return roomScheduleRepository.findById(id).orElse(null);
    }
}
