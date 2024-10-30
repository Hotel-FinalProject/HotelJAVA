package com.example.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.JOSEException;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Autowired
    public LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 사용자 인증 정보에서 필요한 데이터 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        try {
            // 사용자 정보를 기반으로 JWT 생성
            String token = jwtUtil.createJwt(oAuth2User.getName(), null, "ROLE_USER"); // 필요한 사용자 정보로 수정

            // 응답에 JWT 추가
            response.setHeader("Authorization", "Bearer " + token);

            // 리다이렉트 설정 (프론트엔드로 JWT와 함께 응답)
            response.sendRedirect("/?token=" + token);
        } catch (JOSEException e) {
            // JWT 생성 중 오류가 발생한 경우 처리
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT 생성 중 오류가 발생했습니다.");
        }
    }
}
