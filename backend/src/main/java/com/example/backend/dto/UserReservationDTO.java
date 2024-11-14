package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserReservationDTO {

    // 호텔 이름, 객실명,체크인/아웃, 숙박인원, 요청사항
    String hotelName;
    String roomName;
    LocalDate checkIn;
    LocalDate checkOut;
    int guestNum;
    String request;
    String status;

    String uid;
    String paymentStatus;

}