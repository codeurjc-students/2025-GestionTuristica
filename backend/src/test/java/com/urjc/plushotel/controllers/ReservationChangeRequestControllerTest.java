package com.urjc.plushotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urjc.plushotel.config.SecurityConfig;
import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.dtos.response.ModificationRequestDTO;
import com.urjc.plushotel.entities.RequestStatus;
import com.urjc.plushotel.entities.RequestType;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.JwtService;
import com.urjc.plushotel.services.ReservationChangeRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationChangeRequestController.class)
@Import(SecurityConfig.class)
class ReservationChangeRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReservationChangeRequestService reservationChangeRequestService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "ADMIN")
    void findReservationChangeRequestsSuccess() throws Exception {

        ModificationRequestDTO request1 =
                ModificationRequestDTO.builder().id(1L).reservationIdentifier("RSV-123").userEmail("john@test.com")
                        .type(RequestType.CANCELLATION).status(RequestStatus.PENDING).build();

        ModificationRequestDTO request2 =
                ModificationRequestDTO.builder().id(2L).reservationIdentifier("RSV-456").userEmail("john@test.com")
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.PENDING).build();

        List<ModificationRequestDTO> requests = List.of(request1, request2);

        when(reservationChangeRequestService.findReservationChangeRequests(any())).thenReturn(requests);

        mockMvc.perform(get("/api/v1/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].reservationIdentifier").value("RSV-123"))
                .andExpect(jsonPath("$[1].reservationIdentifier").value("RSV-456"));

        verify(reservationChangeRequestService, times(1)).findReservationChangeRequests(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createRequestSuccess() throws Exception {

        ModificationRequest modificationRequest = ModificationRequest.builder().type(RequestType.CANCELLATION)
                .userEmail("john@test.com").reservationIdentifier("RSV-123").build();

        mockMvc.perform(post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificationRequest)))
                .andExpect(status().isOk());

        verify(reservationChangeRequestService, times(1)).createRequest(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void approveRequestSuccess() throws Exception {

        mockMvc.perform(patch("/api/v1/requests/approve/1")).andExpect(status().isOk());

        verify(reservationChangeRequestService, times(1)).approveRequest(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void rejectRequestSuccess() throws Exception {

        mockMvc.perform(patch("/api/v1/requests/reject/1")).andExpect(status().isOk());

        verify(reservationChangeRequestService, times(1)).rejectRequest(1L);
    }
}
