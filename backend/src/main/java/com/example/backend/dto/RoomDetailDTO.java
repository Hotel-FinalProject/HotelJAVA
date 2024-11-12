package com.example.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDetailDTO {
	
	// 객실
    private Long roomId;
    private String roomType;// 객실 유형
    private Long roomOccupancy;// 객실 인원
    private BigDecimal roomPrice;// 객실 요금
    
    // 객실 이미지
    private Long roomImageId;
    private String roomImageUrl;
    
    // 객실 잔여 수
    private Long roomCountId;
    private int roomCount;
}
