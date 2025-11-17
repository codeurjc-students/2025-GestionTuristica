package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.repositories.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelBySlug(String slug) {
        return hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("This hotel doesn't exist")
        );
    }
}
