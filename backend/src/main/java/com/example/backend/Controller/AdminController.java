package com.example.backend.Controller;

import com.example.backend.dto.HotelDTO;
import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.service.AdminService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("/update/{email}")
    public ResponseEntity<String> update(@PathVariable("email") String email) {
        try {
            adminService.update(email);
            return ResponseEntity.ok("암호화 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("암호화 처리 에러: " + e.getMessage());
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return adminService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    // 호텔 관리자 생성
    //  시스템 관리자인지 확인 후 생성
    @PostMapping("/auth/hotelAdmin-create/{hotelId}")
    public ResponseEntity<?> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody User user,
            @PathVariable Long hotelId) {
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            // 권한 확인 및 호텔 관리자 생성
            return adminService.join(user, hotelId, adminUserId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("생성 실패: " + e.getMessage());
        }
    }






}
