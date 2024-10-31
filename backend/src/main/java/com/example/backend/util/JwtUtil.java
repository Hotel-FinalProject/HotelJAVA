package com.example.backend.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    /** JWT 토큰 생성 로직
     * @param userId 로그인 시도한 유저의 idx
     * @param name 로그인 시도한 유저의 이름
     * @param role 로그인 시도한 유저의 권한
     * */
    public String createJwt(String subject, Long userId, String name, String role) throws JOSEException {
        // JWT Claims 생성
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("userIdx", userId)
                .claim("name", name)
                .claim("role", role)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)) // 토큰 만료 시간 1시간 설정
                .build();

        // 서명 알고리즘 및 키 설정
        JWSSigner signer = new MACSigner(SECRET.getBytes());

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
