package com.example.backend.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
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
    private Long total; // 총 객실 수
    private BigDecimal price; // 객실 가격
    @Column(length = 1000)
    private String description; // 객실 설명
    private Long occupancy; // 기준 인원
    
    // 편의시설 관련 필드
    private boolean bathFacility;
    private boolean bath;
    private boolean airCondition;
    private boolean tv;
    private boolean cable;
    private boolean internet;
    private boolean refrigerator;
    private boolean toiletries;
    private boolean sofa;
    private boolean tableYn;
    private boolean hairdryer;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // 호텔 관리 번호 (외래키)
    private Hotel hotel;


    @OneToMany(mappedBy = "room")
    private List<RoomImage> images;
}

