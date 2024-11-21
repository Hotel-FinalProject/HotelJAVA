package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService { //extends DefaultOAuth2UserService {

    @Value("${google.people-api-url}")
    private String googlePeopleApiUrl;

    /** 유저 정보 조회 */
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        // 유저 정보 가져오기
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//
//        // 이메일이 없으면 예외 발생
//        if (email == null || email.isEmpty()) {
//            throw new OAuth2AuthenticationException("이메일 정보가 제공되지 않았습니다.");
//        }
//
//        // 전화번호 가져오기 (Google People API 사용)
//        String phone = null;
//        try {
//            String accessToken = userRequest.getAccessToken().getTokenValue();
//            RestTemplate restTemplate = new RestTemplate();
//            String url = googlePeopleApiUrl + "?personFields=phoneNumbers";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessToken);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//
//            if (responseEntity.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> personFields = responseEntity.getBody();
//                System.out.println("응답 본문: " + personFields);
//                List<Map<String, Object>> phoneNumbers = (List<Map<String, Object>>) personFields.get("phoneNumbers");
//                if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
//                    phone = (String) phoneNumbers.get(0).get("value");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return oAuth2User;
//    }
}