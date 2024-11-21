package com.example.backend.Controller;

import com.example.backend.dto.AdminUserDTO;
import com.example.backend.dto.LoginRequest;
import com.example.backend.service.HotelAdminService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hotelAdmin")
public class HotelAdminController {

    @Autowired
    private HotelAdminService hotelAdminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return hotelAdminService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    @PutMapping("/auth/update-password")
    public ResponseEntity<?> readUser (@RequestHeader("Authorization") String token, @RequestBody LoginRequest loginRequest){
        try {
            // 토큰에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long adminUserId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            if (adminUserId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
            }

            hotelAdminService.updatePassword(adminUserId,loginRequest);
            return ResponseEntity.ok("비밀번호 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("변경 실패: " + e.getMessage());
        }
    }


}
