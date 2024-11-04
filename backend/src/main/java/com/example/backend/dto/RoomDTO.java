package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RoomDTO {
	 	private Long roomId;
	    private String name;
	    private BigDecimal price;
	    private String description;
	    private Long occupancy;
	    private List<String> imageUrls;private int availableRooms; // 남은 객실 수 필드 추가
}
