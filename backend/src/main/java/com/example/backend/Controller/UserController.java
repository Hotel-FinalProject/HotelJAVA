package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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

    /** 아이디 조회 */
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        String phone = requestBody.get("phone");

        // 이름을 기반으로 사용자를 조회
        Optional<User> userOptional = userRepository.findByName(name);

        if (userOptional.isEmpty()) {
            // 이름이 일치하는 사용자가 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이름의 사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        // 전화번호 일치 여부 확인
        if (!user.getPhone().equals(phone)) {
            // 전화번호가 일치하지 않는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("전화번호가 일치하지 않습니다.");
        }

        // 로그인 유형에 따라 응답 처리
        if ("oauth".equals(user.getLoginType())) {
            // 소셜 로그인인 경우 로그인 제공자를 확인하여 메시지 반환
            String provider = user.getOauthProvider();
            if ("google".equalsIgnoreCase(provider)) {
                return ResponseEntity.ok("구글 소셜 로그인입니다.");
            } else if ("naver".equalsIgnoreCase(provider)) {
                return ResponseEntity.ok("네이버 소셜 로그인입니다.");
            } else {
                return ResponseEntity.ok("소셜 로그인입니다.");
            }
        } else if ("normal".equals(user.getLoginType())) {
            // 일반 로그인인 경우 이메일 반환
            return ResponseEntity.ok("회원님의 이메일은 " + user.getEmail() + " 입니다.");
        }

        // loginType이 명확하지 않을 경우 오류 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 유형을 확인할 수 없습니다.");
    }


}