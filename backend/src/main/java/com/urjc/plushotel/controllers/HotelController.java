package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels() {

        return ResponseEntity.ok(hotelService.getAll());
    }

    @GetMapping("/hotels/{slug}")
    public ResponseEntity<Hotel> getHotelBySlug(@PathVariable String slug) {

        Hotel hotel = hotelService.getHotelBySlug(slug);
        return ResponseEntity.ok(hotel);
    }
}
