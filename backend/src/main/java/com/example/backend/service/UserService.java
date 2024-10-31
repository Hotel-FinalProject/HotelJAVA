package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** 회원가입 서비스 부분 */
    public ResponseEntity<?> signup(User user){
        // 정규식
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
        String emailPattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        String phonePattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        String localPhonePattern = "^\\d{3}-\\d{3,4}-\\d{4}$";


        // 이메일 유효성 검사
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        } else if(!user.getEmail().matches(emailPattern)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 이메일 형식입니다.");
        }

        // 비밀번호 유효성 검사
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 입력되지 않았습니다.");
        } else if (!user.getPassword().matches(passwordPattern)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 4~12자, 영어 대/소문자 및 특수문자를 포함해야 합니다.");
        }

        // 전화번호 유효성 검사
        if(!user.getPhone().matches(phonePattern) || !user.getPhone().matches(localPhonePattern)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 전화번호 형식입니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    /** oauth 회원가입 및 로그인 */
    public User OAuthPostLogin(String email, String name) {
        Optional<User> existUser = userRepository.findByEmail(email);

        User user;
        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            // 임의의 비밀번호 설정 (UUID 사용)
            String randomPassword = UUID.randomUUID().toString();
            newUser.setPassword(passwordEncoder.encode(randomPassword));
            newUser.setRole("ROLE_USER");
            user = userRepository.save(newUser);
        } else {
            user = existUser.get();
        }
        return user;
    }

    /** 로그인 처리 및 JWT 발급 */
    public ResponseEntity<?> login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                // 비밀번호가 일치하면 JWT 생성
                try {
                    String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(),user.getName(), user.getRole());
                    return ResponseEntity.ok(token);
                } catch (JOSEException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
