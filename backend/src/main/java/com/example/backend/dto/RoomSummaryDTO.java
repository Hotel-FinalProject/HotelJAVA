package com.example.backend.dto;

import com.example.backend.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSummaryDTO {
	private String roomType; // 객실 타입
    private int roomCount;   // 남은 객실 수
    private Room room;
}
