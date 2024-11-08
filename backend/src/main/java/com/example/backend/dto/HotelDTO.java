package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDTO {
	
	private Long hotelId;
    private String name;
    private String address;
    private String imageUrl;
    private Double rating;
}
