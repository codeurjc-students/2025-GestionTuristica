package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.HotelImage;
import com.urjc.plushotel.services.ImageService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(EndpointConstants.ImageEndpoints.HOTEL_IMAGES_URL)
    public ResponseEntity<Void> uploadImage(@PathVariable String slug, @RequestParam("file") MultipartFile file,
                                            @RequestParam(name = "position") int postion) {

        HotelImage image = imageService.uploadImage(file, slug, postion);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/images/{id}")
                .buildAndExpand(image.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(EndpointConstants.ImageEndpoints.ROOM_IMAGES_URL)
    public ResponseEntity<Void> uploadRoomImage(@PathVariable Long roomId, @RequestParam("file") MultipartFile file,
                                                @RequestParam(name = "position") int position) {

        HotelImage image = imageService.uploadImage(file, roomId, position);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/images/{id}")
                .buildAndExpand(image.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
