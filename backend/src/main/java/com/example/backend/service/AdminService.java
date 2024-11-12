package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void update(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().startsWith("$2a$")) { // BCrypt로 암호화된 비밀번호는 "$2a$"로 시작함
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Password is already encoded with BCrypt.");
        }
    }

    public ResponseEntity<?> login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                if (user.getRole() != null && user.getRole().equals("ROLE_ADMIN")) {
                    // 비밀번호가 일치하면 JWT 생성
                    try {
                        String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(),user.getName(), user.getRole());

                        // JSON 형태로 응답하기 위해 Map 사용
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", token);
                        response.put("userId", user.getUserId());
                        response.put("name", user.getName());
                        response.put("email", user.getEmail());

                        return ResponseEntity.ok(response);
                    } catch (JOSEException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
                    }
                }
                else {
                    // 관리자 권한이 없는 경우
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 없습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
