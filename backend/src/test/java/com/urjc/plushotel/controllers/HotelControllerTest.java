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

        List<Hotel> hotels = List.of(new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/ Example 4, Madrid", 3
                        , "h1"),
                new Hotel("H2", "Hotel2 desc", "España", "Barcelona", "C/ Example 3, Barcelona", 4, "h2"));

        when(hotelService.getAll()).thenReturn(hotels);

        mockMvc.perform(get("/api/v1/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("H1"))
                .andExpect(jsonPath("$[1].city").value("Barcelona"));
    }

}
