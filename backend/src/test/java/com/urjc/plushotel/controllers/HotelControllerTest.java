package com.urjc.plushotel.controllers;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

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

}
