package com.example.schedules.repository;

import com.example.schedules.entity.RoomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Long> {
}
