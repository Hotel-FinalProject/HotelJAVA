package com.example.backend.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
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
    private final String apiKey = ""; // 실제 API 키로 변경 필요

    public void fetchAndSaveHotels() {
        try {
            // 기본 정보 URI 생성
            String url = apiUrl + "?serviceKey=" + apiKey + "&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=175&areaCode=1";
            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/xml; charset=UTF-8");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출 및 UTF-8로 변환
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);

            // XML -> JSON 변환
            JSONObject jsonObject = XML.toJSONObject(xmlResponse);
            JSONArray items = jsonObject.getJSONObject("response")
                                        .getJSONObject("body")
                                        .getJSONObject("items")
                                        .getJSONArray("item");

            // JSON 데이터를 Hotel 객체로 변환하여 DB에 저장
            List<Hotel> hotels = parseJsonToHotels(items);
            
            // 상세 정보를 조회하여 체크인/체크아웃 시간 추가
            for (Hotel hotel : hotels) {
                setCheckInOutTimes(hotel);
            }
            
            // 모든 호텔 정보를 DB에 저장
            hotelRepository.saveAll(hotels);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // 상세 정보 API를 호출하여 체크인/체크아웃 시간 설정
    private void setCheckInOutTimes(Hotel hotel) {
        try {
            // 상세 정보 URI 생성
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

            // API 호출 및 XML 응답을 JSON으로 변환
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);
            JSONObject jsonObject = XML.toJSONObject(xmlResponse);

            // JSON에서 체크인 및 체크아웃 시간 추출
            JSONObject item = jsonObject.getJSONObject("response")
                                        .getJSONObject("body")
                                        .getJSONObject("items")
                                        .getJSONObject("item");

            String checkInTime = item.optString("checkintime");
            String checkOutTime = item.optString("checkouttime");

            // Hotel 객체에 체크인/체크아웃 시간 설정
            hotel.setCheckIn(checkInTime.isEmpty() ? null : checkInTime);
            hotel.setCheckOut(checkOutTime.isEmpty() ? null : checkOutTime);


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // JSON 데이터를 파싱하여 Hotel 객체로 변환
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

    // DB에서 모든 호텔 리스트 조회
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    
    // Hotel을 contentId로 조회하는 메서드 추가
    public Optional<Hotel> findByContentId(Long contentId) {
        return hotelRepository.findByContentId(contentId);
    }
}
