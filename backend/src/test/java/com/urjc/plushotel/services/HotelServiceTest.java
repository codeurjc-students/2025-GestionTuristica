package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @InjectMocks
    HotelService hotelService;

    @Mock
    HotelRepository hotelRepository;

    @Test
    public void findAllTest() {

        Hotel h1 = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/ Example 4, Madrid", 3);
        Hotel h2 = new Hotel("H2", "Hotel2 desc", "España", "Barcelona", "C/ Example 3, Barcelona", 4);

        List<Hotel> hotels = List.of(h1, h2);

        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.findAllHotels();

        assertEquals(2, result.size());
        assertEquals(h1, result.getFirst());
        assertEquals(h2, result.getLast());

        verify(hotelRepository, times(1)).findAll();
    }
}
