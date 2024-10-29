package com.example.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    
    // 객실 데이터 가져오기 및 DB 저장 엔드포인트
    @PostMapping("/fetch")
    public String fetchAndSaveRoomData(@RequestParam String apiUrl, @RequestBody Hotel hotel) {
        roomService.fetchAndSaveRoomData(apiUrl, hotel);
        return "Room data fetched and saved successfully.";
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }
}
