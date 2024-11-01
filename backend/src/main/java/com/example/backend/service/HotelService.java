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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.key}")
    private  String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://apis.data.go.kr/B551011/KorService1/searchStay1";
    private final String detailApiUrl = "http://apis.data.go.kr/B551011/KorService1/detailIntro1";

    public void fetchAndSaveHotels() {
        try {
            String url = apiUrl + "?serviceKey=" + apiKey + "&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=175&areaCode=1";
            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/xml; charset=UTF-8");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            String xmlResponse = new String(response.getBody(), StandardCharsets.UTF_8);

            // XML -> JSON 변환
            JSONObject jsonObject = XML.toJSONObject(xmlResponse);

            // response, body, items 키가 존재하는지 확인 후 접근
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

    // 상세 정보 API를 호출하여 체크인/체크아웃 시간 설정
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

            // response, body, items 키가 존재하는지 확인 후 접근
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