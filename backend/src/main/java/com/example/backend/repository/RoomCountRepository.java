package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCountRepository extends JpaRepository<RoomCount, Long> {
    
    Optional<RoomCount> findByRoomAndDate(Room room, LocalDate now);

    Optional<RoomCount> findRoomCountByRoomRoomIdAndDate(Long roomId, LocalDate checkInDate);
    
    List<RoomCount> findByRoomAndDateBetween(Room room, LocalDate startDate, LocalDate endDate);
}
