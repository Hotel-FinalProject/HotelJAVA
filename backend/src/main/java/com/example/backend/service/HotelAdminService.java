package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                if (user.getRole() != null && user.getRole().equals("ROLE_HOTELADMIN")) {
                    // 비밀번호가 일치하면 JWT 생성
                    try {
                        String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(),user.getName(), user.getRole());

                        user.setLastLoginTime(LocalDateTime.now());
                        userRepository.save(user);
                        // JSON 형태로 응답하기 위해 Map 사용
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", token);
                        response.put("userId", user.getUserId());
                        response.put("name", user.getName());
                        response.put("email", user.getEmail());
                        response.put("role",user.getRole());

                        return ResponseEntity.ok(response);
                    } catch (JOSEException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
                    }
                }
                else {
                    // 관리자 권한이 없는 경우
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("호텔 관리자 권한이 없습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @Transactional
    public void updatePassword(Long adminUserId, LoginRequest loginRequest) throws IllegalAccessException {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin user ID"));

        if (!"ROLE_HOTELADMIN".equals(adminUser.getRole())) {
            throw new IllegalAccessException("호텔 관리자 계정이 아닙니다.");
        }

        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
        String newPassword = loginRequest.getPasswd();
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalAccessException("새 비밀번호가 입력되지 않았습니다.");
        } else if (!newPassword.matches(passwordPattern)) {
            throw new IllegalAccessException("비밀번호는 4~12자, 영어 대/소문자 및 특수문자를 포함해야 합니다.");
        }

        // 비밀번호 암호화 및 저장
        adminUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(adminUser);
    }
}
