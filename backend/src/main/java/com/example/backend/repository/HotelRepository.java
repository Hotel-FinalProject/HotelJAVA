package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
	Optional<Hotel> findByContentId(Long contentId);

	@Query("SELECT h FROM Hotel h WHERE REPLACE(h.name, ' ', '') LIKE %:query%")
	List<Hotel> searchByNameIgnoringSpaces(@Param("query") String query);
}

