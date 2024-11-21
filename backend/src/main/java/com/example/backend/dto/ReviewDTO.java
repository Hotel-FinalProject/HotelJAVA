package com.example.backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class ReviewDTO {
    private String content;
    private Float rating;
    private List<MultipartFile> imageUrls;
    private Long reservationId;
    private Long userId;
    private Long hotelId;
    private String hotelName;
}
