package com.example.backend.Controller;

import com.example.backend.dto.UpdateUserRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/users")
public class AuthUserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 현재 로그인된 사용자의 정보 조회
     * - 사용자의 정보를 확인합니다.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String email = jwtUtil.verifyJwt(token);

            // 이메일을 통해 사용자 정보 조회
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 사용자 정보를 JSON 형태로 응답
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("name", user.getName());
                userInfo.put("email", user.getEmail());
                userInfo.put("phone", user.getPhone());

                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 현재 로그인된 사용자의 정보 수정
     * - JWT를 사용하여 인증 후 사용자의 정보를 수정합니다.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            // Bearer 토큰에서 실제 토큰 부분만 추출
            String token = authorizationHeader.replace("Bearer ", "");
            // JWT 토큰 검증 및 이메일 추출
            String email = jwtUtil.verifyJwt(token);

            // 사용자 정보 수정 서비스 호출
            return userService.updateUser(email, updateUserRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 비밀번호 변경 요청 처리
     * - 이메일과 인증 토큰을 검증한 후 새로운 비밀번호로 변경합니다.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 필요합니다.");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호가 필요합니다.");
        }

        try {
            return userService.resetPassword(email, token, newPassword);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

    /**
     * 비밀번호 확인 기능
     * <p>회원 정보 수정을 위한 비밀번호 확인 기능. 사용자로부터 입력받은 비밀번호가 현재 비밀번호와 일치하는지 검증합니다.<p/>
     */
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> requestBody) {
        try {
            // Bearer 토큰에서 실제 토큰 부분만 추출
            String token = authorizationHeader.replace("Bearer ", "");
            // JWT 토큰 검증 및 이메일 추출
            String email = jwtUtil.verifyJwt(token);

            // 요청 본문에서 입력받은 비밀번호 추출
            String rawPassword = requestBody.get("password");

            if (rawPassword == null || rawPassword.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 필요합니다.");
            }

            // 사용자 서비스에서 비밀번호 검증 로직 실행
            return userService.verifyPassword(email, rawPassword);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }
}

