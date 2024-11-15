package com.example.backend.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId; // 후기 관리 번호

    private String content; // 후기 내용
    private Float rating; // 평점
    @CreatedDate
    private Date writeDate; // 후기 작성 날짜
    @LastModifiedDate
    private Date updateDate; // 후기 수정 날짜

    @Column(nullable = true)
    private List<String> imageUrl; // 후기 이미지 URL (S3 이미지 경로)

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false) // 예약 관리 번호 (외래키)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false) // 고객 관리 번호 (외래키)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotelId", nullable = false) // 호텔 관리 번호 (외래키)
    private Hotel hotel;
}

