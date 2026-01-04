package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Reservation;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            select new com.urjc.plushotel.dtos.response.ReservedDatesDTO(
                r.startDate,
                r.endDate
            )
            from Reservation r
            where r.room.id = :roomId
            """)
    List<ReservedDatesDTO> findReservedDatesByRoomId(Long roomId);
}
