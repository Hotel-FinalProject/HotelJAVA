package com.example.backend.Controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class OAuthController {

//    private final ClientRegistrationRepository clientRegistrationRepository;

//    /** OAuth 로그인 시도 */
//    @GetMapping("/oauth2/authorize/{provider}")
//    public void redirectToOAuth(@PathVariable String provider, HttpServletResponse response) throws IOException {
//        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider);
//
//        if (registration == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, provider + " 클라이언트 등록 정보가 없습니다.");
//            return;
//        }
//
//        // 리다이렉트 하기 위해 URL 생성
//        String authorizationUri = registration.getProviderDetails().getAuthorizationUri();
//        String redirectUri = registration.getRedirectUri().replace("{registrationId}", provider);
//
//        String redirectUrl = String.format(
//                "%s?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s",
//                authorizationUri,
//                registration.getClientId(),
//                redirectUri,
//                String.join(" ", registration.getScopes()),
//                "state"
//        );
//
//        // 서버 측에서 직접 리다이렉트
//        response.sendRedirect(redirectUrl);
//    }

//    /** OAuth 구글 로그인 */
//    @GetMapping("/oauth2/authorize/google")
//    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
//        ClientRegistration googleRegistration = clientRegistrationRepository.findByRegistrationId("google");
//
//        if (googleRegistration == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "구글 클라이언트 등록 정보가 없습니다.");
//            return;
//        }
//
//        // 구글로 리다이렉트 하기 위해 URL 생성
//        String authorizationUri = googleRegistration.getProviderDetails().getAuthorizationUri();
//        String redirectUri = googleRegistration.getRedirectUri().replace("{registrationId}", "google");
//
//        String redirectUrl = String.format(
//                "%s?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s",
//                authorizationUri,
//                googleRegistration.getClientId(),
//                redirectUri,
//                String.join(" ", googleRegistration.getScopes()),
//                "state"
//        );
//
//        // 서버 측에서 직접 구글로 리다이렉트
//        response.sendRedirect(redirectUrl);
//    }
//
//    /** OAuth 네이버 로그인 */
//    @GetMapping("/oauth2/authorize/naver")
//    public void redirectToNaverOAuth(HttpServletResponse response) throws IOException {
//        ClientRegistration naverRegistration = clientRegistrationRepository.findByRegistrationId("naver");
//
//        if (naverRegistration == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "네이버 클라이언트 등록 정보가 없습니다.");
//            return;
//        }
//
//        // 네이버로 리다이렉트 하기 위해 URL 생성
//        String authorizationUri = naverRegistration.getProviderDetails().getAuthorizationUri();
//        String redirectUri = naverRegistration.getRedirectUri().replace("{registrationId}", "naver");
//
//        String redirectUrl = String.format(
////                "%s?client_id=%s&redirect_uri=%s&response_type=code&state=%s",
//                authorizationUri,
//                naverRegistration.getClientId(),
//                redirectUri,
//                "state"
//        );
//
//        // 서버 측에서 직접 네이버로 리다이렉트
//        response.sendRedirect(redirectUrl);
//    }

}
