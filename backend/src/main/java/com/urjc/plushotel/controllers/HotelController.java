package com.urjc.plushotel.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;

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

    @PostMapping("/hotels")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.createHotel(hotel);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{slug}")
            .buildAndExpand(savedHotel.getSlug())
            .toUri();
        return ResponseEntity.created(location).body(savedHotel);
    }
}
