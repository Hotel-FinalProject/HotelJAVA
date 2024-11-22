package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationDateDTO;
import com.example.backend.dto.ReservationSummaryDTO;
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
                    .reservationId(reservation.getReservationId())
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
    public List<ReservationSummaryDTO> getReservationSummary(Long adminUserId) throws IllegalAccessException {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin user ID"));

        Hotel hotel = adminUser.getHotel();
        if (hotel == null) {
            throw new RuntimeException("이 관리자에게 연결된 호텔이 없습니다.");
        }

        if (!"ROLE_HOTELADMIN".equals(adminUser.getRole())) {
            throw new IllegalAccessException("호텔 관리자 계정이 아닙니다.");
        }

        LocalDate today = LocalDate.now();

        // 예약 요약 정보를 새로운 DTO로 생성
        List<Reservation> reservations = reservationRepository.findByRooms_Hotel(hotel);
        return reservations.stream()
                .filter(reservation -> 
                    !reservation.getCheckIn().isAfter(today) && !reservation.getCheckOut().isBefore(today)
                )
                .<ReservationSummaryDTO>map(reservation -> ReservationSummaryDTO.builder()
                        .roomName(reservation.getRooms() != null ? reservation.getRooms().getName() : "N/A")
                        .userName(reservation.getUser() != null ? reservation.getUser().getName() : "Unknown")
                        .userPhone(reservation.getUser() != null ? reservation.getUser().getPhone() : "없음")
                        .checkIn(reservation.getCheckIn())
                        .checkOut(reservation.getCheckOut())
                        .request(reservation.getRequest())
                        .guest(reservation.getGuestNum())
                        .status(reservation.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }

    
    // 예약 관리 수정 (D)
    public ReservationUpdateDTO updateReservation(Long reservationId, ReservationUpdateDTO reservationUpdateDTO, Long adminUserId) {
        // 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 관리자 권한 확인
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

        if (!"ROLE_HOTELADMIN".equals(adminUser.getRole())) {
            throw new RuntimeException("이 작업을 수행할 권한이 없습니다.");
        }

        // 예약 상태 및 요청 사항 업데이트
        reservation.setStatus(reservationUpdateDTO.getStatus());
        reservation.setRequest(reservationUpdateDTO.getRequest());

        // 체크인, 체크아웃 날짜 검증 및 업데이트
        if (reservationUpdateDTO.getCheckIn() != null && reservationUpdateDTO.getCheckOut() != null) {
            if (reservationUpdateDTO.getCheckIn().isAfter(reservationUpdateDTO.getCheckOut())) {
                throw new RuntimeException("체크인 날짜는 체크아웃 날짜보다 이전이어야 합니다.");
            }
            reservation.setCheckIn(reservationUpdateDTO.getCheckIn());
            reservation.setCheckOut(reservationUpdateDTO.getCheckOut());
        }

        // 저장
        reservationRepository.save(reservation);

        // 수정된 예약 데이터를 반환
        return new ReservationUpdateDTO(
                reservation.getStatus(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getRequest()
        );
    }





    // D
    public List<ReservationSummaryDTO> getAllReservations(Long adminUserId) {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin user ID"));

        Hotel hotel = adminUser.getHotel();
        if (hotel == null) {
            throw new RuntimeException("이 관리자에게 연결된 호텔이 없습니다.");
        }

        List<Reservation> reservations = reservationRepository.findByRooms_Hotel(hotel);
        return reservations.stream()
                .map(reservation -> ReservationSummaryDTO.builder()
                        .roomName(reservation.getRooms() != null ? reservation.getRooms().getName() : "N/A")
                        .userName(reservation.getUser() != null ? reservation.getUser().getName() : "Unknown")
                        .userPhone(reservation.getUser() != null ? reservation.getUser().getPhone() : "없음")
                        .checkIn(reservation.getCheckIn())
                        .checkOut(reservation.getCheckOut())
                        .request(reservation.getRequest())
                        .guest(reservation.getGuestNum())
                        .status(reservation.getStatus())
                        .build())
                .collect(Collectors.toList());
    }


    
    

}
