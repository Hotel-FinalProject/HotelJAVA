package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateDTO {
    private String status; // 예약 상태
    private LocalDate checkIn; // 체크인 날짜
    private LocalDate checkOut; // 체크아웃 날짜
    private String request; // 요청 사항
}
