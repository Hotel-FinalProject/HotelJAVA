package com.example.backend.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException; // 이 부분을 수정해야 합니다.
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secretKey;

    public JwtAuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                // JWT 검증 및 파싱
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new MACVerifier(secretKey);
                if (signedJWT.verify(verifier)) {
                    String email = signedJWT.getJWTClaimsSet().getSubject();
                    String role = signedJWT.getJWTClaimsSet().getStringClaim("role");

                    // 인증 객체 생성 후 SecurityContext에 설정
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (java.text.ParseException | JOSEException e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
