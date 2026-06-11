package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.RequestStatus;
import com.urjc.plushotel.entities.RequestType;
import com.urjc.plushotel.entities.ReservationChangeRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationChangeRequestRepository extends JpaRepository<ReservationChangeRequest, Long> {

    Page<ReservationChangeRequest> findByStatus(RequestStatus status, Pageable pageable);

    List<ReservationChangeRequest> findByType(RequestType type);

    Page<ReservationChangeRequest> findByStatusIn(List<RequestStatus> statuses, Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM ReservationChangeRequest rcr
            WHERE rcr.reservation.room.id = :roomId
            """)
    void deleteByRoomId(Long roomId);
}
