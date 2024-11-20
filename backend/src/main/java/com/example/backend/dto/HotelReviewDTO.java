package com.example.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelReviewDTO {
    private Long hotelId;
    private String name;
    private String address;
    private String imageUrl;
    private Double rating; // 평점
    private Double mapX;
    private Double mapY;
    private BigDecimal roomPrice;
    private int reviewCount; // 리뷰 개수 추가
}
