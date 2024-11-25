
package com.example.backend.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.example.backend.dto.HotelReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.HotelDTO;
import com.example.backend.dto.HotelRoomDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.repository.HotelRepository;
import com.example.backend.service.HotelService;
import com.example.backend.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    private final JwtUtil jwtUtil;
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelController(HotelService hotelService, JwtUtil jwtUtil, HotelRepository hotelRepository) {
        this.hotelService = hotelService;
        this.jwtUtil = jwtUtil;
        this.hotelRepository = hotelRepository;
    }

    /**
     * JWT 토큰을 사용해 managerId와 userId를 비교하여 해당 호텔의 hotelId를 추출
     *
     * @param request HttpServletRequest 객체 (Authorization 헤더에서 JWT 토큰 추출)
     * @return hotelId 또는 에러 메시지
     */
    @GetMapping("/manager-hotel-id")
    public ResponseEntity<?> getHotelIdByManager(@RequestHeader("Authorization") String token) {
        try {
            // Bearer 토큰에서 실제 토큰 추출
            String actualToken = token.replace("Bearer ", "");

            // JWT에서 userId 추출
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            // userId와 managerId가 같은 호텔 조회
            Optional<Hotel> hotelOptional = hotelRepository.findByManager_UserId(userId);
            if (hotelOptional.isPresent()) {
                Long hotelId = hotelOptional.get().getHotelId();
                return ResponseEntity.ok(hotelId);
            } else {
                return ResponseEntity.status(404).body("해당 관리자가 관리하는 호텔이 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 JWT 형식: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("JWT 인증 실패: " + e.getMessage());
        }
    }

    @GetMapping("/manager/hotel")
    public ResponseEntity<?> getManagerHotelInfo(@RequestHeader("Authorization") String token) {
        try {
            // JWT에서 userId 추출
            String actualToken = token.replace("Bearer ", "");
            Long userId = jwtUtil.verifyJwtAndGetUserId(actualToken);

            // userId와 managerId가 같은 호텔 조회
            Optional<Hotel> hotelOptional = hotelRepository.findByManager_UserId(userId);
            if (hotelOptional.isPresent()) {
                Hotel hotel = hotelOptional.get();
                HotelDTO hotelDTO = new HotelDTO(
                        hotel.getHotelId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getImageUrl(),
                        hotelService.calculateAverageRating(hotel),
                        hotel.getMapX(),
                        hotel.getMapY(),
                        null
                );
                return ResponseEntity.ok(hotelDTO);
            } else {
                return ResponseEntity.status(404).body("해당 관리자가 관리하는 호텔이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("JWT 인증 실패: " + e.getMessage());
        }
    }
	
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
    
    // 10개 랜덤 호텔 조회
    @GetMapping("/hotels/random")
    public List<HotelReviewDTO> getRandomHotels() {
        return hotelService.getRandomHotels(10); // 10개 랜덤 호텔 반환
    }

    // 호텔 검색
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
    
	// 특정 호텔 조회
    @GetMapping("/hotels/{id}")
    public HotelRoomDTO getHotelDetailById(@PathVariable("id") Long id) {
        return hotelService.getHotelDetailById(id);
    }
    
// 날짜, 인원 조건 검색
    @GetMapping("/hotels/search-by-date-and-guest")
    public List<HotelDTO> searchHotelsByDateAndGuest(
            @RequestParam(value = "checkInDate", required = false) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate", required = false) LocalDate checkOutDate,
            @RequestParam(value = "guests", required = false, defaultValue = "1") int guests,
            @RequestParam(value = "query", required = false) String query) {
        return hotelService.searchHotelsByDateAndGuest(checkInDate, checkOutDate, guests, query);
    }

    // (T)
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