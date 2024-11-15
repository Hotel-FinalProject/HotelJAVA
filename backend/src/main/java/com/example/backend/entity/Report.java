package com.example.backend.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String status = "신고 접수됨";

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "reviewId", nullable = false) // 리뷰 ID (외래키)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "reporter_id", referencedColumnName = "userId", nullable = false) // 신고한 사용자 (외래키)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_id", referencedColumnName = "userId", nullable = false) // 신고당한 사용자 (외래키)
    private User reported;
}
