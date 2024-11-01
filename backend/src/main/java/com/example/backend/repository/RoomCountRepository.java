package com.example.backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;

public interface RoomCountRepository extends JpaRepository<RoomCount, Long> {
	
	// 날짜와 roomId를 기준으로 RoomCount 찾기
    Optional<RoomCount> findByRoomAndDate(Room room, LocalDate date);
}
