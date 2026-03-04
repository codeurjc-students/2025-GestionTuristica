package com.urjc.plushotel.repositories;

import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
import com.urjc.plushotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findBySlug(String slug);

    @Query("""
            SELECT new com.urjc.plushotel.dtos.response.HotelAvgRatingDTO(
                h.id,
                h.name,
                h.description,
                h.country,
                h.city,
                h.address,
                h.stars,
                h.slug,
                AVG(r.rating)
            )
            FROM Hotel h
            LEFT JOIN h.rooms ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            WHERE h.slug = :slug
            GROUP BY h.id
            """)
    Optional<HotelAvgRatingDTO> findHotelsWithAverageRatingBySlug(String slug);

    @Query("""
            SELECT new com.urjc.plushotel.dtos.response.HotelAvgRatingDTO(
                h.id,
                h.name,
                h.description,
                h.country,
                h.city,
                h.address,
                h.stars,
                h.slug,
                AVG(r.rating)
            )
            FROM Hotel h
            LEFT JOIN h.rooms ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            GROUP BY h.id
            """)
    List<HotelAvgRatingDTO> findHotelsWithAverageRating();
}