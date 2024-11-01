package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        return userService.signup(user);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

}


