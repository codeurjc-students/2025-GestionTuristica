package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PutMapping("/hotels/{slug}")
    public ResponseEntity<Hotel> updateHotel(@RequestBody Hotel hotel, @PathVariable String slug) {
        Hotel updatedHotel = hotelService.updateHotel(hotel, slug);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/hotels/{slug}")
    public ResponseEntity<Hotel> removeHotel(@PathVariable String slug) {
        hotelService.removeHotel(slug);
        return ResponseEntity.noContent().build();
    }
}
