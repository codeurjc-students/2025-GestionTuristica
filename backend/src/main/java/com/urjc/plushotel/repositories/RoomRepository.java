package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
