package com.urjc.plushotel.repositories;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("""
            SELECT new com.urjc.plushotel.dtos.response.RoomAvgRatingDTO(
                ro.id,
                ro.name,
                ro.description,
                ro.price,
                AVG(r.rating)
            )
            FROM Room ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            WHERE ro.hotel.id = :hotelId
            GROUP BY ro.id, ro.name, ro.description, ro.price
            """)
    List<RoomAvgRatingDTO> findRoomsWithAverageRating(@Param("hotelId") Long hotelId);

    @Query("""
            SELECT new com.urjc.plushotel.dtos.response.RoomAvgRatingDTO(
                ro.id,
                ro.name,
                ro.description,
                ro.price,
                AVG(r.rating)
            )
            FROM Room ro
            LEFT JOIN Reservation res ON res.room = ro
            LEFT JOIN Review r ON r.reservation = res
            WHERE ro.id = :roomId
            GROUP BY ro.id, ro.name, ro.description, ro.price
            """)
    Optional<RoomAvgRatingDTO> findRoomWithAverageRatingById(@Param("roomId") Long roomId);
}