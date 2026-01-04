package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.services.RoomService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    void getRoomByRoomIdTest() throws Exception {

        Room room = Room.builder().id(1L).name("Room1").description("Small room").price(BigDecimal.TEN).build();

        when(roomService.getRoomById(anyLong())).thenReturn(room);

        mockMvc.perform(get("/api/v1" + EndpointConstants.RoomsEndpoints.ROOMS_ID_URL, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Room1"))
                .andExpect(jsonPath("$.description").value("Small room"))
                .andExpect(jsonPath("$.price").value(10));
    }
}
