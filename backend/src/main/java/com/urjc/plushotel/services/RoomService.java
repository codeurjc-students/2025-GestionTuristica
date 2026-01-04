package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RuntimeException("There is no room with such id")
        );
    }
}
