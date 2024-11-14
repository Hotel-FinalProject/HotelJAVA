package com.example.backend.service;

import com.example.backend.dto.ReservationDateDTO;
import com.example.backend.dto.UserReservationDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    HotelRepository hotelRepository;

    public List<UserReservationDTO> getReservationInfo(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        List<UserReservationDTO> userReservationDTOList = new ArrayList<>();

        for (Reservation reservation : reservations) {
            UserReservationDTO userReservationDTO = UserReservationDTO.builder()
                    .hotelName(reservation.getRooms().getHotel().getName())
                    .imp_uid(reservation.getPayment().getTransactionId())
                    .roomName(reservation.getRooms().getName())
                    .roomId(reservation.getRooms().getRoomId())
                    .checkIn(reservation.getCheckIn())
                    .checkOut(reservation.getCheckOut())
                    .guestNum(reservation.getGuestNum())
                    .request(reservation.getRequest())
                    .status(reservation.getStatus())
                    .uid(reservation.getPayment().getTransactionId())
                    .paymentStatus(reservation.getPayment().getStatus())
                    .build();
            userReservationDTOList.add(userReservationDTO);
        }
        return userReservationDTOList;

    }

    public List<ReservationDateDTO> getReservationDate(LocalDate today, Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("호텔을 찾을 수 없습니다."));

        // 해당 호텔에 대한 모든 예약 조회
        List<Reservation> reservations = reservationRepository.findByRooms_Hotel(hotel);
        List<ReservationDateDTO> reservationDateDTOS = new ArrayList<>();

        for (Reservation reservation : reservations) {

            if ("예약 완료".equals(reservation.getStatus())) {

                if (!reservation.getCheckIn().isAfter(today) && !reservation.getCheckOut().isBefore(today)) {

                    ReservationDateDTO reservationDateDTO = ReservationDateDTO.builder()
                            .roomName(reservation.getRooms().getName())
                            .userName(reservation.getUser().getName())
                            .checkIn(reservation.getCheckIn())
                            .checkOut(reservation.getCheckOut())
                            .request(reservation.getRequest())
                            .guest(reservation.getGuestNum())
                            .status(reservation.getStatus())
                            .build();

                    reservationDateDTOS.add(reservationDateDTO);
                }
            }
        }

        return reservationDateDTOS;
    }

}
