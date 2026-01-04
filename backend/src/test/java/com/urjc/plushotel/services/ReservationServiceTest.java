package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.response.ReservationDTO;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomService roomService;

    @Test
    void getAllReservationsTest() {

        Room room = new Room();

        Reservation reservation1 = new Reservation(1L, "RSV-123", room, LocalDateTime.now(),
                LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Reservation reservation2 = new Reservation(2L, "RSV-1234", room, LocalDateTime.now(),
                LocalDate.parse("2025-12-26"), LocalDate.parse("2025-12-29"));

        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<ReservationDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("RSV-123", result.getFirst().getReservationIdentifier());
        assertEquals("RSV-1234", result.getLast().getReservationIdentifier());

        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservedDatesByRoomIdTest() {

        ReservedDatesDTO reservedDate1 = new ReservedDatesDTO(LocalDate.parse("2025-12-23"),
                LocalDate.parse("2025-12-26"));

        ReservedDatesDTO reservedDate2 = new ReservedDatesDTO(LocalDate.parse("2025-12-27"),
                LocalDate.parse("2025-12-31"));

        List<ReservedDatesDTO> reservedDates = List.of(reservedDate1, reservedDate2);

        when(reservationRepository.findReservedDatesByRoomId(any())).thenReturn(reservedDates);

        List<ReservedDatesDTO> result = reservationService.getReservedDatesByRoomId(1L);

        assertNotNull(result);
        assertEquals(LocalDate.parse("2025-12-23"), reservedDates.getFirst().getStartDate());
        assertEquals(LocalDate.parse("2025-12-27"), reservedDates.getLast().getStartDate());

        verify(reservationRepository, times(1)).findReservedDatesByRoomId(any());
    }

    @Test
    void getReservationByIdentifierTest() {

        Room room = Room.builder().id(1L).name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, LocalDateTime.now(),
                LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        when(reservationRepository.findByReservationIdentifier(anyString())).thenReturn(Optional.of(reservation));

        ReservationDTO result = reservationService.getReservationByIdentifier("RSV-123");

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getReservationIdentifier(), result.getReservationIdentifier());
        assertEquals(reservation.getStartDate(), result.getStartDate());
        assertEquals(reservation.getEndDate(), result.getEndDate());
        assertEquals(reservation.getRoom().getId(), result.getRoomId());
        assertEquals(reservation.getRoom().getName(), result.getRoomName());
        assertEquals(reservation.getCreatedAt(), result.getCreatedAt());

        verify(reservationRepository, times(1)).findByReservationIdentifier(anyString());
    }

    @Test
    void reserveRoomTest() {

        Room room = Room.builder().id(1L).name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, LocalDateTime.now(),
                LocalDate.parse("2025-12-19"), LocalDate.parse("2025-12-22"));

        ReservedDatesDTO reservedDate1 = new ReservedDatesDTO(LocalDate.parse("2025-12-23"),
                LocalDate.parse("2025-12-26"));

        ReservedDatesDTO reservedDate2 = new ReservedDatesDTO(LocalDate.parse("2025-12-27"),
                LocalDate.parse("2025-12-31"));

        List<ReservedDatesDTO> reservedDates = List.of(reservedDate1, reservedDate2);

        when(reservationRepository.findReservedDatesByRoomId(any())).thenReturn(reservedDates);
        when(roomService.getRoomById(anyLong())).thenReturn(room);
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationDTO result = reservationService.reserveRoom(1L, reservation);

        assertEquals(reservation.getReservationIdentifier(), result.getReservationIdentifier());
        assertEquals(reservation.getStartDate(), result.getStartDate());
        assertEquals(reservation.getEndDate(), result.getEndDate());
        assertEquals(reservation.getRoom().getId(), result.getRoomId());
        assertEquals(reservation.getRoom().getName(), result.getRoomName());

        verify(reservationRepository, times(1)).findReservedDatesByRoomId(any());
        verify(roomService, times(1)).getRoomById(any());
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void cancelReservationTest() {

        Room room = new Room();

        Reservation reservation = new Reservation(1L, "RSV-123", room, LocalDateTime.now(),
                LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        when(reservationRepository.findByReservationIdentifier(anyString())).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation("RSV-123");

        verify(reservationRepository, times(1)).findByReservationIdentifier(anyString());
        verify(reservationRepository, times(1)).delete(any());
    }
}
