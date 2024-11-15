package com.example.backend.service;

import com.example.backend.dto.FavoriteDTO;
import com.example.backend.dto.UserReservationDTO;
import com.example.backend.entity.Favorites;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import com.example.backend.repository.FavoriteRepository;
import com.example.backend.repository.HotelRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public ResponseEntity<?> wish(Long userId, Long hotelId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));

            Hotel hotelInfo = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new IllegalArgumentException("호텔 ID를 찾을 수 없습니다."));

            Favorites favorites = favoriteRepository.findByUserAndHotel(user, hotelInfo)
                    .orElse(null);

            if (favorites != null) {
                if (favorites.getStatus()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 찜된 호텔입니다.");
                } else {
                    favorites.setStatus(true);
                    favoriteRepository.save(favorites);
                    return ResponseEntity.status(HttpStatus.CREATED).body("호텔을 다시 찜하였습니다.");
                }
            } else {
                // 처음 찜하는 경우 새로 저장
                favorites = Favorites.builder()
                        .user(user)
                        .hotel(hotelInfo)
                        .status(true)
                        .build();
                favoriteRepository.save(favorites);
                return ResponseEntity.status(HttpStatus.CREATED).body("호텔을 찜하였습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜하기 처리 중 오류가 발생하였습니다.");
        }
    }

    public ResponseEntity<?> wishCancel(Long userId, Long hotelId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));

            Hotel hotelInfo = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new IllegalArgumentException("호텔 ID를 찾을 수 없습니다."));

            Favorites favorites = favoriteRepository.findByUserAndHotel(user, hotelInfo)
                    .orElseThrow(() -> new RuntimeException("찜된 항목을 찾을 수 없습니다."));

            if (favorites.getStatus()) {
                favorites.setStatus(false);
                favoriteRepository.save(favorites);
                return ResponseEntity.status(HttpStatus.OK).body("호텔 찜을 취소하였습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 취소된 항목입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜 취소 처리 중 오류가 발생하였습니다.");
        }
    }

    public ResponseEntity<?> getFavoriteStatus(Long userId, Long hotelId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));
            Hotel hotelInfo = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new IllegalArgumentException("호텔 ID를 찾을 수 없습니다."));

            Favorites favorites = favoriteRepository.findByUserAndHotel(user, hotelInfo)
                    .orElse(null);

            boolean isFavorited = (favorites != null) && favorites.getStatus();
            return ResponseEntity.ok().body(isFavorited);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜 상태 확인 중 오류가 발생하였습니다.");
        }
    }

    public List<FavoriteDTO> getFavoritesInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        List<Favorites> favorites = favoriteRepository.findByUser(user);
        List<FavoriteDTO> favoriteDTOS = new ArrayList<>();

        for(Favorites favorite : favorites){
            FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                    .hotelName(favorite.getHotel().getName())
                    .hotelImage(favorite.getHotel().getImageUrl())
                    .status(favorite.getStatus())
                    .build();
            favoriteDTOS.add(favoriteDTO);
        }
        return favoriteDTOS;
    }

}
