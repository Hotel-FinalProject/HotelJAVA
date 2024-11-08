//package com.example.backend.service;
//
//import com.example.backend.dto.ReservationDTO;
//import com.example.backend.entity.Payment;
//import com.example.backend.entity.Reservation;
//import com.example.backend.entity.Room;
//import com.example.backend.entity.User;
//import com.example.backend.repository.PaymentRepository;
//import com.example.backend.repository.ReservationRepository;
//import com.example.backend.repository.RoomRepository;
//import com.example.backend.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.net.http.HttpHeaders;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class ReservationService {
//
//    @Autowired
//    private  PaymentService paymentService;
//    @Autowired
//    private  ReservationRepository reservationRepository;
//    @Autowired
//    private  RoomRepository roomRepository;
//
//    public Payment createReservationAndProcessPayment(User user, ReservationDTO request) {
//        Room room = roomRepository.findById(request.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        Reservation reservation = new Reservation();
//        reservation.setRoom(room);
//        reservation.setGuestNum(request.getGuestNum());
//        reservation.setCheckIn(request.getCheckIn());
//        reservation.setCheckOut(request.getCheckOut());
//        reservation.setUser(user); // 로그인한 사용자 정보를 설정
//
//        // 결제 정보 (임시로 설정)
//        BigDecimal amount = new BigDecimal("1"); // 임시 금액
//        String paymentMethod = request.getPaymentMethod();
//        Long transactionId = System.currentTimeMillis(); // 임시 거래 ID
//
//        // 예약 및 결제 처리
//        return paymentService.processPayment(reservation, paymentMethod, amount, transactionId);
//    }
//
//    // findById 메서드 추가
//    public Optional<Reservation> findById(Long reservationId) {
//        return reservationRepository.findById(reservationId);
//    }
//
//
//
//}
