package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.RequestStatus;
import com.urjc.plushotel.entities.RequestType;
import com.urjc.plushotel.entities.ReservationChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationChangeRequestRepository extends JpaRepository<ReservationChangeRequest, Long> {

    List<ReservationChangeRequest> findByStatus(RequestStatus status);

    List<ReservationChangeRequest> findByType(RequestType type);

    List<ReservationChangeRequest> findByStatusIn(List<RequestStatus> statuses);
}
