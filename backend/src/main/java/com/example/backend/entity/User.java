package com.example.backend.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private String phone; // 고객 전화번호

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<Favorites> favorites;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}


