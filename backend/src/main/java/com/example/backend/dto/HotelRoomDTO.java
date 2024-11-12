package com.example.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelRoomDTO {
	
	// 호텔
	private Long hotelId;
    private String name;
    private String address;
    private String imageUrl;
    private Double rating;
    private Double mapX;
    private Double mapY;
    private String hotelnum;
    private String checkIn;
    private String checkOut;
    
    private List<RoomDetailDTO> rooms;
}
