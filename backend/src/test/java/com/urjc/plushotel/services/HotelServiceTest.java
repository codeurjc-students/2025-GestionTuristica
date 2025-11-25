package com.urjc.plushotel.services;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();


        Room room1 = Room.builder().name("Room 1").description("").price(BigDecimal.ONE).available(true).build();
        Room room2 = Room.builder().name("Room 2").description("").price(BigDecimal.TWO).available(true).build();

        h1.getRooms().add(room1);
        h1.getRooms().add(room2);

        hotelService.createHotel(h1);

        assertEquals(room1.getHotel(), h1);
        assertEquals(room2.getHotel(), h1);

        verify(hotelRepository, times(1)).save(h1);
    }
}
