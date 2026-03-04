package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReservationRoomId(Long roomId);

    @Query("""
            SELECT AVG(r.rating)
            FROM Review r
            WHERE r.reservation.room.hotel.id = :hotelId
            """)
    Double findAverageRatingByHotelId(Long hotelId);

    @Query("""
            SELECT AVG(r.rating)
            FROM Review r
            WHERE r.reservation.room.id = :roomId
            """)
    Double findAverageRatingByRoomId(Long roomId);

    Optional<Review> findByReservationReservationIdentifier(String reservationIdentifier);
}
