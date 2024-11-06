package com.example.backend.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.backend.entity.Hotel;
import com.example.backend.repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://apis.data.go.kr/B551011/KorService1/searchStay1";
    private final String detailApiUrl = "http://apis.data.go.kr/B551011/KorService1/detailIntro1";
    private final String apiKey = ""; // API 키 입력
    
    public void fetchAndSaveHotels() {
        try {
            String url = apiUrl + "?serviceKey=" + apiKey + "&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=175&areaCode=1";
            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/xml; charset=UTF-8");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);

            JSONObject jsonObject = XML.toJSONObject(xmlResponse);

            if (jsonObject.has("response") 
                && jsonObject.getJSONObject("response").has("body")
                && jsonObject.getJSONObject("response").getJSONObject("body").has("items")) {
                
                JSONArray items = jsonObject.getJSONObject("response")
                                            .getJSONObject("body")
                                            .getJSONObject("items")
                                            .optJSONArray("item");

                if (items != null) {
                    List<Hotel> hotels = parseJsonToHotels(items);
                    for (Hotel hotel : hotels) {
                        setCheckInOutTimes(hotel);
                    }
                    hotelRepository.saveAll(hotels);
                } else {
                    System.out.println("item 배열이 없습니다.");
                }
            } else {
                System.out.println("response, body 또는 items 키가 JSON 응답에 없습니다.");
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // 체크인/체크아웃
    private void setCheckInOutTimes(Hotel hotel) {
        try {
            String detailUrl = detailApiUrl
                    + "?ServiceKey=" + apiKey
                    + "&contentTypeId=32"
                    + "&contentId=" + hotel.getContentId()
                    + "&MobileOS=ETC"
                    + "&MobileApp=AppTest";
            URI uri = new URI(detailUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/xml; charset=UTF-8");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);
            JSONObject jsonObject = XML.toJSONObject(xmlResponse);

            if (jsonObject.has("response") 
                && jsonObject.getJSONObject("response").has("body")
                && jsonObject.getJSONObject("response").getJSONObject("body").has("items")) {
                
                JSONObject item = jsonObject.getJSONObject("response")
                                            .getJSONObject("body")
                                            .getJSONObject("items")
                                            .optJSONObject("item");

                if (item != null) {
                    String checkInTime = item.optString("checkintime");
                    String checkOutTime = item.optString("checkouttime");

                    hotel.setCheckIn(checkInTime.isEmpty() ? null : checkInTime);
                    hotel.setCheckOut(checkOutTime.isEmpty() ? null : checkOutTime);
                } else {
                    System.out.println("item 객체가 없습니다.");
                }
            } else {
                System.out.println("response, body 또는 items 키가 JSON 응답에 없습니다.");
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // 파싱
    private List<Hotel> parseJsonToHotels(JSONArray items) {
        List<Hotel> hotels = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            Hotel hotel = new Hotel();

            hotel.setAddress(item.optString("addr1"));
            hotel.setLocation(item.optString("areacode"));
            hotel.setImageUrl(item.optString("firstimage"));
            hotel.setHotelnum(item.optString("tel"));
            hotel.setName(item.optString("title"));
            hotel.setContentId(item.optLong("contentid"));
            hotel.setMapX(item.optDouble("mapx"));
            hotel.setMapY(item.optDouble("mapy"));

            hotels.add(hotel);
        }
        return hotels;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    
    public Optional<Hotel> findByContentId(Long contentId) {
        return hotelRepository.findByContentId(contentId);
    }
    
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("호텔을 찾을 수 없습니다. ID: " + id));
    }

}
