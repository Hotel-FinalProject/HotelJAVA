package com.example.backend.Controller;


import com.example.backend.dto.FavoriteDTO;
import com.example.backend.service.FavoriteService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/favorites")
public class FavoritesController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FavoriteService favoriteService;

    //찜하기
    @PostMapping("/{hotelId}")
    public ResponseEntity<?> favorites(@RequestHeader("Authorization") String token, @PathVariable("hotelId") Long hotelId) {
        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            return favoriteService.wish(userId, hotelId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 오류: " + e.getMessage());
        }
    }

    // 찜 취소
    @PostMapping("/cancel/{hotelId}")
    public ResponseEntity<?> favoritesCancel(@RequestHeader("Authorization") String token, @PathVariable("hotelId") Long hotelId) {
        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            return favoriteService.wishCancel(userId, hotelId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 오류: " + e.getMessage());
        }
    }

    // 찜 상태 확인
    @GetMapping("/status/{hotelId}")
    public ResponseEntity<?> getFavoriteStatus(@RequestHeader("Authorization") String token, @PathVariable Long hotelId) {
        try{
            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            return favoriteService.getFavoriteStatus(userId, hotelId);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 오류: " + e.getMessage());
        }
    }

    // 로그인 사용자의 찜한 호텔 조회
    @GetMapping("/favoriteInfo")
    public ResponseEntity<?> getReservationInfo(@RequestHeader("Authorization") String token) {
        try {

            String actualToken = token.replace("Bearer ", "");

            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }
            List<FavoriteDTO> favoriteDTOS = favoriteService.getFavoritesInfo(userId);
            return ResponseEntity.ok(favoriteDTOS);

        } catch (Exception e) {
            // 예외 발생 시 예외 메시지와 함께 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" 오류: " + e.getMessage());
        }

    }
}