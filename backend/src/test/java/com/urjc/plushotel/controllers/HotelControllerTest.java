package com.urjc.plushotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urjc.plushotel.config.SecurityConfig;
import com.urjc.plushotel.dtos.request.HotelFilters;
import com.urjc.plushotel.dtos.request.HotelRequest;
import com.urjc.plushotel.dtos.response.HotelDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.exceptions.HotelNotFoundException;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.HotelRoomCardService;
import com.urjc.plushotel.services.HotelService;
import com.urjc.plushotel.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
@Import(SecurityConfig.class)
class HotelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private HotelRoomCardService hotelRoomCardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllHotelsTest() throws Exception {

        HotelDTO h1 = new HotelDTO(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6);
        HotelDTO h2 = new HotelDTO(2L, "H2", "Hotel2 desc", "España", "Barcelona", "C/" +
                " Example 3, Barcelona", 4, "h2", 4.2);

        List<HotelDTO> hotels = List.of(h1, h2);

        PageImpl<HotelDTO> paginatedHotels = new PageImpl<>(hotels);

        when(hotelRoomCardService.getHotelsInfo(anyInt(), any(HotelFilters.class))).thenReturn(paginatedHotels);

        mockMvc.perform(get("/api/v1/hotels?page=0&name=Hotel&rating=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content.[0].name").value("H1"))
                .andExpect(jsonPath("$.content.[1].city").value("Barcelona"));
    }

    @Test
    void findHotelBySlugTest() throws Exception {

        HotelDTO h1 = new HotelDTO(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 1.2);

        when(hotelService.getHotelBySlug(anyString())).thenReturn(h1);

        mockMvc.perform(get("/api/v1/hotels/{}", "h1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("H1"));
    }

    @Test
    void findHotelBySlugNotFoundTest() throws Exception {

        when(hotelService.getHotelBySlug(anyString())).thenThrow(HotelNotFoundException.class);

        mockMvc.perform(get("/api/v1/hotels/{}", "h1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHotelTest() throws Exception {

        HotelRequest request = HotelRequest.builder().name("H1").description("Hotel1 desc").country("España").city(
                "Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").build();

        Hotel savedH1 = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        when(hotelService.createHotel(any())).thenReturn(savedH1);

        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/hotels/h1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("H1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateHotelTest() throws Exception {

        HotelRequest request =
                HotelRequest.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                        " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Room room = new Room("Room 1", "", BigDecimal.ONE, null);
        Room deletedRoom = new Room("Deleted", "", BigDecimal.ONE, null);

        request.getRooms().add(room);
        request.getRooms().add(deletedRoom);

        Room updatedRoom = new Room("Updated Room 1", "", BigDecimal.ONE, null);
        Room newRoom = new Room("Room 2", "", BigDecimal.ONE, null);

        Hotel updatedH1 = new Hotel(1L, "Updated H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "updated-h1", 3.6, new ArrayList<>(), false);

        updatedH1.addRoom(updatedRoom);
        updatedH1.addRoom(newRoom);

        when(hotelService.updateHotel(any(), anyString())).thenReturn(updatedH1);

        mockMvc.perform(put("/api/v1/hotels/{}", "h1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedH1.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateHotelNotFoundTest() throws Exception {

        HotelRequest request =
                HotelRequest.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                        " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Room room = new Room("Room 1", "", BigDecimal.ONE, null);
        Room deletedRoom = new Room("Deleted", "", BigDecimal.ONE, null);

        request.getRooms().add(room);
        request.getRooms().add(deletedRoom);

        when(hotelService.updateHotel(any(), anyString())).thenThrow(HotelNotFoundException.class);

        mockMvc.perform(put("/api/v1/hotels/{}", "h1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeHotelTest() throws Exception {

        mockMvc.perform(delete("/api/v1/hotels/{}", "h1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeHotelNotFoundTest() throws Exception {

        doThrow(HotelNotFoundException.class).when(hotelService).removeHotel("h1");

        mockMvc.perform(delete("/api/v1/hotels/{}", "h1"))
                .andExpect(status().isNoContent());
    }
}
