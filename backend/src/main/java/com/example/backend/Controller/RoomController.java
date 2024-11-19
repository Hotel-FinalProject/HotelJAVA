package com.example.backend.Controller;

import com.example.backend.dto.RoomDTO;
import com.example.backend.entity.Room;
import com.example.backend.service.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDate;
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
    
    // 호텔 객실 요약 정보
    @GetMapping("/hotel/{hotelId}/room-summary")
    public Map<String, Object> getRoomSummary(
        @PathVariable("hotelId") Long hotelId, 
        @RequestParam("date") LocalDate date) {
        return roomService.getRoomSummary(hotelId, date);
    }
//    @GetMapping("/hotel/{hotelId}/room-summary")
//    public ResponseEntity<?> getRoomSummary(
//        @PathVariable Long hotelId,
//        @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//    ) {
//        LocalDate targetDate = (date != null) ? date : LocalDate.now();
//        Map<String, Object> roomSummary = roomService.getRoomSummary(hotelId, targetDate);
//        return ResponseEntity.ok(roomSummary);
//    }
//    @GetMapping("/hotel/{hotelId}/room-summary")
//    public ResponseEntity<?> getRoomSummary(
//        @PathVariable Long hotelId,
//        @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//    ) {
//        LocalDate targetDate = (date != null) ? date : LocalDate.now();
//        Map<String, Object> roomSummary = roomService.getRoomSummary(hotelId, targetDate);
//        return ResponseEntity.ok(roomSummary);
//    }

}