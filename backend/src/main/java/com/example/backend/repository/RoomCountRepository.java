package com.example.backend.repository;

import com.example.backend.entity.RoomCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCountRepository  extends JpaRepository<RoomCount,Long> {
}
