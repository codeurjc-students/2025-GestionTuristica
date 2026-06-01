package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.HotelImage;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final MinioService minioService;
    private final HotelService hotelService;
    private final RoomService roomService;

    public ImageService(ImageRepository imageRepository, MinioService minioService, HotelService hotelService,
                        RoomService roomService) {
        this.imageRepository = imageRepository;
        this.minioService = minioService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    public HotelImage uploadImage(MultipartFile file, String slug, int position) {

        Hotel hotel = hotelService.findBySlug(slug);

        String fileName = minioService.uploadImage(file);

        HotelImage image = new HotelImage(fileName, hotel, position);

        image = imageRepository.save(image);

        return image;
    }

    public HotelImage uploadImage(MultipartFile file, Long roomId, int position) {

        Room room = roomService.getRoomEntityById(roomId);

        String fileName = minioService.uploadImage(file);

        HotelImage image = new HotelImage(fileName, room.getHotel(), room, position);

        image = imageRepository.save(image);

        return image;
    }

    public List<HotelImageDTO> getImagesByHotelSlug(String slug) {

        List<HotelImage> images = imageRepository.findByHotel_SlugOrderByPosition(slug);

        return images.stream().map(
                img -> new HotelImageDTO(minioService.getImageUrl(img.getFileName()), img.getHotel().getId())
        ).toList();
    }

    public List<HotelImageDTO> getImagesByRoomId(Long roomId) {

        List<HotelImage> images = imageRepository.findByRoom_IdOrderByPosition(roomId);

        return images.stream().map(
                img -> new HotelImageDTO(img.getId(), minioService.getImageUrl(img.getFileName()),
                        img.getHotel().getId(), img.getRoom().getId(), img.getPosition())
        ).toList();
    }

    public List<HotelImageDTO> getHotelsMainImages() {

        List<HotelImage> images = imageRepository.findByRoomIsNullAndPosition(0);

        return images.stream().map(
                img -> new HotelImageDTO(img.getId(), minioService.getImageUrl(img.getFileName()),
                        img.getHotel().getId(), img.getPosition())
        ).toList();
    }

    public List<HotelImageDTO> getHotelRoomsMainImages(String slug) {

        List<HotelImage> images = imageRepository.findByHotel_SlugAndRoomIsNotNullAndPosition(slug, 0);

        return images.stream().map(
                img -> new HotelImageDTO(minioService.getImageUrl(img.getFileName()), img.getHotel().getId(),
                        img.getRoom().getId())
                img -> new HotelImageDTO(img.getId(), minioService.getImageUrl(img.getFileName()),
                        img.getHotel().getId(), img.getRoom().getId(), img.getPosition())
        ).toList();
    }
}
