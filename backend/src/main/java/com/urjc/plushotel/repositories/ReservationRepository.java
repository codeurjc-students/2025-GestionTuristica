package com.urjc.plushotel.repositories;

import com.urjc.plushotel.dtos.response.HotelReservationsResponse;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationIdentifier(String reservationIdentifier);

    @Query("""
            select new com.urjc.plushotel.dtos.response.ReservedDatesDTO(
                r.startDate,
                r.endDate
            )
            from Reservation r
            where r.room.id = :roomId
            """)
    List<ReservedDatesDTO> findReservedDatesByRoomId(Long roomId);

    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    Page<Reservation> findByStatusNot(ReservationStatus status, Pageable pageable);

    Page<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status, Pageable pageable);

    Page<Reservation> findByUserIdAndStatusNot(Long userId, ReservationStatus status, Pageable pageable);

    @Query("""
            SELECT new com.urjc.plushotel.dtos.response.HotelReservationsResponse(
                h.name,
                COUNT(r)
            )
            FROM Reservation r
            JOIN r.room ro
            JOIN ro.hotel h
            WHERE h.deleted = false
            GROUP BY h.name
            ORDER BY COUNT(r) DESC
            """)
    List<HotelReservationsResponse> findMostReservedHotels();
}
