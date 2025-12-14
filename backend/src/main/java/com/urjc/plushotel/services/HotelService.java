package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.Room;
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

    public Hotel createHotel(Hotel hotel) {
        if (hotel.getRooms() != null) {
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }
        }
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Hotel hotel, String slug) {
        Hotel savedHotel = hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("This hotel doesn't exist")
        );

        savedHotel.getRooms().clear();

        for (Room room : hotel.getRooms()) {
            savedHotel.addRoom(room);
        }

        savedHotel.setName(hotel.getName());
        savedHotel.setDescription(hotel.getDescription());
        savedHotel.setCountry(hotel.getCountry());
        savedHotel.setCity(hotel.getCity());
        savedHotel.setAddress(hotel.getAddress());
        savedHotel.setStars(hotel.getStars());
        savedHotel.setSlug(hotel.getSlug());

        return hotelRepository.save(savedHotel);
    }

    public void removeHotel(String slug) {
        Hotel hotelToRemove = hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("Hotel not found")
        );

        hotelRepository.delete(hotelToRemove);
    }
}
