package com.example.backend.Controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /** 회원가입 요청 */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user, @RequestParam String verificationToken) {
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
            } else if ("normal".equals(user.getLoginType()) && user.getPhone().equals(phone)) {
                normalAccounts.add(user);
            }
        }

        // 소셜 계정이 여러 개일 경우
        if (!socialAccounts.isEmpty()) {
            StringBuilder responseMessage = new StringBuilder("소셜 로그인 계정입니다. ");

            // 소셜 로그인 제공자 정보 추가
            for (User socialUser : socialAccounts) {
                String provider = socialUser.getOauthProvider();
                responseMessage.append("[").append(provider).append("]");
            }

            responseMessage.append(" 소셜 제공자를 통해 로그인해 주세요.");
            return ResponseEntity.ok(responseMessage.toString());
        }

        // 일반 계정이 있을 경우
        if (!normalAccounts.isEmpty()) {
            User user = normalAccounts.get(0);
            return ResponseEntity.ok("회원님의 이메일은 " + user.getEmail() + " 입니다.");
        }

        // 해당 이름의 소셜 계정은 있으나 전화번호가 일치하지 않을 경우
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("전화번호가 일치하지 않습니다.");
    }

    /** 이메일 인증 메일 요청 */
    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 필요합니다.");
        }
        return userService.sendVerificationEmail(email);
    }

    /** 이메일 인증 요청 */
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return userService.verifyEmail(token);
    }

}