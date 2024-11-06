package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId; // 객실 이미지 번호

    @Column(nullable = true)
    private String imageUrl; // 객실 이미지 URL (S3 이미지 경로)
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) // 객실 관리 번호 (외래키)
    @JsonBackReference
    private Room room;
}

