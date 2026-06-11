package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.exceptions.RoomNotFoundException;
import com.urjc.plushotel.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
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

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomEntityById(1L));
    }

    @Test
    void deleteRoomSuccessfulTest() {
        Room room = Room.builder().id(1L).name("Room 1").description("").price(BigDecimal.ONE).deleted(false).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.deleteRoom(1L);

        assertTrue(room.isDeleted());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void deleteRoomNotFoundTest() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.deleteRoom(1L));

        verify(roomRepository, times(0)).save(any());
    }
}
