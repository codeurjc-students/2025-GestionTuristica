package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByReservationRoomId(Long roomId, Pageable pageable);

    Optional<Review> findByReservationReservationIdentifier(String reservationIdentifier);
}
