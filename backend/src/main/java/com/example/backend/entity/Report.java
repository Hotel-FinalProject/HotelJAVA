package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId; // 신고 관리 번호

    @ManyToOne
    @JoinColumn(name = "reporter_id", referencedColumnName = "reporterId", nullable = false) // 신고한 사람 (외래키)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_id",referencedColumnName = "reportedId", nullable = false) // 신고 당한 사람 (외래키)
    private User reported;

    @ManyToOne
    @JoinColumn(name = "reservation_id",referencedColumnName = "reservationId", nullable = false ) // 예약 관리 번호 (외래키)
    private Reservation reservation;
}

