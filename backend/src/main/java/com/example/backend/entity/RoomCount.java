package com.example.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomCount {

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long roomCountId;
	 
	 private LocalDate date;

	@ColumnDefault("10")
	 private int roomCount;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "room_id", nullable = false)
	 private Room room;

}
