package com.example.backend.repository;

import com.example.backend.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelApiRepository extends JpaRepository<Hotel, Long> {

    boolean existsByContentid(Long contentid);
}
