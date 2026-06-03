package com.urjc.plushotel;

import com.urjc.plushotel.repositories.*;
import com.urjc.plushotel.services.AuthService;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class PlushotelApplicationTests {

    @MockitoBean
    private HotelRepository hotelRepository;

    @MockitoBean
    private RoomRepository roomRepository;

    @MockitoBean
    private ReservationRepository reservationRepository;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private ReservationChangeRequestRepository reservationChangeRequestRepository;

    @MockitoBean
    private ReviewRepository reviewRepository;

    @MockitoBean
    private ImageRepository imageRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

}
