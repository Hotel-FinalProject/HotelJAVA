package com.example.backend.dto;

import com.example.backend.entity.Room;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
public class RoomCountDTO {

    private Long roomCountId;

    private LocalDate date;

    private int roomCount;

    private int roomId;
}
