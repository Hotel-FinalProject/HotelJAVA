package com.example.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.service.TourApiService;

@RestController
@RequestMapping("/api")
public class HotelController {

    private final TourApiService tourApiService;

    public HotelController(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    @GetMapping("/hotels")
    public String getHotels(@RequestParam(name = "cityName") String cityName) {
        return tourApiService.getHotelData(cityName);
    }

}

