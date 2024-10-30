package com.example.backend.config;

import com.example.backend.service.customOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 활성화
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (개발 중)
                .csrf(AbstractHttpConfigurer :: disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/**", "/oauth2/**","/login/**").permitAll()  // 회원가입 및 로그인 경로를 모든 사용자에게 허용
                        .anyRequest().authenticated()
                )
//                .oauth2Login(oauth2 ->
//                        oauth2
//                                .authorizationEndpoint(authorization -> authorization
//                                        .baseUri("http://localhost:8081/api/users/oauth2/google") // Google 소셜 로그인 요청 경로 설정
//                                )
//                                .defaultSuccessUrl("http://localhost:8082/", true) // Vue 홈 경로로 리다이렉트
//                );
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorization ->
//                                authorization.baseUri("/oauth2/authorization")
//                        )
//                        .redirectionEndpoint(redirection ->
//                                redirection.baseUri("/login/oauth2/code/*")
//                        )
//                );
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login") // 로그인 페이지 설정
                                .defaultSuccessUrl("/api/oauth2/success", true) // 성공 시 호출할 엔드포인트 설정
                                .failureUrl("/login?error=true") // 실패 시 호출할 경로 설정
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService()) // CustomOAuth2UserService를 통해 유저 정보 처리
                                )
                );

        return http.build();
    }

    @Bean
    public customOAuth2UserService customOAuth2UserService() {
        return new customOAuth2UserService();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8082"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
