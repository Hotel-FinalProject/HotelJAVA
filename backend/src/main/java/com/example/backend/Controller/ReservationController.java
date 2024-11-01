//package com.example.backend.Controller;
//
//import com.example.backend.entity.Payment;
//import com.example.backend.entity.User;
//import com.example.backend.service.PaymentService;
//import com.example.backend.service.ReservationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/reservations")
//public class ReservationController {
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private PaymentService paymentService;
//
//
//
//    @PostMapping("/{roomId}")
//    public ResponseEntity<String> reserveRoom(@PathVariable Long roomId, @RequestBody Map<String, Object> paymentData, @AuthenticationPrincipal User user) throws IOException {
//        String impUid = (String) paymentData.get("imp_uid");
//        BigDecimal amount = new BigDecimal((String) paymentData.get("amount"));
//
//        Payment payment = paymentService.verifyPayment(impUid);
//
//        if (payment == null || !paymentService.isPaymentAmountValid(payment, amount)) {
//            return ResponseEntity.badRequest().body("Payment validation failed.");
//        }
//
//        reservationService.createReservation(roomId, payment, user);
//        return ResponseEntity.ok("Reservation and payment successful.");
//    }
//}
