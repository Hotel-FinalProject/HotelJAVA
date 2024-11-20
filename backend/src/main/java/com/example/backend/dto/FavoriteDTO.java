package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDTO {
    String hotelName;
    String hotelImage;
    Long hotelId;
    Boolean status;
}
