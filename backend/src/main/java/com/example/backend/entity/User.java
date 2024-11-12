package com.example.backend.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 고객 관리 번호

    @Column(nullable = false)
    private String password; // 고객 비밀번호

    @Column(nullable = false)
    private String name; // 고객 이름

    @Column(nullable = false, unique = true)
    private String email; // 고객 이메일

    //    @Column(nullable = false)
    private String phone; // 고객 전화번호

    private String role; // 권한

    @Column(nullable = false)
    private String loginType;

    private String oauthProvider;

    // 리뷰 내용 보존을 위해 계정 활성화/비활성화 컬럼 추가
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // 마지막 로그인 시간 기록
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<Favorites> favorites;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }


}