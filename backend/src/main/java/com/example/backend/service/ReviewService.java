package com.example.backend.service;

import com.example.backend.entity.Review;
import com.example.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // 리뷰 조회 (단일)
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));
    }

    // 리뷰 목록 조회 (호텔 별)
    public List<Review> getReviewsByHotel(Long hotelId) {
        return reviewRepository.findAll(); // 필요시 JPQL이나 Query Method를 사용해 특정 호텔의 리뷰만 조회하도록 수정 가능합니다.
    }

    // 유저별 리뷰 조회
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    // 리뷰 수정
    @Transactional
    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = getReview(reviewId);
        existingReview.setContent(updatedReview.getContent());
        existingReview.setRating(updatedReview.getRating());
        existingReview.setImageUrl(updatedReview.getImageUrl());
        existingReview.setUpdateDate(new java.util.Date()); // 수정 날짜 갱신
        return existingReview;
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
