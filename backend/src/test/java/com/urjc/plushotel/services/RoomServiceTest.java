package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getRoomByIdNotFound() {
        when(roomRepository.findRoomWithAverageRatingById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void getRoomEntityById() {
        Room room = Room.builder().id(1L).name("Room 1").description("").price(BigDecimal.ONE).build();
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        Room resultRoom = roomService.getRoomEntityById(1L);

        assertNotNull(resultRoom);
        assertEquals(room.getId(), resultRoom.getId());
        assertEquals(room.getName(), resultRoom.getName());
        assertEquals(room.getDescription(), resultRoom.getDescription());
        assertEquals(room.getPrice(), resultRoom.getPrice());
    }

    @Test
    void getRoomEntityByIdNotFound() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roomService.getRoomEntityById(1L));
    }

    @Test
    void getRoomsByHotelId() {
        RoomAvgRatingDTO roomDTO1 = new RoomAvgRatingDTO(1L, "room1", "desc1", BigDecimal.TEN, 3.3);
        RoomAvgRatingDTO roomDTO2 = new RoomAvgRatingDTO(2L, "room2", "desc2", BigDecimal.TWO, 3.7);
        when(roomRepository.findRoomsWithAverageRating(anyLong())).thenReturn(List.of(roomDTO1, roomDTO2));

        List<RoomAvgRatingDTO> result = roomService.getRoomsByHotelId(1L);

        assertNotNull(result);
        assertEquals(roomDTO1.getId(), result.getFirst().getId());
        assertEquals(roomDTO2.getId(), result.getLast().getId());
        assertEquals(roomDTO1.getName(), result.getFirst().getName());
        assertEquals(roomDTO2.getName(), result.getLast().getName());
        assertEquals(roomDTO1.getDescription(), result.getFirst().getDescription());
        assertEquals(roomDTO2.getDescription(), result.getLast().getDescription());
        assertEquals(roomDTO1.getPrice(), result.getFirst().getPrice());
        assertEquals(roomDTO2.getPrice(), result.getLast().getPrice());
    }
}
