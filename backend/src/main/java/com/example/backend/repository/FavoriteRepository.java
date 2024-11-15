package com.example.backend.repository;

import com.example.backend.entity.Favorites;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.User;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites,Long> {
    boolean existsByUserAndHotel(User user, Hotel hotelInfo);

    Optional<Favorites> findByUserAndHotel(User user, Hotel hotelInfo);

    List<Favorites> findByUser(User user);
}
