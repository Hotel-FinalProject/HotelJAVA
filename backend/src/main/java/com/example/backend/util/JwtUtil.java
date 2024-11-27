package com.example.backend.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    /** JWT 토큰 생성 로직 (유저 상세 정보 포함)
     * @param subject JWT의 주체 (일반적으로 이메일 또는 사용자 이름)
     * @param userId 로그인 시도한 유저의 ID
     * @param name 로그인 시도한 유저의 이름
     * @param role 로그인 시도한 유저의 권한
     * */
    public String createJwt(String subject, Long userId, String name, String role) throws JOSEException {
        // JWT Claims 생성
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("userId", userId)
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

    /** 이메일 인증용 JWT 생성 로직 (이메일만을 위한 간단한 JWT)
     * @param email 인증하려는 유저의 이메일
     * */
    public String createJwt(String email) throws JOSEException {
        return createEmailVerificationJwt(email);
    }

    /** 이메일 인증용 JWT 생성 로직 (세부적인 메소드)
     * @param email 인증하려는 유저의 이메일
     * */
    public String createEmailVerificationJwt(String email) throws JOSEException {
        // JWT Claims 생성 (이메일만 포함)
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)) // 토큰 만료 시간 15분 설정
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

    /** JWT 검증 및 이메일 반환 로직
     * @param token 검증하려는 JWT 토큰
     * @return JWT의 subject (이메일)
     * @throws ParseException 토큰 파싱 실패 시 예외 발생
     * @throws JOSEException 서명 검증 실패 시 예외 발생
     * */
    public String verifyJwt(String token) throws ParseException, JOSEException {
        // JWT 파싱
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 서명 검증
        JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("JWT 서명 검증에 실패했습니다.");
        }

        // 만료 시간 확인
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (new Date().after(expirationTime)) {
            throw new JOSEException("JWT가 만료되었습니다.");
        }

        // 이메일 반환 (subject)
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    /** JWT 검증 및 유저 ID 반환 로직
     * @param token 검증하려는 JWT 토큰
     * @return JWT의 userId
     * @throws ParseException 토큰 파싱 실패 시 예외 발생
     * @throws JOSEException 서명 검증 실패 시 예외 발생
     * */
    public Long verifyJwtAndGetUserId(String token) throws ParseException, JOSEException {
        // JWT 파싱
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 서명 검증
        JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("JWT 서명 검증에 실패했습니다.");
        }

        // 만료 시간 확인
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (new Date().after(expirationTime)) {
            throw new JOSEException("JWT가 만료되었습니다.");
        }

        // 유저 ID 반환 (Claims에서 userId를 추출)
        Long userId = signedJWT.getJWTClaimsSet().getLongClaim("userId");

        // 유저 ID가 없다면 예외를 던집니다.
        if (userId == null) {
            throw new JOSEException("유저 ID가 포함되지 않은 토큰입니다.");
        }

        return userId;
    }

}
