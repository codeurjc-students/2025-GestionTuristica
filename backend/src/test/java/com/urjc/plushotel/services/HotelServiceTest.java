package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.HotelRequest;
import com.urjc.plushotel.dtos.response.HotelAvgRatingDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @InjectMocks
    HotelService hotelService;

    @Mock
    HotelRepository hotelRepository;

    @Test
    void findAllTest() {
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").build();
        Hotel h2 = Hotel.builder().name("H2").description("Hotel2 desc").country("España").city("Barcelona").address(
                "C/ Example 3, Barcelona").stars(4).slug("h2").build();

        List<Hotel> hotels = List.of(h1, h2);

        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.getAll();

        assertEquals(2, result.size());
        assertEquals(h1, result.getFirst());
        assertEquals(h2, result.getLast());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void createHotelTest() {
        HotelRequest request = HotelRequest.builder().name("H1").description("Hotel1 desc").country("España").city(
                "Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Room room1 = Room.builder().name("Room 1").description("").price(BigDecimal.ONE).build();
        Room room2 = Room.builder().name("Room 2").description("").price(BigDecimal.TWO).build();

        request.getRooms().add(room1);
        request.getRooms().add(room2);

        Hotel savedHotel = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city(
                "Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(List.of(room1, room2)).build();

        when(hotelRepository.save(any())).thenReturn(savedHotel);

        Hotel result = hotelService.createHotel(request);

        assertEquals(request.getRooms(), result.getRooms());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void updateHotelTest() {
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        HotelRequest request =
                HotelRequest.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                        .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(new ArrayList<>()).build();

        Hotel updatedH1 = Hotel.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(new ArrayList<>()).build();

        when(hotelRepository.findBySlug(anyString())).thenReturn(Optional.of(h1));
        when(hotelRepository.save(any())).thenReturn(updatedH1);

        Hotel updatedHotel = hotelService.updateHotel(request, "h1");

        assertEquals(updatedHotel.getName(), updatedH1.getName());
        assertEquals(updatedHotel.getDescription(), updatedH1.getDescription());
        assertEquals(updatedHotel.getCountry(), updatedH1.getCountry());
        assertEquals(updatedHotel.getCity(), updatedH1.getCity());
        assertEquals(updatedHotel.getAddress(), updatedH1.getAddress());
        assertEquals(updatedHotel.getStars(), updatedH1.getStars());
        assertEquals(updatedHotel.getSlug(), updatedH1.getSlug());

        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void updateHotelNotFoundTest() {
        HotelRequest request =
                HotelRequest.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                        .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(new ArrayList<>()).build();

        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.updateHotel(request, "h1"));

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void removeHotelTest() {
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        when(hotelRepository.findBySlug(any())).thenReturn(Optional.of(h1));

        hotelService.removeHotel("h1");

        verify(hotelRepository, times(1)).delete(any());
    }

    @Test
    void removeHotelNotFoundTest() {
        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.removeHotel("h1"));

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void findHotelBySlugSuccessTest() {
        HotelAvgRatingDTO hotel = new HotelAvgRatingDTO(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/ Example 4, " +
                "Madrid", 3, "h1", 3.6);


        when(hotelRepository.findHotelsWithAverageRatingBySlug("h1")).thenReturn(Optional.of(hotel));

        HotelAvgRatingDTO result = hotelService.getHotelBySlug("h1");

        assertEquals(hotel.getName(), result.getName());
        assertEquals(hotel.getSlug(), result.getSlug());
        assertEquals(hotel.getDescription(), result.getDescription());
        assertEquals(hotel.getCountry(), result.getCountry());
        assertEquals(hotel.getCity(), result.getCity());
        assertEquals(hotel.getAddress(), result.getAddress());
        assertEquals(hotel.getStars(), result.getStars());
        assertEquals(hotel.getAverageRating(), result.getAverageRating());
        assertEquals(hotel.getId(), result.getId());

        verify(hotelRepository, times(1)).findHotelsWithAverageRatingBySlug("h1");
    }

    @Test
    void findHotelBySlugNotFoundTest() {
        when(hotelRepository.findHotelsWithAverageRatingBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.getHotelBySlug("h1"));

        verify(hotelRepository, times(1)).findHotelsWithAverageRatingBySlug("h1");
    }
}
