package com.example.backend.repository;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Object> findByRoomsRoomId(Long roomId);

    List<Reservation> findByUser(User user);

    List<Reservation> findByRooms_Hotel(Hotel hotel);
}
