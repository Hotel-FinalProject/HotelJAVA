package com.example.backend.entity;

import java.util.Date;

import jakarta.persistence.Column;
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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId; // 후기 관리 번호

    private String content; // 후기 내용
    private Long rating; // 평점
    private Date writeDate; // 후기 작성 날짜
    private Date updateDate; // 후기 수정 날짜

    @Column(nullable = true)
    private String imageUrl; // 후기 이미지 URL (S3 이미지 경로)

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false) // 예약 관리 번호 (외래키)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 고객 관리 번호 (외래키)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // 호텔 관리 번호 (외래키)
    private Hotel hotel;
}

