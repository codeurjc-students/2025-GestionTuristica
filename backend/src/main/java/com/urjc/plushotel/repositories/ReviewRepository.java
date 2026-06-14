package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByReservationRoomId(Long roomId, Pageable pageable);

    Optional<Review> findByReservationReservationIdentifier(String reservationIdentifier);

    @Query("""
            SELECT AVG(r.rating)
            FROM Hotel h
            LEFT JOIN h.rooms ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            WHERE h.slug = :slug
            AND h.deleted = false
            GROUP BY h.id
            """)
    Double findHotelAverageRating(String slug);

    @Query("""
            SELECT AVG(r.rating)
            FROM Room ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            WHERE ro.id = :roomId
            AND ro.deleted = false
            GROUP BY ro.id
            """)
    Double findRoomAverageRating(Long roomId);
}
