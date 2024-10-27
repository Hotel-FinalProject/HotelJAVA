package com.example.backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId; // 호텔 관리 번호

    private String name; // 호텔 이름
    private Long contentid; // 호텔 분류 번호
    private String locationX; // 호텔 위치
    private String locationY; // 호텔 위치
    private String hotelTel; // 호텔 전화번호
    private String address; // 호텔 주소
    @Column(nullable = true)
    private String imageUrl; // 호텔 이미지 URL (S3 이미지 경로)

    private String checkIn; // 호텔 체크인 날짜
    private String checkOut; // 호텔 체크아웃 날짜

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;

    @OneToOne(mappedBy = "hotel")
    private Favorites favorite;


}

