package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
	List<Room> findByHotel(Hotel hotel);
}
