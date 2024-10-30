//package com.example.backend.service;
//
//import com.example.backend.entity.User;
//import com.example.backend.repository.UserRepository;
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSSigner;
//import com.nimbusds.jose.crypto.MACSigner;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Transactional
//@Service
//@Slf4j
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    private static final String SECRET = "your_very_long_secret_key_that_is_at_least_msa6th_2team"; // 실제로는 환경 변수로 관리하는 것이 안전합니다.
//
//    public ResponseEntity<?> signup(User user){
//        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,12}$";
//
//        // 이메일 중복 검사
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
//        }
//
//        if (user.getPassword() == null || user.getPassword().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 입력되지 않았습니다.");
//        } else if (!user.getPassword().matches(passwordPattern)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 4~12자, 영어 대/소문자 및 특수문자를 포함해야 합니다.");
//        }
//
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//        user.setRole("ROLE_USER");
//
//        userRepository.save(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
//    }
//
//    // 로그인 처리 및 JWT 발급
//    public ResponseEntity<?> login(String email, String rawPassword) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
//                // 비밀번호가 일치하면 JWT 생성
//                try {
//                    String token = createJwt(user);
//                    return ResponseEntity.ok(token);
//                } catch (JOSEException e) {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JWT 생성 중 오류가 발생했습니다.");
//                }
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
//    }
//
//    private String createJwt(User user) throws JOSEException {
//        // JWT Claims 생성
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                .subject(user.getEmail())
//                .claim("userIdx", user.getUserId())
//                .claim("role", user.getRole())
//                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)) // 토큰 만료 시간 1시간 설정
//                .build();
//        // 서명 알고리즘 및 키 설정
//        JWSSigner signer = new MACSigner(SECRET.getBytes()); // 바이트 배열로 변환하여 사용
//        // JWT 생성 및 서명
//        SignedJWT signedJWT = new SignedJWT(
//                new JWSHeader(JWSAlgorithm.HS256),
//                claimsSet
//        );
//
//        signedJWT.sign(signer);
//
//        // JWT 문자열 반환
//        return signedJWT.serialize();
//    }
//}
