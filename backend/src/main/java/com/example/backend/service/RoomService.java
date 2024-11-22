package com.example.backend.service;

import java.util.stream.Collectors;
import com.example.backend.dto.RoomDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;
import com.example.backend.entity.RoomImage;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.RoomCountRepository;
import com.example.backend.repository.RoomRepository;
import com.example.backend.repository.RoomImageRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final HotelRepository hotelRepository;
    private final RoomCountRepository roomCountRepository;
    private final ReservationRepository reservationRepository;
    private final String API_URL = "http://apis.data.go.kr/B551011/KorService1/detailInfo1";
    //private final String SERVICE_KEY = ""; // API 키 입력

    @Value("${api.key}")
    private  String SERVICE_KEY;

    @Transactional
    public void fetchAndSaveAllRooms() throws URISyntaxException {
        List<Hotel> hotels = hotelRepository.findAll();
        for (Hotel hotel : hotels) {
            try {
                fetchAndSaveRooms(hotel.getContentId());
            } catch (Exception e) {
                System.out.println("Error fetching rooms for contentId " + hotel.getContentId() + ": " + e.getMessage());
            }
        }
    }

    @Transactional
    public void fetchAndSaveRooms(Long contentId) throws URISyntaxException {
        Hotel hotel = hotelRepository.findByContentId(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contentId"));

        String url = String.format("%s?ServiceKey=%s&contentTypeId=32&contentId=%s&MobileOS=ETC&MobileApp=AppTest",
                API_URL, SERVICE_KEY, contentId);
        URI uri = new URI(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/xml; charset=UTF-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
        String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);

        JSONObject jsonResponse = XML.toJSONObject(xmlResponse);

        JSONArray items = jsonResponse.optJSONObject("response")
                .optJSONObject("body")
                .optJSONObject("items")
                .optJSONArray("item");

        if (items == null) {
            items = new JSONArray();
        }

        // 중복 방지 로직 추가
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            String roomName = item.optString("roomtitle", "Unknown");
            boolean roomExists = roomRepository.existsByHotelAndName(hotel, roomName);
            if (roomExists) {
                continue; // 이미 존재하는 객실은 건너뜀
            }

            Room room = new Room();
            room.setHotel(hotel);
            room.setName(roomName);
            room.setPrice(new BigDecimal(100000)); // 임의 가격 설정
            room.setDescription(item.optString("roomintro", "No description available"));
            room.setOccupancy(item.optLong("roombasecount", 2L));
            room.setBathFacility(item.optString("roombathfacility", "N").equals("Y"));
            room.setBath(item.optString("roombath", "N").equals("Y"));
            room.setAirCondition(item.optString("roomaircondition", "N").equals("Y"));
            room.setTv(item.optString("roomtv", "N").equals("Y"));
            room.setCable(item.optString("roomcable", "N").equals("Y"));
            room.setInternet(item.optString("roominternet", "N").equals("Y"));
            room.setRefrigerator(item.optString("roomrefrigerator", "N").equals("Y"));
            room.setToiletries(item.optString("roomtoiletries", "N").equals("Y"));
            room.setSofa(item.optString("roomsofa", "N").equals("Y"));
            room.setTableYn(item.optString("roomtable", "N").equals("Y"));
            room.setHairdryer(item.optString("roomhairdryer", "N").equals("Y"));

            roomRepository.save(room);

            List<RoomImage> images = saveRoomImages(item, room);
            roomImageRepository.saveAll(images);
            room.setImages(images);
        }
    }



    private List<Room> parseAndSaveRooms(JSONArray items, Hotel hotel) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            Room room = new Room();
            room.setHotel(hotel);
            room.setName(item.optString("roomtitle", "Unknown"));
            room.setTotal(item.optInt("total",10)); // 유형별 객실 수 10개로.
            room.setPrice(new BigDecimal(100000)); // 임의 가격 설정
            room.setDescription(item.optString("roomintro", "No description available"));
            room.setOccupancy(item.optLong("roombasecount", 2L));
            room.setBathFacility(item.optString("roombathfacility", "N").equals("Y"));
            room.setBath(item.optString("roombath", "N").equals("Y"));
            room.setAirCondition(item.optString("roomaircondition", "N").equals("Y"));
            room.setTv(item.optString("roomtv", "N").equals("Y"));
            room.setCable(item.optString("roomcable", "N").equals("Y"));
            room.setInternet(item.optString("roominternet", "N").equals("Y"));
            room.setRefrigerator(item.optString("roomrefrigerator", "N").equals("Y"));
            room.setToiletries(item.optString("roomtoiletries", "N").equals("Y"));
            room.setSofa(item.optString("roomsofa", "N").equals("Y"));
            room.setTableYn(item.optString("roomtable", "N").equals("Y"));
            room.setHairdryer(item.optString("roomhairdryer", "N").equals("Y"));

            roomRepository.save(room);

            List<RoomImage> images = saveRoomImages(item, room);
            roomImageRepository.saveAll(images);

            room.setImages(images);

            rooms.add(room);
        }
        return rooms;
    }

    private List<RoomImage> saveRoomImages(JSONObject item, Room room) {
        List<RoomImage> images = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            String imageUrlKey = "roomimg" + j;
            if (item.has(imageUrlKey) && !item.isNull(imageUrlKey)) {
                RoomImage roomImage = new RoomImage();
                roomImage.setImageUrl(item.getString(imageUrlKey));
                roomImage.setRoom(room);
                images.add(roomImage);
            }
        }
        return images;
    }

    public List<Room> getRoomsByContentId(Long contentId) {
        Hotel hotel = hotelRepository.findByContentId(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contentId"));
        return roomRepository.findByHotel(hotel);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));

        List<Room> rooms = roomRepository.findByHotel(hotel);

        return rooms.stream().map(room -> {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomId(room.getRoomId());
            roomDTO.setName(room.getName());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setDescription(room.getDescription());
            roomDTO.setOccupancy(room.getOccupancy());
            roomDTO.setImageUrls(room.getImages().stream()
                    .map(RoomImage::getImageUrl)
                    .collect(Collectors.toList()));
            roomDTO.setAvailableRooms(getAvailableRooms(room));

            roomDTO.setBathFacility(room.isBathFacility());
            roomDTO.setBath(room.isBath());
            roomDTO.setAirCondition(room.isAirCondition());
            roomDTO.setTv(room.isTv());
            roomDTO.setCable(room.isCable());
            roomDTO.setInternet(room.isInternet());
            roomDTO.setRefrigerator(room.isRefrigerator());
            roomDTO.setToiletries(room.isToiletries());
            roomDTO.setSofa(room.isSofa());
            roomDTO.setTableYn(room.isTableYn());
            roomDTO.setHairdryer(room.isHairdryer());

            return roomDTO;
        }).collect(Collectors.toList());
    }

    public List<RoomDTO> getAllRoomsAsDTO() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room -> {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomId(room.getRoomId());
            roomDTO.setName(room.getName());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setDescription(room.getDescription());
            roomDTO.setOccupancy(room.getOccupancy());
            roomDTO.setImageUrls(room.getImages().stream()
                    .map(RoomImage::getImageUrl)
                    .collect(Collectors.toList()));
            roomDTO.setAvailableRooms(getAvailableRooms(room));
            return roomDTO;
        }).collect(Collectors.toList());
    }

    // 특정 날짜의 남은 객실 수 계산
    private int getAvailableRooms(Room room) {
        RoomCount roomCount = roomCountRepository.findByRoomAndDate(room, LocalDate.now())
                .orElse(null);
        if (roomCount == null) {
            return 10;
        }

        return roomCount.getRoomCount();
    }
