package com.urjc.plushotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urjc.plushotel.config.SecurityConfig;
import com.urjc.plushotel.dtos.request.HotelImageUpdateRequest;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.HotelImage;
import com.urjc.plushotel.entities.ImageType;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.ImageService;
import com.urjc.plushotel.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
@Import(SecurityConfig.class)
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadImageTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file".getBytes());

        Hotel hotel = Hotel.builder().id(1L).slug("hotel-test").build();

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 0);
        image1.setId(1L);

        when(imageService.uploadImage(any(), eq("hotel-test"), eq(0))).thenReturn(image1);

        mockMvc.perform(multipart("/api/v1/images/hotel/{slug}", "hotel-test")
                        .file(file)
                        .param("position", "0"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/images/1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadRoomImageTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file".getBytes());

        Room room = Room.builder().id(1L).build();

        Hotel hotel = Hotel.builder().id(1L).slug("hotel-test").build();

        HotelImage image1 = new HotelImage("file1.jpg", hotel, room, 0);
        image1.setId(1L);

        when(imageService.uploadImage(any(), eq(1L), eq(0))).thenReturn(image1);

        mockMvc.perform(multipart("/api/v1/images/room/{roomId}", 1)
                        .file(file)
                        .param("position", "0"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/images/1"));
    }

    @Test
    void getHotelImagesTest() throws Exception {

        HotelImageDTO image1 = new HotelImageDTO(1L, "url1.test", 1L, 0);
        HotelImageDTO image2 = new HotelImageDTO(2L, "url2.test", 1L, 1);

        when(imageService.getHotelImages("hotel-test")).thenReturn(List.of(image1, image2));

        mockMvc.perform(get("/api/v1/images/hotel/{slug}", "hotel-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].url").value("url1.test"))
                .andExpect(jsonPath("$[0].hotelId").value(1))
                .andExpect(jsonPath("$[0].position").value(0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].url").value("url2.test"))
                .andExpect(jsonPath("$[1].hotelId").value(1))
                .andExpect(jsonPath("$[1].position").value(1));

    }

    @Test
    void getRoomImagesTest() throws Exception {

        HotelImageDTO image1 = new HotelImageDTO(1L, "url1.test", 1L, 1L, 0);
        HotelImageDTO image2 = new HotelImageDTO(2L, "url2.test", 1L, 1L, 1);

        when(imageService.getImagesByRoomId(1L)).thenReturn(List.of(image1, image2));

        mockMvc.perform(get("/api/v1/images/room/{roomId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].url").value("url1.test"))
                .andExpect(jsonPath("$[0].hotelId").value(1))
                .andExpect(jsonPath("$[0].roomId").value(1))
                .andExpect(jsonPath("$[0].position").value(0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].url").value("url2.test"))
                .andExpect(jsonPath("$[1].hotelId").value(1))
                .andExpect(jsonPath("$[1].roomId").value(1))
                .andExpect(jsonPath("$[1].position").value(1));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateImageTest() throws Exception {

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(null, ImageType.HOTEL, 2);

        HotelImageDTO updatedImage = new HotelImageDTO(1L, "url1.test", 1L, 2);

        when(imageService.updateImage(eq(1L), any(HotelImageUpdateRequest.class))).thenReturn(updatedImage);

        mockMvc.perform(put("/api/v1/images/{imageId}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value("url1.test"))
                .andExpect(jsonPath("$.hotelId").value(1))
                .andExpect(jsonPath("$.position").value(2));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteImageTest() throws Exception {

        mockMvc.perform(delete("/api/v1/images/{imageId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void uploadImageNoAuthTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file".getBytes());

        Hotel hotel = Hotel.builder().id(1L).slug("hotel-test").build();

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 0);
        image1.setId(1L);

        when(imageService.uploadImage(any(), eq("hotel-test"), eq(0))).thenReturn(image1);

        mockMvc.perform(multipart("/api/v1/images/hotel/{slug}", "hotel-test")
                        .file(file)
                        .param("position", "0"))
                .andExpect(status().isForbidden());
    }

    @Test
    void uploadRoomImageNoAuthTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "file.jpg", "image/jpeg", "file".getBytes());

        Room room = Room.builder().id(1L).build();

        Hotel hotel = Hotel.builder().id(1L).slug("hotel-test").build();

        HotelImage image1 = new HotelImage("file1.jpg", hotel, room, 0);
        image1.setId(1L);

        when(imageService.uploadImage(any(), eq(1L), eq(0))).thenReturn(image1);

        mockMvc.perform(multipart("/api/v1/images/room/{roomId}", 1)
                        .file(file)
                        .param("position", "0"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateImageNoAuthTest() throws Exception {

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(null, ImageType.HOTEL, 2);

        HotelImageDTO updatedImage = new HotelImageDTO(1L, "url1.test", 1L, 2);

        when(imageService.updateImage(eq(1L), any(HotelImageUpdateRequest.class))).thenReturn(updatedImage);

        mockMvc.perform(put("/api/v1/images/{imageId}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

    }

    @Test
    void deleteImageNoAuthTest() throws Exception {

        mockMvc.perform(delete("/api/v1/images/{imageId}", 1))
                .andExpect(status().isForbidden());
    }
}
