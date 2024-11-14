package com.example.backend.service;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.User;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.example.backend.util.PasswordGenerator;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private HotelRepository hotelRepository;


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
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 없습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 호텔 관리자 생성
    public ResponseEntity<?> join(User user, Long hotelId, Long adminUserId) {
        // 관리자 권한 확인
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin user ID"));

        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 없습니다.");
        }

        // 이메일 중복확인
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        // 이메일 유효성 검사
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 입력되지 않았습니다.");
        } else if (!user.getEmail().matches(emailPattern)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 유효성 일치하지 않습니다.");
        }

        // 비밀번호 생성
        String rawPassword = passwordGenerator.generateRandomPassword(8);
        user.setPassword(rawPassword);

        // 호텔 정보 가져오기
        Hotel hotelInfo = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));

        // 사용자 정보 설정
        user.setName(hotelInfo.getName());
        user.setPhone(hotelInfo.getHotelnum());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("ROLE_HOTELADMIN");
        user.setLoginType("normal");


        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다. 비밀번호: " + rawPassword);
    }

}
