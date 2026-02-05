package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.ReservationChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationChangeRequestRepository extends JpaRepository<ReservationChangeRequest, Long> {
}
