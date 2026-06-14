package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.HotelDTO;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelRoomCardService {

    private final ImageService imageService;

    private final HotelService hotelService;

    private final RoomService roomService;

    public HotelRoomCardService(ImageService imageService, HotelService hotelService, RoomService roomService) {
        this.imageService = imageService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    public Page<HotelDTO> getHotelsInfo(int pageNumber) {

        Page<HotelDTO> hotels = hotelService.getAll(pageNumber);

        List<Long> hotelIds = hotels.getContent().stream().map(HotelDTO::getId).toList();

        List<HotelImageDTO> hotelsMainImages = imageService.getHotelsMainImages(hotelIds);

        Map<Long, String> imagesMap = hotelsMainImages.stream().collect(Collectors.toMap(
                HotelImageDTO::getHotelId,
                HotelImageDTO::getUrl));

        hotels.getContent().forEach(hotel -> hotel.setMainImageUrl(imagesMap.get(hotel.getId())));

        return hotels;
    }

    public Page<RoomAvgRatingDTO> getHotelRoomsInfo(String slug, int page) {

        Page<RoomAvgRatingDTO> hotelRooms = roomService.getRoomsByHotelSlug(slug, page);

        List<Long> roomIds = hotelRooms.getContent().stream().map(RoomAvgRatingDTO::getId).toList();

        List<HotelImageDTO> hotelRoomsMainImages = imageService.getHotelRoomsMainImages(roomIds);

        Map<Long, String> imagesMap = hotelRoomsMainImages.stream().collect(Collectors.toMap(
                HotelImageDTO::getRoomId,
                HotelImageDTO::getUrl));

        hotelRooms.getContent().forEach(room -> room.setMainImageUrl(imagesMap.get(room.getId())));

        return hotelRooms;
    }
}
