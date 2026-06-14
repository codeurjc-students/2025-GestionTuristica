package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByHotel_Slug(String hotelSlug, Pageable pageable);

    List<Room> findByHotel_Slug(String hotelSlug);
}