package com.example.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewResponseDTO {
    private Long reviewId;
    private String content;
    private Float rating;
    private Date writeDate;
    private Date updateDate;
    private String imageUrl;
    private Long reservationId;
    private Long userId;
    private String userName;
    private Long hotelId;
}
