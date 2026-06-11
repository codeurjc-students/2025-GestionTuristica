package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.HotelRequest;
import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.HotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final ReservationChangeRequestService reservationChangeRequestService;

    public HotelService(HotelRepository hotelRepository, RoomService roomService,
                        ReservationService reservationService,
                        ReservationChangeRequestService reservationChangeRequestService) {
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.reservationChangeRequestService = reservationChangeRequestService;
    }

    public Page<HotelAvgRatingDTO> getAll(int pageNumber) {
        return hotelRepository.findHotelsWithAverageRating(Pageable.ofSize(5).withPage(pageNumber));
    }

    public HotelAvgRatingDTO getHotelBySlug(String slug) {
        return hotelRepository.findHotelsWithAverageRatingBySlug(slug).orElseThrow(
                () -> new RuntimeException("This hotel doesn't exist")
        );
    }

    public Hotel findBySlug(String slug) {
        return hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("This hotel doesn't exist")
        );
    }

    public Hotel createHotel(HotelRequest hotelRequest) {
        Hotel hotel = requestToHotel(hotelRequest);
        if (hotel.getRooms() != null) {
            for (Room room : hotelRequest.getRooms()) {
                room.setHotel(hotel);
            }
        }
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(HotelRequest hotel, String slug) {
        Hotel savedHotel = hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("This hotel doesn't exist")
        );

        savedHotel.setName(hotel.getName());
        savedHotel.setDescription(hotel.getDescription());
        savedHotel.setCountry(hotel.getCountry());
        savedHotel.setCity(hotel.getCity());
        savedHotel.setAddress(hotel.getAddress());
        savedHotel.setStars(hotel.getStars());
        savedHotel.setSlug(hotel.getSlug());

        Set<Long> updatedRoomIds = new HashSet<>();

        for (Room roomRequest : hotel.getRooms()) {
            if (roomRequest.getId() == null) {
                savedHotel.addRoom(roomRequest);
            } else {
                Room existingRoom = roomService.getRoomEntityById(roomRequest.getId());

                existingRoom.setName(roomRequest.getName());
                existingRoom.setDescription(roomRequest.getDescription());
                existingRoom.setPrice(roomRequest.getPrice());

                updatedRoomIds.add(existingRoom.getId());
            }
        }

        for (Room room : savedHotel.getRooms()) {
            if (room.getId() != null && !updatedRoomIds.contains(room.getId())) {
                deleteRoom(room);
            }
        }

        return hotelRepository.save(savedHotel);
    }

    public void removeHotel(String slug) {
        Hotel hotelToRemove = hotelRepository.findBySlug(slug).orElseThrow(
                () -> new RuntimeException("Hotel not found")
        );

        hotelToRemove.setDeleted(true);
        for (Room room : hotelToRemove.getRooms()) {
            deleteRoom(room);
        }
        hotelRepository.save(hotelToRemove);
    }

    private void deleteRoom(Room room) {
        roomService.deleteRoom(room.getId());
        for (Reservation reservation : room.getReservations()) {
            reservationService.cancelReservation(reservation.getReservationIdentifier());
        }

        reservationChangeRequestService.deleteChangeRequestsFromRoom(room.getId());
    }

    private Hotel requestToHotel(HotelRequest request) {
        return Hotel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .country(request.getCountry())
                .city(request.getCity())
                .address(request.getAddress())
                .stars(request.getStars())
                .slug(request.getSlug())
                .rooms(request.getRooms())
                .build();
    }
}
