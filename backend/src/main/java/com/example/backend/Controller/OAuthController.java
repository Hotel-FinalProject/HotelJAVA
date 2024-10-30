package com.example.backend.Controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    /** OAuth 구글 로그인 */
    @GetMapping("/oauth2/authorize/google")
    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
        ClientRegistration googleRegistration = clientRegistrationRepository.findByRegistrationId("google");

        if (googleRegistration == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "구글 클라이언트 등록 정보가 없습니다.");
            return;
        }

        // 구글로 리다이렉트 하기 위해 URL 생성
        String authorizationUri = googleRegistration.getProviderDetails().getAuthorizationUri();
        String redirectUri = googleRegistration.getRedirectUri().replace("{registrationId}", "google");

        String redirectUrl = String.format(
                "%s?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s",
                authorizationUri,
                googleRegistration.getClientId(),
                redirectUri,
                String.join(" ", googleRegistration.getScopes()),
                "state"
        );

        // 서버 측에서 직접 구글로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}
