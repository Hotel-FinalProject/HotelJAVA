package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String SECRET = "your_very_long_secret_key_that_is_at_least_msa6th_2team"; // 실제로는 환경 변수로 관리하는 것이 안전합니다.

    public User signup(User user){
        if (user.getPasswd() == null || user.getPasswd().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 입력되지 않았습니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPasswd());
        user.setPasswd(encodedPassword);

        return userRepository.save(user);
    }

    // 로그인 처리 및 JWT 발급
    public String login(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPasswd())) {
                // 비밀번호가 일치하면 JWT 생성
                try {
                    return createJwt(user);
                } catch (JOSEException e) {
                    throw new RuntimeException("JWT 생성 중 오류가 발생했습니다.", e);
                }
            }
        }
        throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    private String createJwt(User user) throws JOSEException {
        // JWT Claims 생성
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("userIdx", user.getUserIdx())
//                .claim("role", user.getRole())
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)) // 토큰 만료 시간 1시간 설정
                .build();
        // 서명 알고리즘 및 키 설정
        JWSSigner signer = new MACSigner(SECRET.getBytes()); // 바이트 배열로 변환하여 사용
        // JWT 생성 및 서명
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer);

        // JWT 문자열 반환
        return signedJWT.serialize();
    }
}
