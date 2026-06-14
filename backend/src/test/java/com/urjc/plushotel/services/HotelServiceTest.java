package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.HotelRequest;
import com.urjc.plushotel.dtos.response.HotelDTO;
import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.repositories.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @InjectMocks
    HotelService hotelService;

    @Mock
    HotelRepository hotelRepository;

    @Mock
    RoomService roomService;

    @Mock
    ReservationService reservationService;

    @Mock
    ReservationChangeRequestService reservationChangeRequestService;

    @Test
    void findAllTest() {
        Hotel h1 = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/" +
                " Example 4, Madrid", 3, "h1", 3.6, new ArrayList<>(), false);
        Hotel h2 = new Hotel(2L, "H2", "Hotel2 desc", "España", "Barcelona", "C/" +
                " Example 3, Barcelona", 4, "h2", 4.2, new ArrayList<>(), false);

        List<Hotel> hotels = List.of(h1, h2);

        PageImpl<Hotel> paginatedHotels = new PageImpl<>(hotels);

        when(hotelRepository.findAll(Pageable.ofSize(5).withPage(0))).thenReturn(paginatedHotels);

        Page<HotelDTO> result = hotelService.getAll(0);

        List<HotelDTO> resultContent = result.getContent();

        assertEquals(2, resultContent.size());
        assertEquals(h1.getName(), resultContent.getFirst().getName());
        assertEquals(h2.getName(), resultContent.getLast().getName());
        assertEquals(h1.getDescription(), resultContent.getFirst().getDescription());
        assertEquals(h2.getDescription(), resultContent.getLast().getDescription());
        assertEquals(h1.getCountry(), resultContent.getFirst().getCountry());
        assertEquals(h2.getCountry(), resultContent.getLast().getCountry());
        assertEquals(h1.getCity(), resultContent.getFirst().getCity());
        assertEquals(h2.getCity(), resultContent.getLast().getCity());
        assertEquals(h1.getAddress(), resultContent.getFirst().getAddress());
        assertEquals(h2.getAddress(), resultContent.getLast().getAddress());
        assertEquals(h1.getStars(), resultContent.getFirst().getStars());
        assertEquals(h2.getStars(), resultContent.getLast().getStars());
        assertEquals(h1.getSlug(), resultContent.getFirst().getSlug());
        assertEquals(h2.getSlug(), resultContent.getLast().getSlug());

        verify(hotelRepository, times(1)).findAll(Pageable.ofSize(5).withPage(0));
    }

    @Test
    void createHotelTest() {
        HotelRequest request = HotelRequest.builder().name("H1").description("Hotel1 desc").country("España").city(
                "Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Room room1 = Room.builder().name("Room 1").description("").price(BigDecimal.ONE).build();
        Room room2 = Room.builder().name("Room 2").description("").price(BigDecimal.TWO).build();

        request.getRooms().add(room1);
        request.getRooms().add(room2);

        Hotel savedHotel = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city(
                "Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(List.of(room1, room2)).build();

        when(hotelRepository.save(any())).thenReturn(savedHotel);

        Hotel result = hotelService.createHotel(request);

        assertEquals(request.getRooms(), result.getRooms());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void updateHotelTest() {
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Reservation reservation = Reservation.builder().reservationIdentifier("RSV-123").build();

        Room r1 = Room.builder().id(1L).name("Room 1").description("").price(BigDecimal.ONE)
                .reservations(List.of(reservation)).build();

        Room r2 = Room.builder().id(2L).name("Room 2").description("room2").price(BigDecimal.TWO).build();

        h1.addRoom(r1);
        h1.addRoom(r2);

        Room updatedR2 =
                Room.builder().id(2L).name("Updated Room 2").description("Updated room2").price(BigDecimal.TWO).build();

        Room newRoom = Room.builder().name("New Room").description("new room").price(BigDecimal.TEN).build();

        HotelRequest request =
                HotelRequest.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                        .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(List.of(updatedR2, newRoom)).build();

        Hotel updatedH1 = Hotel.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(List.of(updatedR2, newRoom)).build();

        when(hotelRepository.findBySlug(anyString())).thenReturn(Optional.of(h1));
        when(hotelRepository.save(any())).thenReturn(updatedH1);
        when(roomService.getRoomEntityById(2L)).thenReturn(r2);

        Hotel updatedHotel = hotelService.updateHotel(request, "h1");

        assertEquals(updatedHotel.getName(), updatedH1.getName());
        assertEquals(updatedHotel.getDescription(), updatedH1.getDescription());
        assertEquals(updatedHotel.getCountry(), updatedH1.getCountry());
        assertEquals(updatedHotel.getCity(), updatedH1.getCity());
        assertEquals(updatedHotel.getAddress(), updatedH1.getAddress());
        assertEquals(updatedHotel.getStars(), updatedH1.getStars());
        assertEquals(updatedHotel.getSlug(), updatedH1.getSlug());

        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void updateHotelNotFoundTest() {
        HotelRequest request =
                HotelRequest.builder().name("H1 up").description("Hotel1 up desc").country("España").city("Madrid")
                        .address("C/ Example 4, Madrid").stars(3).slug("h1-up").rooms(new ArrayList<>()).build();

        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.updateHotel(request, "h1"));

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void removeHotelTest() {
        Hotel h1 = Hotel.builder().name("H1").description("Hotel1 desc").country("España").city("Madrid").address("C/" +
                " Example 4, Madrid").stars(3).slug("h1").rooms(new ArrayList<>()).build();

        Reservation reservation = Reservation.builder().reservationIdentifier("RSV-123").build();

        Room room = Room.builder().id(1L).reservations(List.of(reservation)).build();

        h1.addRoom(room);

        when(hotelRepository.findBySlug(any())).thenReturn(Optional.of(h1));

        hotelService.removeHotel("h1");

        verify(roomService, times(1)).deleteRoom(1L);
        verify(reservationService, times(1)).cancelReservation("RSV-123");
        verify(reservationChangeRequestService, times(1)).deleteChangeRequestsFromRoom(1L);
    }

    @Test
    void removeHotelNotFoundTest() {
        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.removeHotel("h1"));

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void findHotelBySlugSuccessTest() {
        Hotel hotel = new Hotel(1L, "H1", "Hotel1 desc", "España", "Madrid", "C/ Example 4, " +
                "Madrid", 3, "h1", 3.6, new ArrayList<>(), false);


        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.of(hotel));

        HotelDTO result = hotelService.getHotelBySlug("h1");

        assertEquals(hotel.getName(), result.getName());
        assertEquals(hotel.getSlug(), result.getSlug());
        assertEquals(hotel.getDescription(), result.getDescription());
        assertEquals(hotel.getCountry(), result.getCountry());
        assertEquals(hotel.getCity(), result.getCity());
        assertEquals(hotel.getAddress(), result.getAddress());
        assertEquals(hotel.getStars(), result.getStars());
        assertEquals(hotel.getRating(), result.getRating());
        assertEquals(hotel.getId(), result.getId());

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void findHotelBySlugNotFoundTest() {
        when(hotelRepository.findBySlug("h1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.getHotelBySlug("h1"));

        verify(hotelRepository, times(1)).findBySlug("h1");
    }

    @Test
    void updateRatingTest() {

        Hotel hotel = Hotel.builder().slug("hotel").rooms(new ArrayList<>()).build();

        Room room = Room.builder().id(1L).build();

        hotel.addRoom(room);

        when(roomService.updateRating(4.0, 1L)).thenReturn(room);
        when(hotelRepository.findBySlug("hotel")).thenReturn(Optional.of(hotel));

        hotelService.updateRating(4.5, 4.0, 1L);

        verify(hotelRepository, times(1)).save(hotel);
    }
}
