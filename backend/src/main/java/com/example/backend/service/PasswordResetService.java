package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.example.backend.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** 비밀번호 재설정 및 이메일 발송 */
    public ResponseEntity<?> resetPasswordWithGeneratedPassword(String email, String token) {
        log.info("비밀번호 재설정 요청 - 이메일: {}", email);

        // 토큰 검증
        try {
            String verifiedEmail = jwtUtil.verifyJwt(token);
            if (!verifiedEmail.equals(email)) {
                log.warn("인증된 이메일과 요청 이메일이 일치하지 않음 - 인증된 이메일: {}, 요청된 이메일: {}", verifiedEmail, email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 이메일과 일치하지 않습니다.");
            }
            log.info("토큰 검증 성공 - 이메일: {}", verifiedEmail);
        } catch (Exception e) {
            log.error("토큰 검증 실패 - 이메일: {}", email, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 인증 토큰입니다.");
        }

        // 사용자 조회
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("사용자 조회 실패 - 이메일: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 임시 비밀번호 생성
        String temporaryPassword = PasswordGenerator.generateRandomPassword(8);
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
        if (!temporaryPassword.matches(passwordPattern)) {
            log.error("생성된 임시 비밀번호가 패턴에 맞지 않음 - 비밀번호: {}", temporaryPassword);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 4~12자, 영어 대/소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        // 임시 비밀번호 암호화 및 저장
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);
        log.info("임시 비밀번호 저장 완료 - 이메일: {}", email);

        // 이메일 발송 (임시 비밀번호 포함)
        try {
            userService.sendPasswordResetEmail(user.getEmail(), temporaryPassword);
            log.info("비밀번호 재설정 이메일 발송 성공 - 이메일: {}", user.getEmail());
            return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.");
        } catch (Exception e) {
            log.error("비밀번호 재설정 이메일 발송 실패 - 이메일: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 오류가 발생했습니다.");
        }
    }
}
