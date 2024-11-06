package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
	Optional<Hotel> findByContentId(Long contentId);
//	List<Hotel> findByContentId(Long contentId);
}

