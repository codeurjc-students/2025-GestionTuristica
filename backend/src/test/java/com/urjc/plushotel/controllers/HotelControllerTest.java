package com.urjc.plushotel.controllers;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.services.HotelService;

@WebMvcTest(HotelController.class)
public class HotelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    public void findAllHotelsTest() throws Exception {

        List<Hotel> hotels = List.of(new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/ Example 4, Madrid", 3),
                new Hotel("H2", "Hotel2 desc", "España", "Barcelona", "C/ Example 3, Barcelona", 4));

        when(hotelService.findAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/api/v1/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("H1"))
                .andExpect(jsonPath("$[1].city").value("Barcelona"));
    }

}
