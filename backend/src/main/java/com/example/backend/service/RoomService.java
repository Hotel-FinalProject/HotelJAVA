package com.example.backend.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.entity.RoomImage;
import com.example.backend.repository.RoomImageRepository;
import com.example.backend.repository.RoomRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomImageRepository roomImageRepository;

    public void fetchAndSaveRoomData(String apiUrl, Hotel hotel) {
        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(apiUrl, String.class);

        // XML 응답을 JSONObject로 변환
        JSONObject jsonObject = XML.toJSONObject(xmlResponse);
        JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
        JSONArray items = body.getJSONObject("items").getJSONArray("item");

        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject roomData = items.getJSONObject(i);
            Room room = new Room();

            room.setName(roomData.getString("roomtitle"));
            room.setTotal(roomData.optLong("roomcount", 1));
            room.setOccupancy(roomData.optLong("roombasecount", 2));
            room.setDescription(roomData.optString("roomintro", "No description available"));
            room.setPrice(BigDecimal.valueOf(100000)); // 임의의 가격 설정
            room.setBathFacility("Y".equals(roomData.optString("roombathfacility")));
            room.setBath("Y".equals(roomData.optString("roombath")));
            room.setAirCondition("Y".equals(roomData.optString("roomaircondition")));
            room.setTv("Y".equals(roomData.optString("roomtv")));
            room.setCable("Y".equals(roomData.optString("roomcable")));
            room.setInternet("Y".equals(roomData.optString("roominternet")));
            room.setRefrigerator("Y".equals(roomData.optString("roomrefrigerator")));
            room.setToiletries("Y".equals(roomData.optString("roomtoiletries")));
            room.setSofa("Y".equals(roomData.optString("roomsofa")));
            room.setTable("Y".equals(roomData.optString("roomtable")));
            room.setHairdryer("Y".equals(roomData.optString("roomhairdryer")));

            room.setHotel(hotel);

            // RoomImage 생성
            List<RoomImage> images = new ArrayList<>();
            for (int j = 1; j <= 5; j++) {
                String imageUrlTag = "roomimg" + j;
                String imageAltTag = "roomimg" + j + "alt";
                if (roomData.has(imageUrlTag) && !roomData.getString(imageUrlTag).isEmpty()) {
                    RoomImage image = new RoomImage();
                    image.setImageUrl(roomData.getString(imageUrlTag));
                    image.setImageAlt(roomData.optString(imageAltTag, ""));
                    image.setRoom(room);
                    images.add(image);
                }
            }
            room.setImages(images);

            rooms.add(room);
        }

        // 저장
        for (Room room : rooms) {
            roomRepository.save(room);
            for (RoomImage image : room.getImages()) {
                roomImageRepository.save(image);
            }
        }
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

}


