package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelRoomCardServiceTest {

    @Mock
    private ImageService imageService;

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private HotelRoomCardService hotelRoomCardService;

    @Test
    void getHotelsInfoTest() {

        HotelImageDTO image1 = new HotelImageDTO(1L, "url1", 1L, 0);
        HotelImageDTO image2 = new HotelImageDTO(3L, "url3", 2L, 0);

        List<HotelImageDTO> images = List.of(image1, image2);

        HotelAvgRatingDTO h1 = new HotelAvgRatingDTO(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6);
        HotelAvgRatingDTO h2 = new HotelAvgRatingDTO(2L, "H2", "Hotel2 desc", "España", "Barcelona", "C/" +
                " Example 3, Barcelona", 4, "h2", 4.2);

        List<HotelAvgRatingDTO> hotels = List.of(h1, h2);

        when(hotelService.getAll()).thenReturn(hotels);

        when(imageService.getHotelsMainImages()).thenReturn(images);

        List<HotelAvgRatingDTO> hotelsInfo = hotelRoomCardService.getHotelsInfo();

        assertNotNull(hotelsInfo);

        assertEquals(hotelsInfo.getFirst().getMainImageUrl(), image1.getUrl());
        assertEquals(hotelsInfo.getLast().getMainImageUrl(), image2.getUrl());
        assertEquals(hotelsInfo.getFirst().getId(), h1.getId());
        assertEquals(hotelsInfo.getLast().getId(), h2.getId());
        assertEquals(hotelsInfo.getFirst().getName(), h1.getName());
        assertEquals(hotelsInfo.getLast().getName(), h2.getName());
        assertEquals(hotelsInfo.getFirst().getDescription(), h1.getDescription());
        assertEquals(hotelsInfo.getLast().getDescription(), h2.getDescription());
        assertEquals(hotelsInfo.getFirst().getCountry(), h1.getCountry());
        assertEquals(hotelsInfo.getLast().getCountry(), h2.getCountry());
        assertEquals(hotelsInfo.getFirst().getCity(), h1.getCity());
        assertEquals(hotelsInfo.getLast().getCity(), h2.getCity());
        assertEquals(hotelsInfo.getFirst().getAddress(), h1.getAddress());
        assertEquals(hotelsInfo.getLast().getAddress(), h2.getAddress());
        assertEquals(hotelsInfo.getFirst().getStars(), h1.getStars());
        assertEquals(hotelsInfo.getLast().getStars(), h2.getStars());
        assertEquals(hotelsInfo.getFirst().getSlug(), h1.getSlug());
        assertEquals(hotelsInfo.getLast().getSlug(), h2.getSlug());
        assertEquals(hotelsInfo.getFirst().getAverageRating(), h1.getAverageRating());
        assertEquals(hotelsInfo.getLast().getAverageRating(), h2.getAverageRating());
    }

    @Test
    void getHotelRoomsInfoTest() {

        HotelImageDTO image1 = new HotelImageDTO(1L, "url1", 1L, 1L, 0);
        HotelImageDTO image2 = new HotelImageDTO(2L, "url2", 1L, 2L, 0);

        List<HotelImageDTO> images = List.of(image1, image2);

        RoomAvgRatingDTO roomDTO1 = new RoomAvgRatingDTO(1L, "room1", "desc1", BigDecimal.TEN, 3.3);
        RoomAvgRatingDTO roomDTO2 = new RoomAvgRatingDTO(2L, "room2", "desc2", BigDecimal.TWO, 3.7);

        List<RoomAvgRatingDTO> rooms = List.of(roomDTO1, roomDTO2);

        when(roomService.getRoomsByHotelSlug(anyString())).thenReturn(rooms);

        when(imageService.getHotelRoomsMainImages(anyString())).thenReturn(images);

        List<RoomAvgRatingDTO> hotelRoomsInfo = hotelRoomCardService.getHotelRoomsInfo("h1");

        assertNotNull(hotelRoomsInfo);
        assertEquals(hotelRoomsInfo.getFirst().getId(), roomDTO1.getId());
        assertEquals(hotelRoomsInfo.getLast().getId(), roomDTO2.getId());
        assertEquals(hotelRoomsInfo.getFirst().getName(), roomDTO1.getName());
        assertEquals(hotelRoomsInfo.getLast().getName(), roomDTO2.getName());
        assertEquals(hotelRoomsInfo.getFirst().getDescription(), roomDTO1.getDescription());
        assertEquals(hotelRoomsInfo.getLast().getDescription(), roomDTO2.getDescription());
        assertEquals(hotelRoomsInfo.getFirst().getPrice(), roomDTO1.getPrice());
        assertEquals(hotelRoomsInfo.getLast().getPrice(), roomDTO2.getPrice());
        assertEquals(hotelRoomsInfo.getFirst().getAverageRating(), roomDTO1.getAverageRating());
        assertEquals(hotelRoomsInfo.getLast().getAverageRating(), roomDTO2.getAverageRating());
        assertEquals(hotelRoomsInfo.getFirst().getMainImageUrl(), image1.getUrl());
        assertEquals(hotelRoomsInfo.getLast().getMainImageUrl(), image2.getUrl());
    }
}
