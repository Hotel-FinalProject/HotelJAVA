package com.example.backend.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.service.HotelRoomSummaryService;

@RestController
@RequestMapping("/api/hotels")
public class HotelSummaryController {

    @Autowired
    private HotelRoomSummaryService hotelRoomSummaryService;

    @GetMapping("/{hotelId}/room-summary")
    public ResponseEntity<Map<String, Object>> getHotelRoomSummary(@PathVariable("hotelId") Long hotelId) {
        Map<String, Object> summary = hotelRoomSummaryService.calculateHotelRoomSummary(hotelId);
        return ResponseEntity.ok(summary);
    }
}

