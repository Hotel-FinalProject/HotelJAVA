package com.example.backend.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId; // 예약 관리 번호

    @Column(nullable = false)
    private String status; // 예약 상태
    
    @Column(nullable = true)
    private String request; // 요청 사항
    
    @Column(nullable = false)
    private int guestNum; // 투숙 인원
    
    private String paymentStatus; // 결제 상태
    
    private Date checkIn;
    private Date checkOut;    

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId", nullable = false) // 고객 아이디 (외래키)
    private User user;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room rooms;

    @OneToOne(mappedBy = "reservation")
    private Review review;
}

