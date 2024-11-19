package com.example.backend.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.backend.dto.HotelReviewDTO;
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

import com.example.backend.dto.HotelDTO;
import com.example.backend.dto.HotelRoomDTO;
import com.example.backend.dto.RoomDetailDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Room;
import com.example.backend.entity.RoomCount;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.RoomCountRepository;
import com.example.backend.repository.RoomRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomCountRepository roomCountRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://apis.data.go.kr/B551011/KorService1/searchStay1";
    private final String detailApiUrl = "http://apis.data.go.kr/B551011/KorService1/detailIntro1";

    @Value("${api.key}")
    private  String apiKey;

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
    
//    public List<HotelDTO> searchHotelsByName(String query) {
//        String queryWithoutSpaces = query.replaceAll("\\s+", ""); // 공백 제거
//        return hotelRepository.searchByNameIgnoringSpaces(queryWithoutSpaces).stream()
//            .map(hotel -> new HotelDTO(
//                    hotel.getHotelId(), 
//                    hotel.getName(), 
//                    hotel.getAddress(), 
//                    hotel.getImageUrl(), 
//                    hotel.getRating(), 
//                    hotel.getMapX(),  // mapX 추가
//                    hotel.getMapY()   // mapY 추가
//                ))
//            .collect(Collectors.toList());
//    }
    
    public List<HotelDTO> searchHotelsByNameOrAddress(String query) {
        String queryWithoutSpaces = query.replaceAll("\\s+", ""); // 공백 제거
        return hotelRepository.searchByNameAndAddressIgnoringSpaces(queryWithoutSpaces).stream()
            .map(hotel -> new HotelDTO(
                hotel.getHotelId(), 
                hotel.getName(), 
                hotel.getAddress(), 
                hotel.getImageUrl(),
                calculateAverageRating(hotel),
                hotel.getMapX(),
                hotel.getMapY(),
                null
            ))
            .collect(Collectors.toList());
    }


    public List<HotelDTO> getRandomHotels(int count) {
        List<Hotel> allHotels = hotelRepository.findAll();
        Collections.shuffle(allHotels);
        return allHotels.subList(0, Math.min(count, allHotels.size())).stream()
            .map(hotel -> new HotelDTO(
                    hotel.getHotelId(), 
                    hotel.getName(), 
                    hotel.getAddress(), 
                    hotel.getImageUrl(),
                    calculateAverageRating(hotel),
                    hotel.getMapX(),  // mapX 추가
                    hotel.getMapY(),   // mapY 추가
                    null
                ))
            .collect(Collectors.toList());
    }
    
    public List<HotelDTO> searchHotelsByLocation(String location) {
        if (location == null) {
            // 검색어가 없으면 전체 호텔 목록 조회
            return hotelRepository.findAll().stream()
                .map(hotel -> new HotelDTO(
                    hotel.getHotelId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getImageUrl(),
                    calculateAverageRating(hotel),
                    hotel.getMapX(),
                    hotel.getMapY(),
                    null
                ))
                .collect(Collectors.toList());
        } else {
            // 특정 구(location)에 해당하는 호텔 목록 조회
            return hotelRepository.findByLocation(location).stream()
                .map(hotel -> new HotelDTO(
                    hotel.getHotelId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getImageUrl(),
                    calculateAverageRating(hotel),
                    hotel.getMapX(),
                    hotel.getMapY(),
                    null
                ))
                .collect(Collectors.toList());
        }
    }

    
    // 특정 호텔과 관련된 객실 상세 정보 조회
    public HotelRoomDTO getHotelDetailById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("호텔을 찾을 수 없습니다. ID: " + id));

        // RoomDetailDTO 리스트 생성
        List<RoomDetailDTO> roomDetails = hotel.getRooms().stream()
                .map(room -> new RoomDetailDTO(
                        room.getRoomId(),
                        room.getName(), // roomType
                        room.getOccupancy(),
                        room.getPrice(),
                        room.getImages().isEmpty() ? null : room.getImages().get(0).getImageId(),
                        room.getImages().isEmpty() ? null : room.getImages().get(0).getImageUrl(),
                        null, // roomCountId
                        getAvailableRooms(room, LocalDate.now()) // 특정 날짜의 남은 객실 수 조회
                ))
                .collect(Collectors.toList());

        return new HotelRoomDTO(
                hotel.getHotelId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getImageUrl(),
                calculateAverageRating(hotel),
                hotel.getMapX(),
                hotel.getMapY(),
                hotel.getHotelnum(),
                hotel.getCheckIn(),
                hotel.getCheckOut(),
                roomDetails // 간소화된 객실 리스트 추가
        );
    }
    
    /**
     * 특정 날짜에 특정 객실의 남은 객실 수를 조회하는 메서드
     * @param room - 남은 객실 수를 조회할 Room 엔티티
     * @param date - 조회할 날짜
     * @return 남은 객실 수 (RoomCount 엔티티가 없는 경우 기본값 10 반환)
     */
    private int getAvailableRooms(Room room, LocalDate date) {
        RoomCount roomCount = roomCountRepository.findByRoomAndDate(room, date)
                .orElse(null);
        return (roomCount != null) ? roomCount.getRoomCount() : 10; // RoomCount가 없으면 기본 10개 반환
    }
    
    public List<HotelDTO> searchHotelsByDateAndGuest(LocalDate checkInDate, LocalDate checkOutDate, int guests, String query) {
        // 호텔 검색 결과를 가져옵니다.
        List<Hotel> hotels = query == null || query.isEmpty()
            ? hotelRepository.findAll() // 검색어가 없으면 모든 호텔
            : hotelRepository.searchByNameAndAddressIgnoringSpaces(query); // 검색어로 필터링된 호텔
        
        // 호텔 리스트 중에서 날짜와 인원에 맞는 호텔만 필터링
        return hotels.stream()
            .filter(hotel -> hotelHasAvailableRooms(hotel, checkInDate, checkOutDate, guests))
            .map(hotel -> new HotelDTO(
                    hotel.getHotelId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getImageUrl(),
                    calculateAverageRating(hotel),
                    hotel.getMapX(),
                    hotel.getMapY(),
                    null
            ))
            .collect(Collectors.toList());
    }
    
    private boolean hotelHasAvailableRooms(Hotel hotel, LocalDate checkInDate, LocalDate checkOutDate, int guests) {
        List<Room> rooms = roomRepository.findByHotel(hotel);
        
        // 호텔의 모든 객실을 순회하며 조건에 맞는 객실이 있는지 확인
        return rooms.stream().anyMatch(room -> 
            room.getOccupancy() >= guests && isRoomAvailable(room, checkInDate, checkOutDate)
        );
    }

    private boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        // 특정 날짜 범위에서 객실 예약 정보를 확인하여 가용 여부를 판별
        List<RoomCount> roomCounts = roomCountRepository.findByRoomAndDateBetween(room, checkInDate, checkOutDate);
        return roomCounts.stream().allMatch(roomCount -> roomCount.getRoomCount() > 0);
    }

    public List<HotelReviewDTO> getTop10HotelsByReviewCount() {
        List<HotelReviewDTO> hotelsWithReviews = hotelRepository.findAll().stream()
                .filter(hotel -> hotel.getReviews() != null && !hotel.getReviews().isEmpty())
                .sorted((h1, h2) -> Integer.compare(h2.getReviews().size(), h1.getReviews().size()))
                .limit(10)
                .map(hotel -> new HotelReviewDTO(
                        hotel.getHotelId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getImageUrl(),
                        calculateAverageRating(hotel),  
                        hotel.getMapX(),
                        hotel.getMapY(),
                        null,  // roomPrice는 null로 설정
                        hotel.getReviews().size() // 리뷰 개수 추가
                ))
                .collect(Collectors.toList());

        // 리뷰가 있는 호텔이 10개 미만인 경우, 리뷰가 없는 호텔로 추가 채움
        if (hotelsWithReviews.size() < 10) {
            List<HotelReviewDTO> hotelsWithoutReviews = hotelRepository.findAll().stream()
                    .filter(hotel -> hotel.getReviews() == null || hotel.getReviews().isEmpty())
                    .limit(10 - hotelsWithReviews.size())
                    .map(hotel -> new HotelReviewDTO(
                            hotel.getHotelId(),
                            hotel.getName(),
                            hotel.getAddress(),
                            hotel.getImageUrl(),
                            null,  // 리뷰가 없으므로 평점은 null
                            hotel.getMapX(),
                            hotel.getMapY(),
                            null,  // roomPrice는 null로 설정
                            0 // 리뷰 개수는 0
                    ))
                    .collect(Collectors.toList());
            hotelsWithReviews.addAll(hotelsWithoutReviews);
        }

        return hotelsWithReviews;
    }

    public List<HotelReviewDTO> getTop10HotelsByRating() {
        List<HotelReviewDTO> hotelsWithReviews = hotelRepository.findAll().stream()
                .filter(hotel -> hotel.getReviews() != null && !hotel.getReviews().isEmpty())
                .sorted((h1, h2) -> Double.compare(calculateAverageRating(h2), calculateAverageRating(h1)))
                .limit(10)
                .map(hotel -> new HotelReviewDTO(
                        hotel.getHotelId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getImageUrl(),
                        calculateAverageRating(hotel),  
                        hotel.getMapX(),
                        hotel.getMapY(),
                        null,  // roomPrice는 null로 설정
                        hotel.getReviews().size() // 리뷰 개수 추가
                ))
                .collect(Collectors.toList());

        // 평점이 있는 호텔이 10개 미만인 경우, 평점이 없는 호텔로 추가 채움
        if (hotelsWithReviews.size() < 10) {
            List<HotelReviewDTO> hotelsWithoutReviews = hotelRepository.findAll().stream()
                    .filter(hotel -> hotel.getReviews() == null || hotel.getReviews().isEmpty())
                    .limit(10 - hotelsWithReviews.size())
                    .map(hotel -> new HotelReviewDTO(
                            hotel.getHotelId(),
                            hotel.getName(),
                            hotel.getAddress(),
                            hotel.getImageUrl(),
                            null,  // 리뷰가 없으므로 평점은 null
                            hotel.getMapX(),
                            hotel.getMapY(),
                            null,  // roomPrice는 null로 설정
                            0 // 리뷰 개수는 0
                    ))
                    .collect(Collectors.toList());
            hotelsWithReviews.addAll(hotelsWithoutReviews);
        }

        return hotelsWithReviews;
    }

    public Double calculateAverageRating(Hotel hotel) {
        if (hotel.getReviews() == null || hotel.getReviews().isEmpty()) {
            return 0.0;
        }
        return hotel.getReviews().stream()
                .mapToDouble(review -> review.getRating())
                .average()
                .orElse(0.0);
    }
}