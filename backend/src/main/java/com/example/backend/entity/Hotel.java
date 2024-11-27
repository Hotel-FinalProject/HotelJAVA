package com.example.backend.entity;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId; // 호텔 관리 번호

    private String name; // 호텔 이름
    private String location; // 호텔 위치
    private String address; // 호텔 주소

    @Column(nullable = true)
    private String imageUrl; // 호텔 이미지 URL (S3 이미지 경로)

    private String hotelnum; // 호텔 전화번호

    private Long contentId; // API에서 제공하는 호텔 고유 식별자

    private Double mapX; // 호텔 위치 X 좌표
    private Double mapY; // 호텔 위치 Y 좌표

    private String checkIn; // 호텔 체크인 날짜
    private String checkOut; // 호텔 체크아웃 날짜

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;

    @OneToMany(mappedBy = "hotel")
    private List<Favorites> favorite;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;
}


