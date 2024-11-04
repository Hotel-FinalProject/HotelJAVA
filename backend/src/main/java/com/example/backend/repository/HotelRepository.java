package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
	Optional<Hotel> findByContentId(Long contentId);
	
	// 페이징된 호텔 목록 조회
//    Page<Hotel> findAll(Pageable pageable);
}
