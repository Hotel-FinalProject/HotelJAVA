package com.example.backend.Controller;

import com.example.backend.dto.RoomDTO;
import com.example.backend.entity.Room;
import com.example.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // 특정 호텔 객실 API 호출
    @PostMapping("/fetch/{contentId}")
    public ResponseEntity<String> fetchAndSaveRooms(@PathVariable("contentId") Long contentId) {
        try {
            roomService.fetchAndSaveRooms(contentId);
            return ResponseEntity.ok("Room data fetched and saved successfully");
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid URI syntax: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching and saving room data: " + e.getMessage());
        }
    }

    // 모든 호텔 객실 API 호출
    @PostMapping("/fetch/all")
    public ResponseEntity<String> fetchAndSaveAllRooms() {
        try {
            roomService.fetchAndSaveAllRooms();
            return ResponseEntity.ok("All room data fetched and saved successfully");
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid URI syntax: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching and saving all room data: " + e.getMessage());
        }
    }

    // 모든 호텔 객실 조회
    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // 특정 호텔 객실 조회 (RoomDTO로 반환)
    @GetMapping("/hotel/{contentId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByContentId(@PathVariable("contentId") Long contentId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotelId(contentId);
        return ResponseEntity.ok(rooms);
    }

    // 특정 객실 조회 (RoomDTO로 반환)
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable("roomId") Long roomId) {
        RoomDTO room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }
    
    // 객실 요약 정보
    @GetMapping("/hotel/{hotelId}/room-summary")
    public ResponseEntity<Map<String, Object>> getRoomSummaryByHotel(@PathVariable("hotelId") Long hotelId) {
        Map<String, Object> roomSummary = roomService.getRoomSummaryByHotel(hotelId);
        return ResponseEntity.ok(roomSummary);
    }
    
    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable("roomId") Long roomId, @RequestBody RoomDTO roomDto) {
        roomService.updateRoom(roomId, roomDto);
        return ResponseEntity.ok("객실 정보가 수정되었습니다.");
    }
}