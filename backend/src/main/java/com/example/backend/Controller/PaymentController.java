package com.example.backend.Controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.RoomCountDTO;
import com.example.backend.dto.RoomDTO;
import com.example.backend.service.PaymentService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserController userController;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/paymentCancel/{imp_uid}")
    public ResponseEntity<?> cancel(
            @PathVariable("imp_uid") String imp_uid,
            @RequestBody RoomDTO roomDTO,
            @RequestHeader("Authorization") String token) {

        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            if (imp_uid != null) {
                Long roomId = roomDTO.getRoomId();
                paymentService.cancel(userId,imp_uid,roomId);
                return ResponseEntity.ok("결제 취소 완료");
            } else {
                return ResponseEntity.badRequest().body("결제 검증 실패");
            }
        } catch (Exception e) {
            // 예외 발생 시 예외 메시지와 함께 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }
}
