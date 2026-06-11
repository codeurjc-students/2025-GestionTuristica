package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
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

        PageImpl<HotelAvgRatingDTO> paginatedHotels = new PageImpl<>(hotels);

        when(hotelService.getAll(0)).thenReturn(paginatedHotels);

        when(imageService.getHotelsMainImages(List.of(1L, 2L))).thenReturn(images);

        Page<HotelAvgRatingDTO> hotelsInfo = hotelRoomCardService.getHotelsInfo(0);

        List<HotelAvgRatingDTO> hotelsInfoContent = hotelsInfo.getContent();

        assertNotNull(hotelsInfo);

        assertEquals(hotelsInfoContent.getFirst().getMainImageUrl(), image1.getUrl());
        assertEquals(hotelsInfoContent.getLast().getMainImageUrl(), image2.getUrl());
        assertEquals(hotelsInfoContent.getFirst().getId(), h1.getId());
        assertEquals(hotelsInfoContent.getLast().getId(), h2.getId());
        assertEquals(hotelsInfoContent.getFirst().getName(), h1.getName());
        assertEquals(hotelsInfoContent.getLast().getName(), h2.getName());
        assertEquals(hotelsInfoContent.getFirst().getDescription(), h1.getDescription());
        assertEquals(hotelsInfoContent.getLast().getDescription(), h2.getDescription());
        assertEquals(hotelsInfoContent.getFirst().getCountry(), h1.getCountry());
        assertEquals(hotelsInfoContent.getLast().getCountry(), h2.getCountry());
        assertEquals(hotelsInfoContent.getFirst().getCity(), h1.getCity());
        assertEquals(hotelsInfoContent.getLast().getCity(), h2.getCity());
        assertEquals(hotelsInfoContent.getFirst().getAddress(), h1.getAddress());
        assertEquals(hotelsInfoContent.getLast().getAddress(), h2.getAddress());
        assertEquals(hotelsInfoContent.getFirst().getStars(), h1.getStars());
        assertEquals(hotelsInfoContent.getLast().getStars(), h2.getStars());
        assertEquals(hotelsInfoContent.getFirst().getSlug(), h1.getSlug());
        assertEquals(hotelsInfoContent.getLast().getSlug(), h2.getSlug());
        assertEquals(hotelsInfoContent.getFirst().getAverageRating(), h1.getAverageRating());
        assertEquals(hotelsInfoContent.getLast().getAverageRating(), h2.getAverageRating());
    }

    @Test
    void getHotelRoomsInfoTest() {

        HotelImageDTO image1 = new HotelImageDTO(1L, "url1", 1L, 1L, 0);
        HotelImageDTO image2 = new HotelImageDTO(2L, "url2", 1L, 2L, 0);

        List<HotelImageDTO> images = List.of(image1, image2);

        RoomAvgRatingDTO roomDTO1 = new RoomAvgRatingDTO(1L, "room1", "desc1", BigDecimal.TEN, 3.3);
        RoomAvgRatingDTO roomDTO2 = new RoomAvgRatingDTO(2L, "room2", "desc2", BigDecimal.TWO, 3.7);

        List<RoomAvgRatingDTO> rooms = List.of(roomDTO1, roomDTO2);

        PageImpl<RoomAvgRatingDTO> paginatedRooms = new PageImpl<>(rooms);

        when(roomService.getRoomsByHotelSlug(anyString(), anyInt())).thenReturn(paginatedRooms);

        when(imageService.getHotelRoomsMainImages(anyList())).thenReturn(images);

        Page<RoomAvgRatingDTO> hotelRoomsInfo = hotelRoomCardService.getHotelRoomsInfo("h1", 0);

        List<RoomAvgRatingDTO> hotelRoomsInfoContent = hotelRoomsInfo.getContent();

        assertNotNull(hotelRoomsInfo);
        assertEquals(hotelRoomsInfoContent.getFirst().getId(), roomDTO1.getId());
        assertEquals(hotelRoomsInfoContent.getLast().getId(), roomDTO2.getId());
        assertEquals(hotelRoomsInfoContent.getFirst().getName(), roomDTO1.getName());
        assertEquals(hotelRoomsInfoContent.getLast().getName(), roomDTO2.getName());
        assertEquals(hotelRoomsInfoContent.getFirst().getDescription(), roomDTO1.getDescription());
        assertEquals(hotelRoomsInfoContent.getLast().getDescription(), roomDTO2.getDescription());
        assertEquals(hotelRoomsInfoContent.getFirst().getPrice(), roomDTO1.getPrice());
        assertEquals(hotelRoomsInfoContent.getLast().getPrice(), roomDTO2.getPrice());
        assertEquals(hotelRoomsInfoContent.getFirst().getAverageRating(), roomDTO1.getAverageRating());
        assertEquals(hotelRoomsInfoContent.getLast().getAverageRating(), roomDTO2.getAverageRating());
        assertEquals(hotelRoomsInfoContent.getFirst().getMainImageUrl(), image1.getUrl());
        assertEquals(hotelRoomsInfoContent.getLast().getMainImageUrl(), image2.getUrl());
    }
}
