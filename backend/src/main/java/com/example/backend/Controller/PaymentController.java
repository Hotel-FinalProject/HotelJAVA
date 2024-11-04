//package com.example.backend.Controller;
//
//import com.example.backend.dto.PaymentDTO;
//import com.example.backend.entity.Payment;
//import com.example.backend.entity.Reservation;
//import com.example.backend.entity.User;
//import com.example.backend.service.PaymentService;
//import com.example.backend.service.ReservationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth/payment")
//public class PaymentController {
//
//    private final PaymentService paymentService;
//    private final ReservationService reservationService;
//
//    @Autowired
//    public PaymentController(PaymentService paymentService, ReservationService reservationService) {
//        this.paymentService = paymentService;
//        this.reservationService = reservationService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Payment> processPayment(
//            @AuthenticationPrincipal User user, // 로그인한 사용자 정보
//            @RequestBody PaymentDTO paymentRequest) {
//
//        // 예약 정보 가져오기
//        Reservation reservation = reservationService.findById(paymentRequest.getReservationId())
//                .orElseThrow(() -> new RuntimeException("Reservation not found"));
//
//        // 결제 처리
//        Payment payment = paymentService.processPayment(reservation, paymentRequest.getMethod(),
//                paymentRequest.getAmount(), paymentRequest.getTransactionId());
//
//        return ResponseEntity.ok(payment);
//    }
//}
