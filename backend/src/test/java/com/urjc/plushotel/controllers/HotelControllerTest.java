package com.urjc.plushotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllHotelsTest() throws Exception {

        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").build();
        Hotel h2 = Hotel.builder().name("H2").description("Hotel2 desc").country("España").city("Barcelona").address(
                "C/ Example 3, Barcelona").stars(4).slug("h2").build();

        List<Hotel> hotels = List.of(h1, h2);

        when(hotelService.getAll()).thenReturn(hotels);

        mockMvc.perform(get("/api/v1/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("H1"))
                .andExpect(jsonPath("$[1].city").value("Barcelona"));
    }

    @Test
    void createHotelTest() throws Exception {

        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").build();

        Hotel savedH1 = Hotel.builder().id(1L).name("H1").description("Hotel1 desc").country("España").city("Madrid")
                .address("C/ Example 4, Madrid").stars(3).slug("h1").build();

        when(hotelService.createHotel(h1)).thenReturn(savedH1);

        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(h1))).andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/hotels/h1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("H1"));
    }

    @Test
    void updateHotelTest() throws Exception {

        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").build();

        Hotel updatedH1 = Hotel.builder().name("Updated H1").description("Hotel1 desc").country("España").city("Madrid")
                .address("C/ Example 4, Madrid").stars(3).slug("updated-h1").build();

        when(hotelService.updateHotel(any(), anyString())).thenReturn(updatedH1);

        mockMvc.perform(put("/api/v1/hotels/{}", "h1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(h1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedH1.getName()));
    }
}
