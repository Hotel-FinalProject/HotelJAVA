package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // 고객 관리 번호

    @Column(nullable = false)
    private String password; // 고객 비밀번호

    @Column(nullable = false)
    private String name; // 고객 이름

    @Column(nullable = false, unique = true)
    private String email; // 고객 이메일

    private String phone; // 고객 전화번호

    private String role; // 권한

    @Column(nullable = false)
    private String loginType;

    private String oauthProvider;

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
