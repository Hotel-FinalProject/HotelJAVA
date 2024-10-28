//2CO9iXaU94uL2TpF9yjZ0FY25Q%2BI4dNYmea7vdL%2FFvX9qPCpHnHTetc7Vh3CU4AGRJ3nyUjIGE4REwrxVaj17A%3D%3D
package com.example.backend.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    private final String apiKey = "2CO9iXaU94uL2TpF9yjZ0FY25Q%2BI4dNYmea7vdL%2FFvX9qPCpHnHTetc7Vh3CU4AGRJ3nyUjIGE4REwrxVaj17A%3D%3D"; // 실제 API 키로 변경 필요

    public void fetchAndSaveHotels() {
        try {
            // URI 생성
            String url = apiUrl + "?serviceKey=" + apiKey + "&MobileApp=AppTest&MobileOS=ETC&pageNo=1&numOfRows=10&areaCode=1&sigunguCode=18";
            URI uri = new URI(url);

            // 요청 헤더 설정
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
            hotelRepository.saveAll(hotels);

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

            // Hotel 객체 로그 출력
            System.out.println("Parsed Hotel: " + hotel);

            hotels.add(hotel);
        }
        return hotels;
    }

    // DB에서 모든 호텔 리스트 조회
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}

