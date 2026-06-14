package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.RoomDTO;
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

    public RoomDTO getRoomById(Long id) {
        return convertToDTO(roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException("There is no room with such id")
        ));
    }

    public Room getRoomEntityById(Long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException("There is no room with such id")
        );
    }

    public Page<RoomDTO> getRoomsByHotelSlug(String hotelSlug, int pageNumber) {
        return roomRepository.findByHotel_Slug(hotelSlug, Pageable.ofSize(5).withPage(pageNumber)).map(this::convertToDTO);
    }

    public List<RoomDTO> getNonPaginatedRoomsByHotelSlug(String hotelSlug) {
        return roomRepository.findByHotel_Slug(hotelSlug).stream().map(this::convertToDTO).toList();
    }

    public void deleteRoom(Long roomId) {
        Room room = getRoomEntityById(roomId);

        room.setDeleted(true);

        roomRepository.save(room);
    }

    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.getPrice(),
                room.getRating()
        );
    }
}
