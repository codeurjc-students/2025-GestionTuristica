package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.services.RoomService;
import com.urjc.plushotel.utils.EndpointConstants;
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

    @GetMapping(EndpointConstants.RoomsEndpoints.ROOMS_ID_URL)
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }
}
