package com.example.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.HotelDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.service.HotelService;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // API 호출
    @PostMapping("/hotels/fetch")
    public String fetchAndSaveHotels() {
        hotelService.fetchAndSaveHotels();
        return "호텔 데이터가 업데이트되었습니다.";
    }

    // 모든 호텔 조회
    @GetMapping("/hotels")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
    
    // 특정 호텔 조회
    @GetMapping("/hotels/{id}")
    public Hotel getHotelById(@PathVariable("id") Long id) {
        return hotelService.getHotelById(id);
    }
    
 // 10개 랜덤 호텔 조회
    @GetMapping("/hotels/random")
    public List<HotelDTO> getRandomHotels() {
        return hotelService.getRandomHotels(10); // 10개 랜덤 호텔 반환
    }

    // 검색 엔드포인트 추가
    @GetMapping("/hotels/search")
    public List<HotelDTO> searchHotels(@RequestParam("query") String query) {
        return hotelService.searchHotelsByName(query);
    }
}

