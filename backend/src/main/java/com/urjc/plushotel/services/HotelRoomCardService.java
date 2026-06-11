package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
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

    public Page<HotelAvgRatingDTO> getHotelsInfo(int pageNumber) {

        Page<HotelAvgRatingDTO> hotels = hotelService.getAll(pageNumber);

        List<Long> hotelIds = hotels.getContent().stream().map(HotelAvgRatingDTO::getId).toList();

        List<HotelImageDTO> hotelsMainImages = imageService.getHotelsMainImages(hotelIds);

        Map<Long, String> imagesMap = hotelsMainImages.stream().collect(Collectors.toMap(
                HotelImageDTO::getHotelId,
                HotelImageDTO::getUrl));

        hotels.getContent().forEach(hotel -> hotel.setMainImageUrl(imagesMap.get(hotel.getId())));

        return hotels;
    }

    public List<RoomAvgRatingDTO> getHotelRoomsInfo(String slug) {

        List<RoomAvgRatingDTO> hotelRooms = roomService.getRoomsByHotelSlug(slug);

        List<HotelImageDTO> hotelRoomsMainImages = imageService.getHotelRoomsMainImages(slug);

        Map<Long, String> imagesMap = hotelRoomsMainImages.stream().collect(Collectors.toMap(
                HotelImageDTO::getRoomId,
                HotelImageDTO::getUrl));

        hotelRooms.forEach(room -> room.setMainImageUrl(imagesMap.get(room.getId())));

        return hotelRooms;
    }
}
