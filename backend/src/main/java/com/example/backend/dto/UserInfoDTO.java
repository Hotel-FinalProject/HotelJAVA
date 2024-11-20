package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    int userAllCount;
    int hotelAllCount;
    int userActiveCount;
    int userUnActiveCount;
    int hotelActiveCount;
    int hotelUnActiveCount;
}
