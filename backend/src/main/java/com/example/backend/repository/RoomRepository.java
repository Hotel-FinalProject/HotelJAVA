package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.dto.RoomCountDTO;
import com.example.backend.dto.RoomSummaryDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
	List<Room> findByHotel(Hotel hotel);
	
//	@Query("SELECT new com.example.backend.dto.RoomSummaryDTO(r.name, rc.roomCount) " +
//		       "FROM Room r " +
//		       "JOIN RoomCount rc ON r.roomId = rc.room.roomId " +
//		       "WHERE r.hotel.hotelId = :hotelId AND rc.date = :date")
//		List<RoomSummaryDTO> findRoomSummaryByHotelAndDate(
//		    @Param("hotelId") Long hotelId, 
//		    @Param("date") LocalDate date);
	@Query("SELECT new com.example.backend.dto.RoomSummaryDTO(r.name, rc.roomCount, r) " +
		       "FROM Room r " +
		       "JOIN RoomCount rc ON r.roomId = rc.room.roomId " +
		       "WHERE r.hotel.hotelId = :hotelId AND rc.date = :date")
		List<RoomSummaryDTO> findRoomSummaryByHotelAndDate(
		    @Param("hotelId") Long hotelId,
		    @Param("date") LocalDate date
		);


}
