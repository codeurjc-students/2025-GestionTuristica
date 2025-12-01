package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByIdAndHotel_Slug(Long id, String slug);
}
