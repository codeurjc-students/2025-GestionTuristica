package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findByIdAndHotel_Slug(Long id, String slug);
}
