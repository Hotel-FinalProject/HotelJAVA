package com.example.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dto.RoomSummaryDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.RoomRepository;

@Service
public class HotelRoomSummaryService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * 특정 호텔의 총 객실 수와 유형별 객실 수를 계산하는 메서드
     *
     * @param hotelId 호텔 ID
     * @return 총 객실 수와 유형별 객실 수를 포함하는 Map
     */
    public Map<String, Object> calculateHotelRoomSummary(Long hotelId) {
        // 호텔 엔티티 가져오기
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 호텔을 찾을 수 없습니다. ID: " + hotelId));

        // 호텔에 포함된 모든 객실 가져오기
        List<Room> rooms = roomRepository.findByHotel(hotel);

        // 총 객실 수 계산
        int totalRooms = rooms.stream().mapToInt(Room::getTotal).sum();

        // 유형별 객실 수 계산
        Map<String, Integer> roomTypeCounts = rooms.stream()
                .collect(Collectors.groupingBy(Room::getType, Collectors.summingInt(Room::getTotal)));

        // 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("totalRooms", totalRooms);
        result.put("roomTypeCounts", roomTypeCounts);

        return result;
    }
}
