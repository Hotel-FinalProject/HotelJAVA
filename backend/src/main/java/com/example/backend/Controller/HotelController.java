
package com.example.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.Hotel;
import com.example.backend.service.HotelService;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // Tour API에서 데이터 가져와서 DB에 저장
    @PostMapping("/hotels/fetch")
    public String fetchAndSaveHotels() {
        hotelService.fetchAndSaveHotels();
        return "호텔 데이터가 업데이트되었습니다.";
    }

    // DB에서 호텔 리스트 조회
    @GetMapping("/hotels")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
}
