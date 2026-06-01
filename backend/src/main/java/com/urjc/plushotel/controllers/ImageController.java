package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.HotelImageUpdateRequest;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.entities.HotelImage;
import com.urjc.plushotel.services.ImageService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping(EndpointConstants.ImageEndpoints.HOTEL_IMAGES_URL)
    public ResponseEntity<List<HotelImageDTO>> getHotelImages(@PathVariable String slug) {

        List<HotelImageDTO> hotelImages = imageService.getHotelImages(slug);

        return ResponseEntity.ok(hotelImages);
    }

    @GetMapping(EndpointConstants.ImageEndpoints.ROOM_IMAGES_URL)
    public ResponseEntity<List<HotelImageDTO>> getRoomImages(@PathVariable Long roomId) {

        List<HotelImageDTO> hotelImages = imageService.getImagesByRoomId(roomId);

        return ResponseEntity.ok(hotelImages);
    }

    @PutMapping(EndpointConstants.ImageEndpoints.IMAGE_URL)
    public ResponseEntity<HotelImageDTO> updateImage(@PathVariable Long imageId,
                                                     @RequestBody HotelImageUpdateRequest request) {

        HotelImageDTO image = imageService.updateImage(imageId, request);

        return ResponseEntity.ok(image);
    }
}
