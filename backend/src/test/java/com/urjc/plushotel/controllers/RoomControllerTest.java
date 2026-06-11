package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.response.RoomAvgRatingDTO;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.HotelRoomCardService;
import com.urjc.plushotel.services.JwtService;
import com.urjc.plushotel.services.RoomService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private HotelRoomCardService hotelRoomCardService;

    @Test
    @WithMockUser
    void getRoomByRoomIdTest() throws Exception {

        RoomAvgRatingDTO room =
                new RoomAvgRatingDTO(1L, "Room1", "Small room", BigDecimal.TEN, 4.3);

        when(roomService.getRoomById(anyLong())).thenReturn(room);

        mockMvc.perform(get("/api/v1" + EndpointConstants.RoomsEndpoints.ROOMS_ID_URL, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Room1"))
                .andExpect(jsonPath("$.description").value("Small room"))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    @WithMockUser
    void getRoomByRoomByHotelIdTest() throws Exception {

        RoomAvgRatingDTO room1 =
                new RoomAvgRatingDTO(1L, "Room1", "Small room", BigDecimal.TEN, 4.3);

        RoomAvgRatingDTO room2 =
                new RoomAvgRatingDTO(2L, "Room2", "Big room", BigDecimal.TWO, 4.3);

        List<RoomAvgRatingDTO> rooms = List.of(room1, room2);

        PageImpl<RoomAvgRatingDTO> paginatedRooms = new PageImpl<>(rooms);

        when(hotelRoomCardService.getHotelRoomsInfo(anyString(), anyInt())).thenReturn(paginatedRooms);

        mockMvc.perform(get("/api/v1" + EndpointConstants.RoomsEndpoints.ROOMS_HOTEL_SLUG_URL + "?page=0", "hotel" +
                        "-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(1))
                .andExpect(jsonPath("$.content.[0].name").value("Room1"))
                .andExpect(jsonPath("$.content.[0].description").value("Small room"))
                .andExpect(jsonPath("$.content.[0].price").value(10))
                .andExpect(jsonPath("$.content.[1].id").value(2))
                .andExpect(jsonPath("$.content.[1].name").value("Room2"))
                .andExpect(jsonPath("$.content.[1].description").value("Big room"))
                .andExpect(jsonPath("$.content.[1].price").value(2));
    }
}
