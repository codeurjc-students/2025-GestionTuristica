package com.urjc.plushotel.repositories;

import com.urjc.plushotel.entities.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<HotelImage, Long> {
}
