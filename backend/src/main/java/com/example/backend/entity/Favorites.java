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

    @ManyToOne
    @JoinColumn(name = "hotel_id",referencedColumnName = "hotelId", nullable = false, unique = true)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId", nullable = false)
    private User user;
}

