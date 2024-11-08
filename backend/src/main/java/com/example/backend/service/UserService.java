package com.example.backend.service;

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

import java.net.URI;
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

    /** 회원가입 서비스 부분
     * @param user 가입하려는 사용자의 정보 (이메일, 비밀번호, 이름, 전화번호 등).
     * @param verificationToken  이메일 인증을 통해 생성된 JWT 토큰. 사용자의 이메일이 유효한지 확인하는 데 사용됩니다. */
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

    /** oauth 회원가입 및 로그인
     * @param email  사용자의 이메일 주소.
     * @param name  사용자의 이름.
     * @param phone  사용자의 전화번호.
     * @param provider  OAuth 로그인 제공자 (예: Google, Facebook).*/
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

    /** 로그인 처리 및 JWT 발급
     * @param email  사용자가 입력한 이메일 주소.
     * @param rawPassword  사용자가 입력한 비밀번호. */
    public ResponseEntity<?> login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                // 비밀번호가 일치하면 JWT 생성
                try {
                    String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(),user.getName(), user.getRole());

                    // JSON 형태로 응답하기 위해 Map 사용
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", user.getUserId());
                    response.put("name", user.getName());
                    response.put("email", user.getEmail());

                    return ResponseEntity.ok(response);
                } catch (JOSEException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    /** 이메일 인증 메일 전송
     * @param email  인증 메일을 보내려는 사용자의 이메일 주소.
     * @param mode  동작 모드 (예: 'signup' 모드일 경우 가입용 인증 메일을 전송).*/
    public ResponseEntity<?> sendVerificationEmail(String email, String mode) {
        log.info("이메일 인증 메일 전송 시작 - 이메일: {}, 모드: {}", email, mode);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if ("signup".equals(mode) && userOptional.isPresent()) {
            log.warn("이미 사용 중인 이메일입니다. 이메일: {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        try {
            String verificationToken = jwtUtil.createEmailVerificationJwt(email);
            log.info("JWT 토큰 생성 성공 - 토큰: {}", verificationToken);

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

    /** 내부용 이메일 인증 메일 전송
     * @param toEmail 인증 메일을 받을 이메일 주소.
     * @param token 이메일 인증을 위한 JWT 토큰.
     * @param mode 이메일 인증 목적을 구분하는 모드 (예: 회원가입, 비밀번호 변경 등).*/
    private void sendVerificationEmailInternal(String toEmail, String token, String mode) {
        try {
            log.info("이메일 발송 준비 중 - 이메일: {}, 모드: {}", toEmail, mode);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("[수동태] 이메일 인증 요청");

            String link = "http://localhost:8082/verify-email?token=" + token + "&mode=" + mode;
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

    /** 이메일 인증 처리
     * @param token 이메일 인증을 위해 사용되는 JWT 토큰. */
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
}
