package com.example.backend.repository;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.Room;
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
    
    // (D)
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.rooms = :room AND r.checkIn <= :date AND r.checkOut >= :date AND r.status = '예약 완료'")
    long countByRoomAndDate(@Param("room") Room room, @Param("date") LocalDate date);

    
}
