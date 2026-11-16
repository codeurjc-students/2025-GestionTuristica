package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.HotelImageUpdateRequest;
import com.urjc.plushotel.dtos.response.HotelImageDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.HotelImage;
import com.urjc.plushotel.entities.ImageType;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.exceptions.ImageNotFoundException;
import com.urjc.plushotel.exceptions.RoomNotFoundException;
import com.urjc.plushotel.repositories.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MinioService minioService;

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ImageService imageService;

    @Test
    void uploadImageHotelTest() {

        MultipartFile file = mock(MultipartFile.class);

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        HotelImage savedImage = new HotelImage("file.jpg", hotel, 0);
        savedImage.setId(1L);

        when(hotelService.findBySlug(any())).thenReturn(hotel);

        when(minioService.uploadImage(any(MultipartFile.class))).thenReturn("file.jpg");

        when(imageRepository.save(any(HotelImage.class))).thenReturn(savedImage);

        HotelImage image = imageService.uploadImage(file, "hotel-test", 0);

        assertNotNull(image);
        assertEquals("file.jpg", image.getFileName());
        assertEquals(0, image.getPosition());
        assertEquals(1L, image.getHotel().getId());
    }

    @Test
    void uploadImageRoomTest() {

        MultipartFile file = mock(MultipartFile.class);

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room.setId(1L);

        HotelImage savedImage = new HotelImage("file.jpg", hotel, room, 0);
        savedImage.setId(1L);

        when(roomService.getRoomEntityById(1L)).thenReturn(room);

        when(minioService.uploadImage(any(MultipartFile.class))).thenReturn("file.jpg");

        when(imageRepository.save(any(HotelImage.class))).thenReturn(savedImage);

        HotelImage image = imageService.uploadImage(file, 1L, 0);

        assertNotNull(image);
        assertEquals("file.jpg", image.getFileName());
        assertEquals(0, image.getPosition());
        assertEquals(1L, image.getRoom().getId());
    }

    @Test
    void getImagesByRoomIdTest() {

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room.setId(1L);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, room, 0);
        image1.setId(1L);

        HotelImage image2 = new HotelImage("file2.jpg", hotel, room, 1);
        image2.setId(2L);

        List<HotelImage> images = List.of(image1, image2);

        when(imageRepository.findByRoom_IdOrderByPosition(1L)).thenReturn(images);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url1");
        when(minioService.getImageUrl("file2.jpg")).thenReturn("url2");

        List<HotelImageDTO> imagesByRoomId = imageService.getImagesByRoomId(1L);

        assertNotNull(imagesByRoomId);
        assertEquals("url1", imagesByRoomId.getFirst().getUrl());
        assertEquals("url2", imagesByRoomId.getLast().getUrl());
    }

    @Test
    void getHotelsMainImagesTest() {

        Hotel h1 = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Hotel h2 = new Hotel(2L, "H2", "Hotel2 desc", "España", "Madrid", "C/" +
                " Example 5, Madrid", 3, "h2", 3.6, new ArrayList<>(), false);

        HotelImage image1 = new HotelImage("file1.jpg", h1, 0);
        image1.setId(1L);

        HotelImage image2 = new HotelImage("file2.jpg", h2, 0);
        image2.setId(2L);

        List<HotelImage> images = List.of(image1, image2);

        when(imageRepository.findByPositionAndRoomNullAndHotel_IdIn(anyInt(), anyList())).thenReturn(images);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url1");
        when(minioService.getImageUrl("file2.jpg")).thenReturn("url2");

        List<HotelImageDTO> hotelImages = imageService.getHotelsMainImages(List.of(1L, 2L));

        assertNotNull(hotelImages);
        assertEquals("url1", hotelImages.getFirst().getUrl());
        assertEquals("url2", hotelImages.getLast().getUrl());
    }

    @Test
    void getHotelRoomsMainImagesTest() {

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room1 = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room1.setId(1L);
        Room room2 = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room2.setId(2L);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, room1, 0);
        image1.setId(1L);

        HotelImage image2 = new HotelImage("file2.jpg", hotel, room2, 0);
        image2.setId(2L);

        List<HotelImage> images = List.of(image1, image2);

        when(imageRepository.findByPositionAndRoom_IdIn(0, List.of(1L, 2L))).thenReturn(images);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url1");
        when(minioService.getImageUrl("file2.jpg")).thenReturn("url2");

        List<HotelImageDTO> hotelImages = imageService.getHotelRoomsMainImages(List.of(1L, 2L));

        assertNotNull(hotelImages);
        assertEquals("url1", hotelImages.getFirst().getUrl());
        assertEquals("url2", hotelImages.getLast().getUrl());
    }

    @Test
    void getHotelImagesTest() {

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room = new Room("Room 1", "", BigDecimal.ONE, null);
        room.setId(1L);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 0);
        image1.setId(1L);

        HotelImage image2 = new HotelImage("file2.jpg", hotel, room, 0);
        image2.setId(2L);

        List<HotelImage> images = List.of(image1, image2);

        when(imageRepository.findByHotel_SlugAndRoomIsNullOrderByPosition("hotel-test")).thenReturn(images);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url1");
        when(minioService.getImageUrl("file2.jpg")).thenReturn("url2");

        List<HotelImageDTO> hotelImages = imageService.getHotelImages("hotel-test");

        assertNotNull(hotelImages);
        assertEquals("url1", hotelImages.getFirst().getUrl());
        assertEquals("url2", hotelImages.getLast().getUrl());
    }

    @Test
    void deleteImageSuccessTest() {

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 0);
        image1.setId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));

        imageService.deleteImage(1L);

        verify(minioService, times(1)).deleteImage("file1.jpg");
        verify(imageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteImageNotFoundTest() {

        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));

        verify(minioService, times(0)).deleteImage(anyString());
        verify(imageRepository, times(0)).deleteById(any());
    }

    @Test
    void updateImageSuccessfulTest() {

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(1L, ImageType.ROOM, 1);

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room.setId(1L);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 1);
        image1.setId(1L);

        HotelImage savedImage = new HotelImage("file1.jpg", hotel, room, 0);
        savedImage.setId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(roomService.getRoomEntityById(1L)).thenReturn(room);
        when(imageRepository.save(any(HotelImage.class))).thenReturn(savedImage);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url");

        HotelImageDTO updatedImage = imageService.updateImage(1L, request);

        assertNotNull(updatedImage);
        assertEquals(1L, updatedImage.getRoomId());
        assertEquals(0, updatedImage.getPosition());
        assertEquals("url", updatedImage.getUrl());

        verify(imageRepository, times(1)).findById(1L);
        verify(roomService, times(1)).getRoomEntityById(1L);
        verify(imageRepository, times(1)).save(any(HotelImage.class));
        verify(minioService, times(1)).getImageUrl(anyString());
    }

    @Test
    void updateImageSuccessfulNullRoomIdTest() {

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(null, ImageType.ROOM, 1);

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        Room room = new Room("Room 1", "", BigDecimal.ONE, hotel);
        room.setId(1L);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, room, 0);
        image1.setId(1L);

        HotelImage savedImage = new HotelImage("file1.jpg", hotel, 1);
        savedImage.setId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(imageRepository.save(any(HotelImage.class))).thenReturn(savedImage);
        when(minioService.getImageUrl("file1.jpg")).thenReturn("url");

        HotelImageDTO updatedImage = imageService.updateImage(1L, request);

        assertNotNull(updatedImage);
        assertNull(updatedImage.getRoomId());
        assertEquals(1, updatedImage.getPosition());
        assertEquals("url", updatedImage.getUrl());

        verify(imageRepository, times(1)).findById(1L);
        verify(roomService, times(0)).getRoomEntityById(1L);
        verify(imageRepository, times(1)).save(any(HotelImage.class));
        verify(minioService, times(1)).getImageUrl(anyString());
    }

    @Test
    void updateImageNotFoundTest() {

        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);

        HotelImage image1 = new HotelImage("file1.jpg", hotel, 0);
        image1.setId(1L);

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(1L, ImageType.ROOM, 1);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(roomService.getRoomEntityById(1L)).thenThrow(RoomNotFoundException.class);

        assertThrows(RoomNotFoundException.class, () -> imageService.updateImage(1L, request));

        verify(imageRepository, times(1)).findById(1L);
        verify(roomService, times(1)).getRoomEntityById(1L);
        verify(imageRepository, times(0)).save(any(HotelImage.class));
        verify(minioService, times(0)).getImageUrl(anyString());
    }

    @Test
    void updateImageRoomNotFoundTest() {

        HotelImageUpdateRequest request = new HotelImageUpdateRequest(null, ImageType.ROOM, 1);

        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageService.updateImage(1L, request));

        verify(imageRepository, times(1)).findById(1L);
        verify(roomService, times(0)).getRoomEntityById(1L);
        verify(imageRepository, times(0)).save(any(HotelImage.class));
        verify(minioService, times(0)).getImageUrl(anyString());
    }
}
