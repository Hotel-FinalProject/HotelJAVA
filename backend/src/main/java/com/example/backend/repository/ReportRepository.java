package com.example.backend.repository;

import com.example.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    int countByStatus(String 신고처리_완료);

    int countByReportId(Long reportId);

    List<Report> findByReview_ReviewId(Long reviewId);
}