//    private int getAvailableRooms(Room room) {
//        RoomCount roomCount = roomCountRepository.findByRoomAndDate(room, LocalDate.now())
//                .orElse(null);
//        return (roomCount != null) ? roomCount.getRoomCount() : 10; // 기본 10개 반환
//    }

    public RoomDTO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(room.getRoomId());
        roomDTO.setName(room.getName());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setOccupancy(room.getOccupancy());
        roomDTO.setImageUrls(room.getImages().stream()
                .map(RoomImage::getImageUrl)
                .collect(Collectors.toList()));

        List<String> imageUrls = room.getImages().stream()
                .map(RoomImage::getImageUrl)
                .collect(Collectors.toList());
        roomDTO.setImageUrls(imageUrls);
        roomDTO.setPrimaryImageUrl(!imageUrls.isEmpty() ? imageUrls.get(0) : null);


        roomDTO.setAvailableRooms(getAvailableRooms(room));

        roomDTO.setBathFacility(room.isBathFacility());
        roomDTO.setBath(room.isBath());
        roomDTO.setAirCondition(room.isAirCondition());
        roomDTO.setTv(room.isTv());
        roomDTO.setCable(room.isCable());
        roomDTO.setInternet(room.isInternet());
        roomDTO.setRefrigerator(room.isRefrigerator());
        roomDTO.setToiletries(room.isToiletries());
        roomDTO.setSofa(room.isSofa());
        roomDTO.setTableYn(room.isTableYn());
        roomDTO.setHairdryer(room.isHairdryer());

        Hotel hotel = room.getHotel();
        roomDTO.setHotelPhone(hotel.getHotelnum());
        roomDTO.setHotelAddress(hotel.getAddress());
        roomDTO.setHotelCheckIn(hotel.getCheckIn());
        roomDTO.setHotelCheckOut(hotel.getCheckOut());

        return roomDTO;
    }
    
    // 객실 요약 정보
