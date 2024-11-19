package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationTodayDTO {
    String roomName;
    String userName;
    String phone; // 새로 추가된 필드
    LocalDate checkIn;
    LocalDate checkOut;
    String request;
    int guest;
    String status;
}
