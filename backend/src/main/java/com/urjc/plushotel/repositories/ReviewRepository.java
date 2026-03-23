package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReservationRoomId(Long roomId);

    Optional<Review> findByReservationReservationIdentifier(String reservationIdentifier);
}
