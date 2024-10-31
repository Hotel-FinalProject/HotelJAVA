package com.example.backend.util;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.nimbusds.jose.JOSEException;

import java.io.IOException;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public LoginSuccessHandler(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /** 로그인 인증 성공 시 */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        if (oAuth2User != null) {
            // 네이버 로그인일 경우 response 안에 정보가 담겨 있다.
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");

            String email;
            String name;

            // 네이버와 기타 소셜 로그인을 분기 처리
            if (responseMap != null) {
                // 네이버 소셜 로그인 응답 처리
                email = (String) responseMap.get("email");
                name = (String) responseMap.get("name");
            } else {
                // 구글 또는 다른 소셜 로그인 응답 처리
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
            }

            try {
                // 사용자 데이터베이스에 저장
                User user = userService.OAuthPostLogin(email, name);

                // 사용자 정보를 기반으로 JWT 생성
                String token = jwtUtil.createJwt(user.getEmail(), user.getUserId(), user.getName(), user.getRole());

                // 리다이렉트로 토큰 전달
                response.sendRedirect("http://localhost:8082/oauth2/success?token=" + token);

            } catch (JOSEException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT 생성 중 오류가 발생했습니다.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 접근입니다.");
        }
    }
}
