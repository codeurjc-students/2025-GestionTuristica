package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.services.HotelRoomCardService;
import com.urjc.plushotel.services.RoomService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class RoomController {

    private final RoomService roomService;
    private final HotelRoomCardService hotelRoomCardService;

    public RoomController(RoomService roomService, HotelRoomCardService hotelRoomCardService) {
        this.roomService = roomService;
        this.hotelRoomCardService = hotelRoomCardService;
    }

    @GetMapping(EndpointConstants.RoomsEndpoints.ROOMS_ID_URL)
    public ResponseEntity<RoomAvgRatingDTO> getRoomById(@PathVariable Long roomId) {
        RoomAvgRatingDTO room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @GetMapping(EndpointConstants.RoomsEndpoints.ROOMS_HOTEL_SLUG_URL)
    public ResponseEntity<Page<RoomAvgRatingDTO>> getRoomsByHotelSlug(@PathVariable String slug,
                                                                      @RequestParam int page) {
        Page<RoomAvgRatingDTO> rooms = hotelRoomCardService.getHotelRoomsInfo(slug, page);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping(EndpointConstants.RoomsEndpoints.NON_PAGED_ROOMS_HOTEL_SLUG_URL)
    public ResponseEntity<List<RoomAvgRatingDTO>> getNonPaginatedRoomsByHotelSlug(@PathVariable String slug) {
        List<RoomAvgRatingDTO> rooms = roomService.getNonPaginatedRoomsByHotelSlug(slug);
        return ResponseEntity.ok(rooms);
    }
}
