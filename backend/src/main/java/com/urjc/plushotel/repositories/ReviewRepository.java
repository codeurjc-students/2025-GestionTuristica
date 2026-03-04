package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReservationRoomId(Long roomId);

    Optional<Review> findByReservationReservationIdentifier(String reservationIdentifier);
}
