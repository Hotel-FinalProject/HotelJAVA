package com.example.backend.Controller;

import com.example.backend.service.HotelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HotelApiController {

    @Autowired
    private HotelApiService hotelService;

    @GetMapping("/hotel_list")
    public String fetchHotels() {
        hotelService.saveHotel();
        return "호텔 정보를 성공적으로 가져와서 저장했습니다.";
    }
}
