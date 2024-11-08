package com.example.backend.dto;

import com.example.backend.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long reservationId;
    private int guestNum; // 투숙 인원
    private LocalDateTime checkIn; // 체크인 날짜
    private LocalDateTime checkOut; // 체크아웃 날짜
    private String request;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Long roomId;

    public ReservationDTO (Reservation reservation){
        this.reservationId = reservation.getReservationId();
        this.guestNum = reservation.getGuestNum();
        this.checkIn = reservation.getCheckIn();
        this.checkOut = reservation.getCheckOut();
        this.request= reservation.getRequest();
        this.createDate = reservation.getCreateDate();
        this.updateDate = reservation.getCreateDate();
        this.roomId = reservation.getRooms().getRoomId();
    }
}

