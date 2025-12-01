package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("hotels/{slug}/rooms/{roomId}")
    public ResponseEntity<Room> getHotelByRoomIdAndHotelSlug(@PathVariable String slug, @PathVariable Long roomId) {
        Room room = roomService.getRoomByIdAndHotelSlug(roomId, slug);
        return ResponseEntity.ok(room);
    }
}
