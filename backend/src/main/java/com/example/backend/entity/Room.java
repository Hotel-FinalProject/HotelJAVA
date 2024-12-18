package com.example.backend.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Entity;
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

    private String type; // 객실 유형
    private Long total; // 총 객실 수
    private BigDecimal price; // 객실 가격
    private String description; // 객실 설명
    private Long occupancy; // 기준 인원

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // 호텔 관리 번호 (외래키)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false) // 예약 관리 번호 (외래키)
    private Reservation reservation;

    @OneToMany(mappedBy = "room")
    private List<RoomImage> images;
}

