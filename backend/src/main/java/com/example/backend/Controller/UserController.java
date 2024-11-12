package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.PasswordResetService;
import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/test")
    public ResponseEntity<?> testUser(@RequestHeader("Authorization") String token) {
        try {
            // Bearer 부분을 제거하고 실제 토큰 값만 추출
            String actualToken = token.replace("Bearer ", "");

            // 토큰을 이용해 유저 ID 확인
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            // 유저 ID가 확인되면 응답
            return ResponseEntity.ok("유저 ID는 " + userId + "입니다.");
        } catch (Exception e) {
            // 토큰 검증 실패 시
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
        }
    }

    /** 회원가입 요청 */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user, @RequestHeader("verificationToken") String authorizationHeader) {
        String verificationToken = null;

        // 헤더에서 Bearer 토큰 파싱
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            verificationToken = authorizationHeader.substring(7);
        }

        if (verificationToken == null || verificationToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
        }

        return userService.signup(user, verificationToken);
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
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용중인 이메일 입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일 입니다.");
        }
    }

    /** 아이디 찾기 */
    @PostMapping("/find-id")
    public ResponseEntity<Object> findId(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");

        // 이름을 기반으로 사용자 리스트를 조회
        List<User> userList = userRepository.findByName(name);

        if (userList.isEmpty()) {
            // 이름이 일치하는 사용자가 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이름의 사용자를 찾을 수 없습니다.");
        }

        // 소셜 계정과 일반 계정 구분을 위한 리스트
        List<User> socialAccounts = new ArrayList<>();
        List<User> normalAccounts = new ArrayList<>();

        // 소셜 로그인 계정과 일반 로그인 계정을 구분
        for (User user : userList) {

            if ("oauth".equals(user.getLoginType())) {
                socialAccounts.add(user);
            } else if ("normal".equals(user.getLoginType())) {
                normalAccounts.add(user);
            }
        }

        // 소셜 계정이 여러 개일 경우
        if (!socialAccounts.isEmpty()) {
            StringBuilder responseMessage = new StringBuilder("소셜 로그인 계정입니다. ");

            responseMessage.append("소셜 제공자를 통해 로그인해 주세요.");
            return ResponseEntity.ok(responseMessage.toString());
        }

        // 일반 계정이 있을 경우
        if (!normalAccounts.isEmpty()) {
            // 이메일을 마스킹하여 반환
            List<String> maskedEmails = normalAccounts.stream()
                    .map(user -> maskEmail(user.getEmail()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(maskedEmails);
        }

        // 해당 이름의 소셜 계정은 있으나 일반 계정이 없을 경우
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일반 계정을 찾을 수 없습니다.");
    }

    /** 아이디 찾기 이메일 마스킹 로직 */
    private String maskEmail(String email) {
        int atIdx = email.indexOf("@");
        if (atIdx > 1) {
            return email.substring(0, 2) + "****" + email.substring(atIdx);
        }
        return email; // 이메일 형식이 올바르지 않으면 마스킹하지 않음
    }

    /** 이메일 인증 메일 요청 */
    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String mode = requestBody.get("mode"); // mode 값 추가

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }

        if (mode == null || (!mode.equals("signup") && !mode.equals("resetPassword"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 모드입니다.");
        }

        return userService.sendVerificationEmail(email, mode);
    }

    /** 이메일 인증 요청 */
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return userService.verifyEmail(token);
    }

    /** 비밀번호 변경 */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String token = requestBody.get("token");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 필요합니다.");
        }

        try {
            return passwordResetService.resetPasswordWithGeneratedPassword(email, token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 중 오류가 발생했습니다.");
        }
    }
}