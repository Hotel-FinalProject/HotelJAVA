package com.example.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    private Long roomId; // 예약할 객실 ID
    private int guestNum; // 투숙 인원
    private Date checkIn; // 체크인 날짜
    private Date checkOut; // 체크아웃 날짜

    private Long userId;
}
