package com.urjc.plushotel.controllers;

import com.urjc.plushotel.config.SecurityConfig;
import com.urjc.plushotel.dtos.response.ReservationDTO;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.JwtService;
import com.urjc.plushotel.services.ReservationService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class)
@Import(SecurityConfig.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser
    void getReservationsTest() throws Exception {

        ReservationDTO reservation1 = new ReservationDTO(1L, "RSV-123L", 1L, "Room1", LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDateTime.now());

        ReservationDTO reservation2 = new ReservationDTO(2L, "RSV-124L", 1L, "Room1", LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(6), LocalDateTime.now());

        List<ReservationDTO> reservations = List.of(reservation1, reservation2);

        when(reservationService.getAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVATIONS_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].reservationIdentifier").value("RSV-123L"));
    }

    @Test
    @WithMockUser
    void getReservationByReservationIdentifierTest() throws Exception {

        ReservationDTO reservation = new ReservationDTO(1L, "RSV-123L", 1L, "Room1", LocalDate.now(),
                LocalDate.now().plusDays(3), LocalDateTime.now());

        when(reservationService.getReservationByIdentifier(anyString())).thenReturn(reservation);

        mockMvc.perform(get("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL,
                        "RSV-123L"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationIdentifier").value("RSV-123L"))
                .andExpect(jsonPath("$.roomName").value("Room1"));
    }

    @Test
    @WithMockUser
    void getReservedDatesByRoomIdTest() throws Exception {

        ReservedDatesDTO dates1 = new ReservedDatesDTO(LocalDate.parse("2025-12-23"), LocalDate.now().plusDays(3));
        ReservedDatesDTO dates2 = new ReservedDatesDTO(LocalDate.now().plusDays(3), LocalDate.now().plusDays(6));
        ReservedDatesDTO dates3 = new ReservedDatesDTO(LocalDate.now().plusDays(6), LocalDate.now().plusDays(9));

        List<ReservedDatesDTO> reservedDates = List.of(dates1, dates2, dates3);

        when(reservationService.getReservedDatesByRoomId(any())).thenReturn(reservedDates);

        mockMvc.perform(get("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVED_DATES_BY_ROOM_ID,
                        "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].startDate").value("2025-12-23"));
    }

    @Test
    @WithMockUser
    void reserveRoomTest() throws Exception {

        String reservationRequest = """
                {
                    "startDate": "2025-12-23",
                    "endDate": "2025-12-29",
                    "roomId": "1"
                }
                """;

        ReservationDTO reservation = new ReservationDTO(1L, "RSV-123", 1L, "Room1", LocalDate.parse("2025-12-29"),
                LocalDate.parse("2025-12-31"), LocalDateTime.now());

        when(reservationService.reserveRoom(any(), any())).thenReturn(reservation);

        mockMvc.perform(post("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVATIONS_CREATE_URL, "1")
                        .content(reservationRequest)
                        .header("Content-Type", "application/json")
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/reservations/RSV-123")))
                .andExpect(jsonPath("$.startDate").value("2025-12-29"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRoomTest() throws Exception {

        String request = """
                {
                    "startDate": "2025-12-23",
                    "endDate": "2025-12-25"
                }
                """;

        ReservationDTO updatedReservation = new ReservationDTO(1L, "RSV-123", 1L, "Room1",
                LocalDate.parse("2025-12-23"), LocalDate.parse("2025-12-25"),
                LocalDateTime.parse("2025-12-25T01:00:00")
        );

        when(reservationService.updateReservation(anyString(), any())).thenReturn(updatedReservation);

        mockMvc.perform(patch("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL, "1")
                        .content(request)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value("2025-12-23"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelReservationTest() throws Exception {

        mockMvc.perform(delete("/api/v1" + EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL,
                        "RSV-123"))
                .andExpect(status().isNoContent());
    }
}
