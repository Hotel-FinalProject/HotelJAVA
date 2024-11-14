package com.example.backend.service;

import java.util.stream.Collectors;
import com.example.backend.dto.RoomDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;
import com.example.backend.entity.RoomImage;
import com.example.backend.repository.HotelRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final HotelRepository hotelRepository;
    private final RoomCountRepository roomCountRepository;
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

        // 파싱
        List<Room> rooms = parseAndSaveRooms(items, hotel);
        roomRepository.saveAll(rooms);
    }


    private List<Room> parseAndSaveRooms(JSONArray items, Hotel hotel) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            Room room = new Room();
            room.setHotel(hotel);
            room.setName(item.optString("roomtitle", "Unknown"));
            room.setTotal(item.optInt("total",10));
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
}