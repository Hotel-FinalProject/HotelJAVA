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
	private List<String> imageUrls;
	private String primaryImageUrl;
	private int availableRooms;

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

	private String hotelPhone;
	private String hotelAddress;
	private String hotelCheckIn;
	private String hotelCheckOut;
}