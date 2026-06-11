package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<HotelImage, Long> {

    List<HotelImage> findByRoom_IdOrderByPosition(Long roomId);

    List<HotelImage> findByPositionAndHotel_IdIn(int position, List<Long> hotelIds);

    List<HotelImage> findByHotel_SlugAndRoomIsNotNullAndPosition(String slug, int position);

    List<HotelImage> findByHotel_SlugAndRoomIsNullOrderByPosition(String slug);
}
