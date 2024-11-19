package com.example.backend.service;

import com.example.backend.dto.ReservationDateDTO;
import com.example.backend.dto.ReservationTodayDTO;
import com.example.backend.dto.UserReservationDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                    .paymentStatus(reservation.getPayment().getStatus())
                    .build();
            userReservationDTOList.add(userReservationDTO);
        }
        return userReservationDTOList;

    }

    public List<ReservationDateDTO> getReservationDate(LocalDate today, Long adminUserId) throws IllegalAccessException {

        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin user ID"));


        Hotel hotel = adminUser.getHotel();
        if (hotel == null) {
            throw new RuntimeException("이 관리자에게 연결된 호텔이 없습니다.");
        }

        if (!"ROLE_HOTELADMIN".equals(adminUser.getRole())) {
            throw new IllegalAccessException("호텔 관리자 계정이 아닙니다.");
        }

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
    
    // 오늘 날짜의 예약 정보 조회
    public List<ReservationTodayDTO> getTodayReservations(Long hotelId, LocalDate today) {
    	List<Reservation> reservations = reservationRepository.findReservationsForToday(hotelId, today);

    	return reservations.stream()
    		    .filter(reservation -> reservation.getRooms() != null && reservation.getUser() != null)
    		    .map(reservation -> {
    		        try {
    		            return ReservationTodayDTO.builder()
    		                .roomName(reservation.getRooms().getName())
    		                .userName(reservation.getUser().getName())
    		                .phone(reservation.getUser().getPhone())
    		                .request(reservation.getRequest())
    		                .checkIn(reservation.getCheckIn())
    		                .checkOut(reservation.getCheckOut())
    		                .guest(reservation.getGuestNum())
    		                .status(reservation.getStatus())
    		                .build();
    		        } catch (Exception e) {
    		            // 로그를 남기고 건너뛰기
    		            return null;
    		        }
    		    })
    		    .filter(dto -> dto != null)
    		    .collect(Collectors.toList());
    }
}
