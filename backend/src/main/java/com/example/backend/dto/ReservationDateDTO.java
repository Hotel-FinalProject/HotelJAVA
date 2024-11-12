package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationDateDTO {
    //객실명, 예약자명, 체크인/체크아웃, 요청사항,투숙인원, 예약상태
    String roomName;
    String userName;
    LocalDate checkIn;
    LocalDate checkOut;
    String request;
    int guest;
    String status;
}