//    @Transactional(readOnly = true)
//    public Map<String, Object> getRoomSummaryByHotel(Long hotelId) {
//        // 호텔 정보 가져오기
//        Hotel hotel = hotelRepository.findById(hotelId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 호텔이 존재하지 않습니다. ID: " + hotelId));
//
//        // 호텔의 객실 정보 가져오기
//        List<Room> rooms = roomRepository.findByHotel(hotel);
//
//        // 총 객실 수 계산
//        int totalRooms = rooms.stream().mapToInt(Room::getTotal).sum();
//
//        // 유형별 객실 수 계산
//        Map<String, Integer> roomTypeCounts = rooms.stream()
//                .collect(Collectors.groupingBy(Room::getName, Collectors.summingInt(Room::getTotal)));
//
//        // 결과 반환
//        Map<String, Object> summary = new HashMap<>();
//        summary.put("totalRooms", totalRooms);
//        summary.put("roomTypeCounts", roomTypeCounts);
//
//        return summary;
//    }
    // + 예약 반영하여 객실 수 표시
    public Map<String, Object> getRoomSummaryByHotel(Long hotelId) {
        // 예약 상태를 반영한 객실 수 업데이트
        updateRoomCountBasedOnReservations(hotelId, LocalDate.now());

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 호텔이 존재하지 않습니다. ID: " + hotelId));

        List<Room> rooms = roomRepository.findByHotel(hotel);

        LocalDate today = LocalDate.now();
        int totalAvailableRooms = 0;
        Map<String, Integer> roomTypeCounts = new HashMap<>();

        for (Room room : rooms) {
            RoomCount roomCount = roomCountRepository.findByRoomAndDate(room, today).orElse(null);
            int availableRooms = roomCount != null
                    ? roomCount.getRoomCount()
                    : room.getTotal();
            totalAvailableRooms += availableRooms;
            roomTypeCounts.put(
                    room.getName(),
                    roomTypeCounts.getOrDefault(room.getName(), 0) + availableRooms
            );
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRooms", totalAvailableRooms);
        summary.put("roomTypeCounts", roomTypeCounts);

        return summary;
    }

    
    // 수정
    public void updateRoom(Long roomId, RoomDTO roomDto) {
        if (roomDto.getName() == null || roomDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        room.setName(roomDto.getName());
        room.setPrice(roomDto.getPrice());
        room.setDescription(roomDto.getDescription());
        room.setOccupancy(roomDto.getOccupancy());
        room.setBathFacility(roomDto.isBathFacility());
        room.setBath(roomDto.isBath());
        room.setAirCondition(roomDto.isAirCondition());
        room.setTv(roomDto.isTv());
        room.setCable(roomDto.isCable());
        room.setInternet(roomDto.isInternet());
        room.setRefrigerator(roomDto.isRefrigerator());
        room.setToiletries(roomDto.isToiletries());
        room.setSofa(roomDto.isSofa());
        room.setTableYn(roomDto.isTableYn());
        room.setHairdryer(roomDto.isHairdryer());

        roomRepository.save(room); // 변경된 내용 저장
    }

    @Transactional
    public void updateRoomCountBasedOnReservations(Long hotelId, LocalDate date) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 호텔이 존재하지 않습니다. ID: " + hotelId));

        List<Room> rooms = roomRepository.findByHotel(hotel);

        for (Room room : rooms) {
            // 해당 날짜의 예약 수를 계산
            long reservedCount = reservationRepository.countByRoomAndDate(room, date);

            // RoomCount 업데이트
            RoomCount roomCount = roomCountRepository.findByRoomAndDate(room, date)
                    .orElseGet(() -> {
                        RoomCount newRoomCount = new RoomCount();
                        newRoomCount.setRoom(room);
                        newRoomCount.setDate(date);
                        return newRoomCount;
                    });

            roomCount.setRoomCount((int) (room.getTotal() - reservedCount)); // 남은 객실 수 = 총 객실 수 - 예약 수
            roomCountRepository.save(roomCount);
        }
    }

}