package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.exceptions.RoomNotFoundException;
import com.urjc.plushotel.repositories.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomAvgRatingDTO getRoomById(Long id) {
        return roomRepository.findRoomWithAverageRatingById(id).orElseThrow(
                () -> new RoomNotFoundException("There is no room with such id")
        );
    }

    public Room getRoomEntityById(Long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException("There is no room with such id")
        );
    }

    public Page<RoomAvgRatingDTO> getRoomsByHotelSlug(String hotelSlug, int pageNumber) {
        return roomRepository.findRoomsByHotelSlugWithAverageRating(hotelSlug, Pageable.ofSize(5).withPage(pageNumber));
    }

    public List<RoomAvgRatingDTO> getNonPaginatedRoomsByHotelSlug(String hotelSlug) {
        return roomRepository.findNonPaginatedRoomsByHotelSlugWithAverageRating(hotelSlug);
    }

    public void deleteRoom(Long roomId) {
        Room room = getRoomEntityById(roomId);

        room.setDeleted(true);

        roomRepository.save(room);
    }
}
