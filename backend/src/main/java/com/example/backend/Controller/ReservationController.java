package com.example.backend.Controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.entity.Payment;
import com.example.backend.entity.User;
import com.example.backend.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//public class ReservationController {
//
//    @Autowired
//    private  ReservationService reservationService;
//
//
//    @PostMapping("/reservation")
//    public ResponseEntity<Payment> createReservation(
//            @AuthenticationPrincipal User user, // 로그인한 사용자 정보
//            @RequestBody ReservationDTO reservationDTO) {
//
//        Payment payment = reservationService.createReservationAndProcessPayment(user, reservationDTO);
//        return ResponseEntity.ok(payment);
//    }
//}

public class ReservationController {

    @Autowired

    private  PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> requestBody) {
        String impUid = requestBody.get("imp_uid");

        try {
            // 아임포트 토큰 발급
            String token = paymentService.getAuthToken();

            // 결제 검증 요청
            Map<String, Object> paymentData = paymentService.verifyPayment(impUid, token);

            if (paymentData != null && paymentData.get("status").equals("paid")) {
                // 결제 완료된 상태
                return ResponseEntity.ok("결제가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 검증에 실패했습니다.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 확인 중 오류 발생: " + e.getMessage());
        }
    }
}
