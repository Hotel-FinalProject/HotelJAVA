package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationDateDTO;
import com.example.backend.dto.ReservationUpdateDTO;
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
    
    // 예약 요약 정보 (D)
    public List<ReservationDateDTO> getReservationSummary(Long adminUserId) throws IllegalAccessException {
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
//        List<Reservation> reservations = reservationRepository.findByRooms_Hotel(hotel);
        
        // 오늘 날짜만
        LocalDate today = LocalDate.now();

        // 예약 요약 정보 생성
//        return reservations.stream().map(reservation -> ReservationDateDTO.builder()
//                .roomName(reservation.getRooms().getName())
//                .userName(reservation.getUser().getName())
//                .checkIn(reservation.getCheckIn())
//                .checkOut(reservation.getCheckOut())
//                .request(reservation.getRequest())
//                .guest(reservation.getGuestNum())
//                .status(reservation.getStatus())
//                .build()).collect(Collectors.toList());
        
        // 오늘 날짜에 해당하는 예약만 필터링
        return reservationRepository.findByRooms_Hotel(hotel).stream()
                .filter(reservation -> 
                    !reservation.getCheckIn().isAfter(today) && !reservation.getCheckOut().isBefore(today)
                )
                .map(reservation -> ReservationDateDTO.builder()
                        .roomName(reservation.getRooms().getName())
                        .userName(reservation.getUser().getName())
                        .checkIn(reservation.getCheckIn())
                        .checkOut(reservation.getCheckOut())
                        .request(reservation.getRequest())
                        .guest(reservation.getGuestNum())
                        .status(reservation.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
    
    // 예약 관리 수정 (D)
    public ReservationDTO updateReservation(Long reservationId, ReservationUpdateDTO reservationUpdateDTO, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 유효성 검사: 요청한 사용자가 예약 소유자인지 확인
        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 예약만 수정할 수 있습니다.");
        }

        // 예약 정보 업데이트
        reservation.setStatus(reservationUpdateDTO.getStatus());
        reservation.setCheckIn(reservationUpdateDTO.getCheckIn());
        reservation.setCheckOut(reservationUpdateDTO.getCheckOut());
        reservation.setRequest(reservationUpdateDTO.getRequest());

        Reservation updatedReservation = reservationRepository.save(reservation);

        // DTO로 변환하여 반환
        return new ReservationDTO(
            updatedReservation.getReservationId(),
            updatedReservation.getGuestNum(),
            updatedReservation.getCheckIn(),
            updatedReservation.getCheckOut(),
            updatedReservation.getRequest(),
            updatedReservation.getCreateDate(),
            updatedReservation.getUpdateDate(),
            updatedReservation.getRooms().getRoomId()
        );
    }

}
