package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Test
    void getRoomByIdAndHotelSlugTest() {
        Room room = Room.builder().id(1L).name("Room 1").description("").price(BigDecimal.TEN).build();
        when(roomRepository.findByIdAndHotel_Slug(anyLong(), anyString())).thenReturn(Optional.of(room));

        Room resultRoom = roomService.getRoomByIdAndHotelSlug(1L, "hotel-test");

        assertNotNull(resultRoom);
        assertEquals(room, resultRoom);
    }
}
