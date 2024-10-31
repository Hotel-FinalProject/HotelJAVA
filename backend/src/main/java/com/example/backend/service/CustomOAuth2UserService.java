package com.example.backend.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    /** 유저 정보 조회 */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 유저 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        // 필요한 정보 추출
        String email = (String) response.get("email");
        String name = (String) response.get("name");

        // 이메일이 없으면 예외 발생
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("이메일 정보가 제공되지 않았습니다.");
        }

        // 이름도 마찬가지로 검사
        if (name == null || name.isEmpty()) {
            throw new OAuth2AuthenticationException("이름 정보가 제공되지 않았습니다.");
        }

        // 필요한 정보를 User 엔티티에 저장하거나 다른 로직 수행
        // 이 부분은 예를 들면 DB 저장 등의 후속 처리를 하면 됩니다.

        return oAuth2User;
    }
}
