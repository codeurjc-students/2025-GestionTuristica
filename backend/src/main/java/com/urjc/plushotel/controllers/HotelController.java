package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.HotelFilters;
import com.urjc.plushotel.dtos.request.HotelRequest;
import com.urjc.plushotel.dtos.response.HotelDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelRoomCardService;
import com.urjc.plushotel.services.HotelService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class HotelController {

    private final HotelService hotelService;
    private final HotelRoomCardService hotelRoomCardService;

    public HotelController(HotelService hotelService, HotelRoomCardService hotelRoomCardService) {
        this.hotelService = hotelService;
        this.hotelRoomCardService = hotelRoomCardService;
    }

    @GetMapping(EndpointConstants.HotelsEndpoints.HOTELS_BASE_URL)
    public ResponseEntity<Page<HotelDTO>> getAllHotels(@RequestParam int page,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String country,
                                                       @RequestParam(required = false) String city,
                                                       @RequestParam(required = false) Double stars,
                                                       @RequestParam(required = false) Double rating
    ) {
        HotelFilters filters = new HotelFilters(name, country, city, stars, rating);

        return ResponseEntity.ok(hotelRoomCardService.getHotelsInfo(page, filters));
    }

    @GetMapping(EndpointConstants.HotelsEndpoints.HOTELS_SLUG_URL)
    public ResponseEntity<HotelDTO> getHotelBySlug(@PathVariable String slug) {

        HotelDTO hotel = hotelService.getHotelBySlug(slug);
        return ResponseEntity.ok(hotel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(EndpointConstants.HotelsEndpoints.HOTELS_BASE_URL)
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelRequest hotel) {
        Hotel savedHotel = hotelService.createHotel(hotel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{slug}")
                .buildAndExpand(savedHotel.getSlug())
                .toUri();
        return ResponseEntity.created(location).body(savedHotel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(EndpointConstants.HotelsEndpoints.HOTELS_SLUG_URL)
    public ResponseEntity<Hotel> updateHotel(@RequestBody HotelRequest hotel, @PathVariable String slug) {
        Hotel updatedHotel = hotelService.updateHotel(hotel, slug);
        return ResponseEntity.ok(updatedHotel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(EndpointConstants.HotelsEndpoints.HOTELS_SLUG_URL)
    public ResponseEntity<Hotel> removeHotel(@PathVariable String slug) {
        hotelService.removeHotel(slug);
        return ResponseEntity.noContent().build();
    }
}
