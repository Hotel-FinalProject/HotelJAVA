package com.example.backend.repository;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Object> findByRoomsRoomId(Long roomId);

    List<Reservation> findByUser(User user);

    List<Reservation> findByRooms_Hotel(Hotel hotel);
    
    @Query("SELECT r FROM Reservation r WHERE r.rooms.hotel.hotelId = :hotelId AND r.checkIn <= :today AND r.checkOut >= :today")
    List<Reservation> findReservationsForToday(
        @Param("hotelId") Long hotelId,
        @Param("today") LocalDate today
    );
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.rooms.roomId = :roomId AND :date BETWEEN r.checkIn AND r.checkOut AND r.status = '예약 완료'")
    int findReservedRoomCountByRoomAndDate(@Param("roomId") Long roomId, @Param("date") LocalDate date);

}
