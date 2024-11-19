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
    public ResponseEntity<?> testUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
        }
        try {
            String actualToken = token.replace("Bearer ", "");
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);
            return ResponseEntity.ok("유저 ID는 " + userId + "입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 해주세요.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
        }
        String verificationToken = authorizationHeader.substring(7);
        if (verificationToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
        }
        return userService.signup(user, verificationToken);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return userService.login(loginRequest.getEmail(), loginRequest.getPasswd());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }
        boolean exists = userRepository.existsByEmail(email);
        return exists ? ResponseEntity.status(409).body("이미 사용중인 이메일 입니다.") : ResponseEntity.ok("사용 가능한 이메일 입니다.");
    }

    @PostMapping("/find-id")
    public ResponseEntity<Object> findId(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이름이 필요합니다.");
        }

        List<User> userList = userRepository.findByName(name);
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이름의 사용자를 찾을 수 없습니다.");
        }

        List<User> socialAccounts = new ArrayList<>();
        List<User> normalAccounts = new ArrayList<>();
        for (User user : userList) {
            if ("oauth".equals(user.getLoginType())) {
                socialAccounts.add(user);
            } else if ("normal".equals(user.getLoginType())) {
                normalAccounts.add(user);
            }
        }

        if (!socialAccounts.isEmpty()) {
            return ResponseEntity.ok("소셜 로그인 계정입니다. 소셜 제공자를 통해 로그인해 주세요.");
        }

        if (!normalAccounts.isEmpty()) {
            List<String> maskedEmails = normalAccounts.stream()
                    .map(user -> maskEmail(user.getEmail()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(maskedEmails);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일반 계정을 찾을 수 없습니다.");
    }

    private String maskEmail(String email) {
        int atIdx = email.indexOf("@");
        return atIdx > 1 ? email.substring(0, 2) + "****" + email.substring(atIdx) : email;
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String mode = requestBody.get("mode");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }

        if (!isValidMode(mode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 모드입니다.");
        }

        return userService.sendVerificationEmail(email, mode);
    }

    private boolean isValidMode(String mode) {
        return mode != null && (mode.equals("signup") || mode.equals("resetPassword") || mode.equals("editPassword"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 필요합니다.");
        }
        return userService.verifyEmail(token);
    }

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
