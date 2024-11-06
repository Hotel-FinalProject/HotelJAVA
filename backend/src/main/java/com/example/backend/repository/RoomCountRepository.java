package com.example.backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;

public interface RoomCountRepository extends JpaRepository<RoomCount, Long> {
	
    Optional<RoomCount> findByRoomAndDate(Room room, LocalDate date);

}
