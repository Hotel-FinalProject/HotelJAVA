package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /** 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        return userService.signup(user);
    }

    /** 사이트 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return userService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /** 이메일 중복 확인 */
    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email){
        boolean exists = userRepository.existsByEmail(email);
        if(exists){
            return ResponseEntity.status(409).body("이미 사용중인 이메일 입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일 입니다.");
        }
    }
}
