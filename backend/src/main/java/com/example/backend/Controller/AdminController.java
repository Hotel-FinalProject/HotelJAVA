package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.service.AdminService;
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
    


}
