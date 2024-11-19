
package com.example.backend.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.backend.dto.HotelReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.HotelDTO;
import com.example.backend.dto.HotelRoomDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
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
//    @GetMapping("/hotels/{id}")
//    public Hotel getHotelById(@PathVariable("id") Long id) {
//        return hotelService.getHotelById(id);
//    }
    
 // 10개 랜덤 호텔 조회
    @GetMapping("/hotels/random")
    public List<HotelDTO> getRandomHotels() {
        return hotelService.getRandomHotels(10); // 10개 랜덤 호텔 반환
    }

    // 검색 엔드포인트 추가
//    @GetMapping("/hotels/search")
//    public List<HotelDTO> searchHotels(@RequestParam("query") String query) {
//        return hotelService.searchHotelsByName(query);
//    }
//    @GetMapping("/hotels/search")
//    public List<HotelDTO> searchHotels(@RequestParam(value = "query", required = false) String query) {
//        if (query == null || query.isEmpty()) {
//            // 검색어가 없으면 전체 호텔 목록 반환
//            return hotelService.searchHotelsByLocation(null);
//        }
//        // 검색어가 있으면 해당 구에 맞는 호텔 목록 반환
//        return hotelService.searchHotelsByLocation(query);
//    }
    
    
    
    
    
    
//    @GetMapping("/hotels/search")
//    public List<HotelDTO> searchHotels(@RequestParam(value = "query", required = false) String query) {
//        if (query == null || query.isEmpty()) {
//            // 검색어가 없으면 전체 호텔 목록 반환 (지도 표시용)
//            return hotelService.getAllHotels().stream()
//                .map(hotel -> new HotelDTO(
//                    hotel.getHotelId(),
//                    hotel.getName(),
//                    hotel.getAddress(),
//                    hotel.getImageUrl(),
//                    hotel.getRating(),
//                    hotel.getMapX(),
//                    hotel.getMapY()
//                ))
//                .collect(Collectors.toList());
//        }
//        // 검색어가 있으면 해당 구에 맞는 호텔 목록 반환
//        return hotelService.searchHotelsByName(query);
//    }
    
    @GetMapping("/hotels/search")
    public List<HotelDTO> searchHotels(@RequestParam(value = "query", required = false) String query) {
        if (query == null || query.isEmpty()) {
            return hotelService.getAllHotels().stream()
                .map(hotel -> new HotelDTO(
                    hotel.getHotelId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getImageUrl(),
                    hotelService.calculateAverageRating(hotel),
                    hotel.getMapX(),
                    hotel.getMapY(),
                    null
                ))
                .collect(Collectors.toList());
        }
        return hotelService.searchHotelsByNameOrAddress(query);
    }



//    // 특정 호텔 조회 (HotelDetailDTO로 반환)
//    @GetMapping("/hotels/{id}")
//    public HotelRoomDTO getHotelDetailById(@PathVariable("id") Long id) {
//        return hotelService.getHotelDetailById(id);
//    }
    
    // 특정 호텔 상세 정보 조회 (HotelRoomDTO로 반환)
//    @GetMapping("/hotels/{id}")
//    public HotelRoomDTO getHotelDetailById(@PathVariable("id") Long id) {
//        return hotelService.getHotelDetailById(id);
//    }
    
    @GetMapping("/hotels/{id}")
    public HotelRoomDTO getHotelDetailById(@PathVariable("id") Long id) {
        return hotelService.getHotelDetailById(id);
    }
    
    @GetMapping("/hotels/search-by-date-and-guest")
    public List<HotelDTO> searchHotelsByDateAndGuest(
            @RequestParam(value = "checkInDate", required = false) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate", required = false) LocalDate checkOutDate,
            @RequestParam(value = "guests", required = false, defaultValue = "1") int guests,
            @RequestParam(value = "query", required = false) String query) {
        return hotelService.searchHotelsByDateAndGuest(checkInDate, checkOutDate, guests, query);
    }


    @GetMapping("/hotels/top10")
    public Map<String, List<HotelReviewDTO>> getTop10Hotels() {
        List<HotelReviewDTO> topHotelsByReviewCount = hotelService.getTop10HotelsByReviewCount();
        List<HotelReviewDTO> topHotelsByRating = hotelService.getTop10HotelsByRating();

        Map<String, List<HotelReviewDTO>> response = new HashMap<>();
        response.put("topByReviewCount", topHotelsByReviewCount);
        response.put("topByRating", topHotelsByRating);

        return response;
    }
}
