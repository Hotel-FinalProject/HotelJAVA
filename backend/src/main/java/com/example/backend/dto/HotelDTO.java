package com.example.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
	
	private Long hotelId;
    private String name;
    private String address;
    private String imageUrl;
    private Double rating;
    private Double mapX;
    private Double mapY;
    
    // 객실
    private BigDecimal roomPrice;
}
