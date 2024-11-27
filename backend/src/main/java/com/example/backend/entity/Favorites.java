package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId; // 찜 관리 번호

    private Boolean status; // 찜 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id",referencedColumnName = "hotelId", nullable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "userId", nullable = false)
    private User user;


}

