package com.example.backend.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReviewResponseDTO {
    private Long reviewId;
    private String content;
    private Float rating;
    private Date writeDate;
    private Date updateDate;
    private List<String> imageUrl;
    private Long reservationId;
    private Long userId;
    private String userName;
    private Long hotelId;
}
