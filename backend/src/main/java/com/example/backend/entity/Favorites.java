package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId; // 찜 관리 번호

    private Boolean status; // 찜 상태

    @OneToOne
    @JoinColumn(name = "hotel_id", nullable = false, unique = true) // 호텔 관리 번호 (외래키)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 고객 관리 번호 (외래키)
    private User user;
}

