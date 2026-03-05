package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Test
    void getRoomById() {
        RoomAvgRatingDTO roomDTO = new RoomAvgRatingDTO(1L, "room", "desc", BigDecimal.TEN, 3.3);
        when(roomRepository.findRoomWithAverageRatingById(anyLong())).thenReturn(Optional.of(roomDTO));

        RoomAvgRatingDTO resultRoom = roomService.getRoomById(1L);

        assertNotNull(resultRoom);
        assertEquals(roomDTO.getId(), resultRoom.getId());
        assertEquals(roomDTO.getName(), resultRoom.getName());
        assertEquals(roomDTO.getDescription(), resultRoom.getDescription());
        assertEquals(roomDTO.getPrice(), resultRoom.getPrice());
    }
}
