package com.example.backend.service;

import com.example.backend.dto.UpdateUserRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Map<String, String> verificationTokens = new ConcurrentHashMap<>();

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 회원가입 처리 로직
     * @param user 가입하려는 사용자의 정보 (이메일, 비밀번호, 이름, 전화번호 등).
     * @param verificationToken 이메일 인증을 통해 생성된 JWT 토큰으로, 사용자의 이메일이 유효한지 확인하는 데 사용됩니다.
     * @return 회원가입 성공 여부에 대한 HTTP 응답.
     */
    public ResponseEntity<?> signup(User user, String verificationToken) {
        // JWT 검증
        String verifiedEmail = verificationToken;

        try {
            verifiedEmail = jwtUtil.verifyJwt(verificationToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
        }

        if (!verifiedEmail.equals(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
        }

        // 비밀번호 유효성 검사
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 입력되지 않았습니다.");
        } else if (!user.getPassword().matches(passwordPattern)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 4~12자, 영어 대/소문자 및 특수문자를 포함해야 합니다.");
        }

        // 전화번호 유효성 검사
        String phonePattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        if (!user.getPhone().matches(phonePattern)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 전화번호 형식입니다.");
        }

        // 사용자의 정보 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setLoginType("normal");
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    /**
     * OAuth 회원가입 및 로그인 처리 로직
     * @param email 사용자 이메일 주소.
     * @param name 사용자 이름.
     * @param phone 사용자 전화번호.
     * @param provider OAuth 제공자 (예: Google, Naver).
     * @return 신규 사용자가 생성되었거나 이미 존재하는 사용자를 반환합니다.
     */
    public User OAuthPostLogin(String email, String name, String phone, String provider) {
        Optional<User> existUser = userRepository.findByEmail(email);

        User user;
        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPhone(phone);
            // 임의의 비밀번호 설정 (UUID 사용)
            String randomPassword = UUID.randomUUID().toString();
            newUser.setPassword(passwordEncoder.encode(randomPassword));
            newUser.setRole("ROLE_USER");
            newUser.setLoginType("oauth");
            newUser.setOauthProvider(provider);
            user = userRepository.save(newUser);
        } else {
            user = existUser.get();
        }
        return user;
    }

    /**
     * 로그인 처리 및 JWT 발급
     * @param email 사용자가 입력한 이메일 주소.
     * @param rawPassword 사용자가 입력한 비밀번호.
     * @return 로그인 성공 시 JWT와 사용자 정보를 포함한 응답.
     */
    public ResponseEntity<?> login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getIsActive()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("계정이 비활성화되었습니다. 계정을 다시 활성화하려면 고객 지원에 문의하십시오.");
            }

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                // 비밀번호가 일치하면 JWT 생성
                try {
                    String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(),user.getName(), user.getRole());

                    // 마지막 로그인 시간 업데이트
                    user.setLastLoginTime(LocalDateTime.now());
                    userRepository.save(user);

                    // JSON 형태로 응답하기 위해 Map 사용
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", user.getUserId());
                    response.put("userName", user.getName());
                    response.put("email", user.getEmail());
                    response.put("phone", user.getPhone());

                    return ResponseEntity.ok(response);
                } catch (JOSEException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    /**
     * 이메일 인증 메일 전송
     * @param email 인증 메일을 보낼 사용자의 이메일 주소.
     * @param mode 이메일 전송 모드 (예: 회원가입, 비밀번호 초기화, 비밀번호 재설정).
     * @return 이메일 발송 성공 여부에 대한 응답.
     */
    public ResponseEntity<?> sendVerificationEmail(String email, String mode) {
        log.info("이메일 인증 메일 전송 시작 - 이메일: {}, 모드: {}", email, mode);

        Optional<User> userOptional = userRepository.findByEmail(email);

        // 가입 시 이미 존재하는 이메일이면 충돌 오류 반환
        if ("signup".equals(mode) && userOptional.isPresent()) {
            log.warn("이미 사용 중인 이메일입니다. 이메일: {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 변경(수정) 시 이메일이 존재하지 않으면 오류 반환
        if ("editPassword".equals(mode) && userOptional.isEmpty()) {
            log.warn("해당 이메일로 등록된 사용자가 없습니다. 이메일: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록되지 않은 이메일입니다.");
        }

        try {
            String verificationToken = jwtUtil.createEmailVerificationJwt(email);
            log.info("JWT 토큰 생성 성공 - 토큰: {}", verificationToken);

            // 이메일 전송 내부 메서드 호출
            sendVerificationEmailInternal(email, verificationToken, mode);
            log.info("이메일 발송 성공 - 이메일: {}", email);
        } catch (JOSEException e) {
            log.error("JWT 생성 중 오류 발생 - 이메일: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생 - 이메일: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 발송 중 오류가 발생했습니다.");
        }

        return ResponseEntity.ok("인증 이메일이 발송되었습니다.");
    }


    /**
     * 내부용 이메일 인증 메일 전송
     * @param toEmail 이메일을 보낼 대상.
     * @param token 이메일 인증을 위한 JWT 토큰.
     * @param mode 이메일 전송 목적 (회원가입, 비밀번호 변경).
     */
    private void sendVerificationEmailInternal(String toEmail, String token, String mode) {
        try {
            log.info("이메일 발송 준비 중 - 이메일: {}, 모드: {}", toEmail, mode);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("[수동태] 이메일 인증 요청");

            // 로컬
//            String link = "http://localhost:8082/verify-email?token=" + token + "&mode=" + mode;

            // 배포
            String link = "http://43.200.45.122/verify-email?token=" + token + "&mode=" + mode;

            String emailContent = "<html>" +
                    "<body>" +
                    "<p>아래 버튼을 클릭하여 이메일 인증을 완료해주세요:</p>" +
                    "<a href=\"" + link + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #6c5ce7; text-decoration: none; border-radius: 5px;\">이메일 인증하기</a>" +
                    "<p>감사합니다.</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
            log.info("이메일 발송 완료 - 이메일: {}", toEmail);
        } catch (MessagingException e) {
            log.error("이메일 발송 실패 - 이메일: {}", toEmail, e);
            throw new IllegalStateException("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    /**
     * 이메일 인증 처리
     * @param token 인증을 위해 사용되는 JWT 토큰.
     * @return 인증 성공 여부에 대한 응답.
     */
    public ResponseEntity<?> verifyEmail(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = jwtUtil.verifyJwt(token);
            log.info("인증된 이메일: {}", email);
            response.put("success", true);
            response.put("email", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("유효하지 않은 인증 토큰입니다. 토큰: {}", token, e);
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /** 비밀번호 재설정 메일 발송
     * @param toEmail 비밀번호 재설정 안내를 받을 사용자의 이메일 주소.
     * @param temporaryPassword 생성된 임시 비밀번호. 사용자가 이 비밀번호로 로그인 후 변경해야 합니다. */
    void sendPasswordResetEmail(String toEmail, String temporaryPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("[수동태] 비밀번호 재설정 안내");

            String content = "<p>귀하의 요청에 따라 비밀번호가 재설정되었습니다.</p>" +
                    "<p>임시 비밀번호는 다음과 같습니다: <strong>" + temporaryPassword + "</strong></p>" +
                    "<p>로그인 후 즉시 비밀번호를 변경하시기 바랍니다.</p>";
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 정보 수정 로직
     * @param email 수정하려는 사용자의 이메일.
     * @param updateUserRequest 수정할 정보 (이름, 전화번호 등).
     * @return 회원 정보 수정 결과.
     */
    @Transactional
    public ResponseEntity<?> updateUser(String email, UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 사용자 정보 업데이트
            if (updateUserRequest.getUserName() != null && !updateUserRequest.getUserName().isEmpty()) {
                user.setName(updateUserRequest.getUserName());
            }
            if (updateUserRequest.getPhone() != null && !updateUserRequest.getPhone().isEmpty()) {
                user.setPhone(updateUserRequest.getPhone());
            }

            // 업데이트된 사용자 정보 저장
            userRepository.save(user);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    /**
     * 비밀번호 변경 로직
     * @param email 비밀번호를 변경하려는 사용자의 이메일.
     * @param token 이메일 인증을 위해 발송된 JWT 토큰.
     * @param newPassword 새로운 비밀번호.
     * @return 비밀번호 변경 상태에 대한 응답.
     */
    @Transactional
    public ResponseEntity<?> resetPassword(String email, String token, String newPassword) {
        try {
            log.info("비밀번호 변경 요청 - 이메일: {}, 토큰: {}", email, token);

            // JWT 토큰 검증
            String verifiedEmail = jwtUtil.verifyJwt(token);

            log.info("검증된 이메일: {}", verifiedEmail);

            // 토큰의 이메일과 입력된 이메일이 같은지 확인
            if (!verifiedEmail.equals(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 인증 토큰입니다.");
            }

            // 이메일로 사용자 조회
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 비밀번호 유효성 검사
                String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
                if (!newPassword.matches(passwordPattern)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 4~12자, 영어 대/소문자 및 특수문자를 포함해야 합니다.");
                }

                // 비밀번호 변경
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

    /**
     * 비밀번호 확인 로직
     * @param email 비밀번호 확인 대상 사용자의 이메일 주소
     * @param rawPassword 사용자가 입력한 비밀번호 (평문)
     * @return 비밀번호 검증 결과에 대한 HTTP 응답
     */
    @Transactional
    public ResponseEntity<?> verifyPassword(String email, String rawPassword) {
        log.info("비밀번호 확인 요청 - 이메일: {}, 비밀번호: {}", email, rawPassword);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 비밀번호 검증
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return ResponseEntity.ok("비밀번호가 일치합니다.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    /** 회원 탈퇴 처리
     * @param email 탈퇴하려는 사용자의 이메일 주소
     * @return 회원 탈퇴 결과에 대한 HTTP 응답 */
    @Transactional
    public ResponseEntity<?> deleteUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Soft Delete: is_active 플래그를 false로 설정
            user.setIsActive(false);
            userRepository.save(user);

            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}