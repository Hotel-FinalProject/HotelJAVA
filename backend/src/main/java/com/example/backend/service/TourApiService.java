package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TourApiService {

    private final String apiKey = "2CO9iXaU94uL2TpF9yjZ0FY25Q%252BI4dNYmea7vdL%252FFvX9qPCpHnHTetc7Vh3CU4AGRJ3nyUjIGE4REwrxVaj17A%253D%253D";  // 실제 API 키
    private final String baseUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay";

    public String getHotelData(String cityName) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", apiKey)  // API 키
                .queryParam("areaCode", cityName)  // 서울, 부산 등 지역 코드를 사용
                .queryParam("contenttypeid", "B02")  // 숙박 유형 코드
                .queryParam("cat3", "B02010100")  // 관광호텔 분류 코드
                .queryParam("numOfRows", 10)  // 한 번에 가져올 데이터 수
                .queryParam("pageNo", 1)  // 페이지 번호
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .toUriString();

        return restTemplate.getForObject(uri, String.class);  // XML이나 JSON 응답을 String으로 받음
    }
}



//@Service
//public class TourApiService {
//
//    private final String apiKey = "2CO9iXaU94uL2TpF9yjZ0FY25Q%2BI4dNYmea7vdL%2FFvX9qPCpHnHTetc7Vh3CU4AGRJ3nyUjIGE4REwrxVaj17A%3D%3D";
//    private final String baseUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay";
//
//    public String getHotelData(String cityName) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("serviceKey", apiKey)
//                .queryParam("areaCode", cityName)  // 예: 도시 이름을 기반으로 호텔 조회
//                .queryParam("numOfRows", 10)       // 한 번에 가져올 데이터 수
//                .queryParam("pageNo", 1)           // 페이지 번호
//                .queryParam("MobileOS", "ETC")
//                .queryParam("MobileApp", "AppTest")
//                .toUriString();
//
//        return restTemplate.getForObject(uri, String.class);  // XML이나 JSON 응답을 String으로 받음
//    }
//}

