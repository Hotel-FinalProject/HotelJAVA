package com.example.backend.entity;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId; // 객실 관리 번호

    private String name; // 객실 유형
    private int total; // 총 객실 수
    private BigDecimal price; // 객실 가격
    @Column(length = 1000)
    private String description; // 객실 설명

    private String type; // 객실 유형
    private Long occupancy; // 기준 인원

    // 편의시설 관련 필드
    private boolean bathFacility; // 목욕 시설
    private boolean bath; // 욕조
    private boolean airCondition;
    private boolean tv;
    private boolean cable;
    private boolean internet;
    private boolean refrigerator; // 냉장고
    private boolean toiletries; // 세면도구
    private boolean sofa;
    private boolean tableYn; // 테이블
    private boolean hairdryer; // 드라이기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false) // 호텔 관리 번호 (외래키)
//    @JsonBackReference
    private Hotel hotel;

    @OneToMany(mappedBy = "rooms")
//    @JsonManagedReference
    private List<Reservation> reservation;

    @OneToMany(mappedBy = "room")
//    @JsonManagedReference
    private List<RoomImage> images;
}