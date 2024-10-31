package com.example.backend.service;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.entity.RoomImage;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.RoomRepository;
import com.example.backend.repository.RoomImageRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final HotelRepository hotelRepository;
    private final String API_URL = "http://apis.data.go.kr/B551011/KorService1/detailInfo1";
    private final String SERVICE_KEY = ""; // 실제 인증 키로 변경 필요

    // 모든 호텔에 대한 객실 데이터를 추가하는 메서드
    @Transactional
    public void fetchAndSaveAllRooms() throws URISyntaxException {
        List<Hotel> hotels = hotelRepository.findAll(); // 모든 호텔 가져오기
        for (Hotel hotel : hotels) {
            try {
                fetchAndSaveRooms(hotel.getContentId()); // 각 호텔의 contentId로 객실 정보 저장
            } catch (Exception e) {
                System.out.println("Error fetching rooms for contentId " + hotel.getContentId() + ": " + e.getMessage());
                // 로그만 남기고 다음 호텔로 진행
            }
        }
    }
    // 특정 호텔에 대한 객실 데이터를 API에서 가져와 저장하는 메서드
    @Transactional
    public void fetchAndSaveRooms(Long contentId) throws URISyntaxException {
        Hotel hotel = hotelRepository.findByContentId(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contentId"));

        String url = String.format("%s?ServiceKey=%s&contentTypeId=32&contentId=%s&MobileOS=ETC&MobileApp=AppTest",
                API_URL, SERVICE_KEY, contentId);
        URI uri = new URI(url);

        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(uri, String.class);

        // XML 응답을 JSON으로 변환
        JSONObject jsonResponse = XML.toJSONObject(xmlResponse);

        // 'items' 배열이 존재하는지 확인
        JSONArray items = jsonResponse.optJSONObject("response")
                                      .optJSONObject("body")
                                      .optJSONObject("items")
                                      .optJSONArray("item");

        if (items == null) {
//            throw new IllegalArgumentException("API 응답에서 'items' 배열을 찾을 수 없습니다.");
        	items = new JSONArray(); // 빈 배열로 초기화하여 오류 없이 진행
        }

        // API 데이터를 파싱하여 Room과 RoomImage 엔티티로 변환 후 저장
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
            room.setTotal(item.optLong("roomcount", 0L));
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

            // RoomImage 엔티티 설정 및 저장
            List<RoomImage> images = saveRoomImages(item, room);
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

    // 특정 호텔의 객실 조회
    public List<Room> getRoomsByContentId(Long contentId) {
        Hotel hotel = hotelRepository.findByContentId(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contentId"));
        return roomRepository.findByHotel(hotel);
    }
    // 모든 객실 조회
    public List<Room> getAllRooms() {
        return roomRepository.findAll(); // 모든 객실 조회
    }
}
