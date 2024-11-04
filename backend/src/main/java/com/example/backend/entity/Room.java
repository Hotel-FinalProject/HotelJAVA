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
import jakarta.persistence.Table;
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
    private int total = 10; // 총 객실 수
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false) // 호텔 관리 번호 (외래키)
    @JsonBackReference // 순환 참조 방지
    private Hotel hotel;
    
    @OneToMany(mappedBy = "rooms")
    @JsonManagedReference // Reservation과의 관계에서 Room이 Managed Reference
    private List<Reservation> reservation;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference // Room이 RoomImage와의 관계에서 Managed Reference
    private List<RoomImage> images;
}

