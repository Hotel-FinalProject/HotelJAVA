package com.example.backend.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    private Date checkIn;
    private Date checkOut;

    private String paymentStatus; // 결제 상태

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId", nullable = false) // 고객 아이디 (외래키)
    private User user;


    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false) // 예약 관리 번호 (외래키)

    private Payments payment;

//    @ManyToOne
//    @JoinColumn(name = "room_id", nullable = true)
//    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room rooms;

    @OneToOne(mappedBy = "reservation")
    private Review review;
}

