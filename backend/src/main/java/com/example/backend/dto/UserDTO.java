package com.example.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Getter
@Setter
public class UserDTO implements UserDetails {
        private Long userId; // 고객 관리 번호
        private String password; // 고객 비밀번호
        private String name; // 고객 이름
        private String email; // 고객 이메일
        private Collection<? extends GrantedAuthority> authorities; // 권한 목록

        @Override
        public String getUsername() {
                return name; // 사용자 이름을 반환 (로그인 시 사용)
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities; // 권한 목록을 반환
        }

        @Override
        public String getPassword() {
                return password; // 비밀번호를 반환
        }

        @Override
        public boolean isAccountNonExpired() {
                return true; // 계정 만료 여부 (true면 만료되지 않음)
        }

        @Override
        public boolean isAccountNonLocked() {
                return true; // 계정 잠금 여부 (true면 잠금되지 않음)
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true; // 자격 증명 만료 여부 (true면 만료되지 않음)
        }

        @Override
        public boolean isEnabled() {
                return true; // 계정 활성화 여부 (true면 활성화)
        }
}
