package com.urjc.plushotel;

import com.urjc.plushotel.repositories.HotelRepository;
import com.urjc.plushotel.repositories.ReservationChangeRequestRepository;
import com.urjc.plushotel.repositories.ReservationRepository;
import com.urjc.plushotel.repositories.RoomRepository;
import com.urjc.plushotel.services.AuthService;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
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

    @Test
    void contextLoads() {
    }

}
