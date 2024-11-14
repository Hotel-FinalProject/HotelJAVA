package com.example.backend.Controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationDateDTO;
import com.example.backend.dto.RoomCountDTO;
import com.example.backend.dto.UserReservationDTO;
import com.example.backend.entity.Room;
import com.example.backend.entity.User;
import com.example.backend.service.PaymentService;
import com.example.backend.service.ReservationService;
import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class ReservationController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserController userController;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/reservation/{imp_uid}")
    public ResponseEntity<?> verifyPayment(
            @PathVariable("imp_uid") String imp_uid,
            @RequestBody ReservationDTO reservationDTO,
            RoomCountDTO roomCountDTO,
            @RequestHeader("Authorization") String token) {

        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            if (imp_uid != null) {
                paymentService.verifyPayment(userId,imp_uid, reservationDTO,roomCountDTO);
                return ResponseEntity.ok("결제 완료");
            } else {
                return ResponseEntity.badRequest().body("결제 검증 실패");
            }
        } catch (Exception e) {
            // 예외 발생 시 예외 메시지와 함께 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요. 오류: " + e.getMessage());
        }
    }

    //로그인한 유저의 예약 조회
    @GetMapping("/reservationInfo")
    public ResponseEntity<?> getReservationInfo(@RequestHeader("Authorization") String token) {
        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<UserReservationDTO> userReservationDTOList = reservationService.getReservationInfo(userId);
            return ResponseEntity.ok(userReservationDTOList);

        } catch (Exception e) {
            // 예외 발생 시 예외 메시지와 함께 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 오류: " + e.getMessage());
        }

    }

    // 날짜별 객실 예약 조회
    @GetMapping("/reservationInfo-Date")
    public ResponseEntity<?> getReservationDate(@RequestParam Long hotelId) {
        try {


            LocalDate today = LocalDate.now();
            List<ReservationDateDTO> reservationDateDTOs = reservationService.getReservationDate(today, hotelId);
            return ResponseEntity.ok(reservationDateDTOs);

        } catch (Exception e) {
            // 예외 발생 시 예외 메시지와 함께 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("오류: " + e.getMessage());
        }
    }





    }