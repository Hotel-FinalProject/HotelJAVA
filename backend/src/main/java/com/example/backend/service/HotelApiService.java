package com.example.backend.service;

import com.example.backend.entity.Hotel;
import com.example.backend.repository.HotelApiRepository;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HotelApiService {

    @Autowired
    private HotelApiRepository hotelRepository;

    private static final String LIST_API_URL = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1";
    private static final String DETAIL_API_URL = "http://apis.data.go.kr/B551011/KorService1/detailIntro1";

    @Value("${hotel.key}")
    String SERVICE_KEY;

    public void saveHotel() {
        int totalEntries = 175;
        int entriesPerPage = 12;
        int totalPages = (int) Math.ceil((double) totalEntries / entriesPerPage);
        for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
            String urlWithPage = String.format("%s?numOfRows=%d&pageNo=%d&MobileOS=ETC&MobileApp=AppTest&_type=json&ServiceKey=%s&listYN=Y&arrange=A&contentTypeId=32&areaCode=1&sigunguCode=&cat1=B02&cat2=B0201&cat3=B02010100",
                    LIST_API_URL, entriesPerPage, pageNo, SERVICE_KEY);

            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlWithPage);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();
                JsonObject response = jsonObject.getAsJsonObject("response");
                JsonObject body = response.getAsJsonObject("body");
                JsonObject items = body.getAsJsonObject("items");

                if (items != null && items.has("item")) {
                    JsonArray array = items.getAsJsonArray("item");

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject tmp = array.get(i).getAsJsonObject();
                        Long contentId = tmp.get("contentid").getAsLong();

                        // 호텔 기본 정보 저장
                        Hotel hotel = Hotel.builder()
                                .contentid(contentId)
                                .name(tmp.get("title").getAsString())
                                .address(tmp.get("addr1").getAsString())
                                .imageUrl(tmp.has("firstimage") ? tmp.get("firstimage").getAsString() : null)
                                .locationX(tmp.get("mapx").getAsString())
                                .locationY(tmp.get("mapy").getAsString())
                                .hotelTel(tmp.has("tel") ? tmp.get("tel").getAsString() : null)
                                .build();

                        // 상세 정보 API 호출
                        String detailApiUrl = String.format("%s?ServiceKey=%s&contentTypeId=32&contentId=%d&MobileOS=ETC&MobileApp=AppTest&_type=json",
                                DETAIL_API_URL, SERVICE_KEY, contentId);
                        HttpURLConnection detailConnection = (HttpURLConnection) new URL(detailApiUrl).openConnection();
                        detailConnection.setRequestMethod("GET");
                        detailConnection.setRequestProperty("Content-Type", "application/json");

                        BufferedReader detailBr = new BufferedReader(new InputStreamReader(detailConnection.getInputStream(), "UTF-8"));
                        StringBuilder detailResult = new StringBuilder();
                        while ((line = detailBr.readLine()) != null) {
                            detailResult.append(line);
                        }

                        // JSON 응답 파싱
                        JsonObject detailJsonObject = JsonParser.parseString(detailResult.toString()).getAsJsonObject();
                        JsonObject detailResponse = detailJsonObject.getAsJsonObject("response");
                        JsonObject detailBody = detailResponse.getAsJsonObject("body");
                        JsonObject detailItems = detailBody.getAsJsonObject("items");

                        if (detailItems != null && detailItems.has("item")) {
                            JsonObject detailItem = detailItems.getAsJsonArray("item").get(0).getAsJsonObject();
                            // 체크인, 체크아웃 시간 설정
                            hotel.setCheckIn(detailItem.get("checkintime").getAsString());
                            hotel.setCheckOut(detailItem.get("checkouttime").getAsString());
                        }

                        // 호텔 정보 저장
                        hotelRepository.save(hotel);
                        detailConnection.disconnect();
                    }
                }
            } catch (IOException e) {
                System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    private Date parseTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
