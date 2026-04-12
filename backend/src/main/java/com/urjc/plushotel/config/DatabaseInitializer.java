package com.urjc.plushotel.config;

import com.urjc.plushotel.entities.*;
import com.urjc.plushotel.repositories.HotelRepository;
import com.urjc.plushotel.repositories.ReservationRepository;
import com.urjc.plushotel.repositories.ReviewRepository;
import com.urjc.plushotel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@Profile("!test")
public class DatabaseInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${demo.users.admin-password}")
    private String adminPassword;

    @Value("${demo.users.user1-password}")
    private String user1Password;

    @Value("${demo.users.user2-password}")
    private String user2Password;

    public DatabaseInitializer(HotelRepository hotelRepository, UserRepository userRepository,
                               ReservationRepository reservationRepository, ReviewRepository reviewRepository,
                               PasswordEncoder passwordEncoder) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String COUNTRY_SPAIN = "España";

    @Override
    public void run(String... args) {
        if (hotelRepository.count() == 0) {

            Hotel cordobaCenter = Hotel.builder().name("Hotel Córdoba Center").description("El Hotel Cordoba Center " +
                    "está situado en el bloque de oro de Córdoba, junto a la estación del AVE. Tiene una piscina de " +
                    "temporada en la 7.ª planta, con jacuzzi y vistas a la ciudad.").country(COUNTRY_SPAIN).city(
                    "Córdoba").address("Av. de la Libertad, 4, Noroeste, 14006 Córdoba").stars(4).slug("hotel" +
                    "-cordoba-center").build();

            Hotel spiritHotel = Hotel.builder().name("Spirit Hotel Ciudad de Burgos").description("En el Spirit Hotel" +
                    " Ciudad de Burgos disfrutarás de unos días relajados y sin preocupaciones. Todo lo que necesites" +
                    " al alcance de tu mano. Tu bienestar es nuestra principal razón de ser.").country(COUNTRY_SPAIN).city(
                    "Rubena").address("N-1, km 249, 09199 Rubena, Burgos").stars(3).slug("spirit-hotel-ciudad-de" +
                    "-burgos").build();

            Hotel lasFuentes = Hotel.builder().name("Gran Hotel Las Fuentes").description("En frente del mar de " +
                    "Alcocéber se encuentra el hotel Gran Hotel Las Fuentes ****, un prestigioso hotel con una " +
                    "privilegiada ubicación a pie de playa y del paseo marítimo.").country(COUNTRY_SPAIN).city(
                    "Alcocéber").address("Avinguda de les Fonts, 26, 12579 " +
                    "Alcossebre, Castelló").stars(4).slug("gran-hotel-las-fuentes").rooms(new ArrayList<>()).build();

            Room lasFuentesBasicRoom = Room.builder().name("Habitación básica").description("Habitación básica para" +
                            " una persona, incluye todas las comodidades básicas").hotel(lasFuentes)
                    .price(BigDecimal.valueOf(15)).build();

            Room lasFuentesPremiumRoom = Room.builder().name("Habitación prémium").description("Habitación premium " +
                            "para hasta dos personas, incluye comodidades premium y acceso al spa y gimnasio")
                    .hotel(lasFuentes).price(BigDecimal.valueOf(20)).build();

            lasFuentes.addRoom(lasFuentesBasicRoom);
            lasFuentes.addRoom(lasFuentesPremiumRoom);

            hotelRepository.save(cordobaCenter);

            hotelRepository.save(spiritHotel);

            hotelRepository.save(lasFuentes);

            User user1 =
                    User.builder().name("John").email("john@email.com").password(passwordEncoder.encode(user1Password)).role(Role.USER).build();

            User user2 =
                    User.builder().name("Juan").email("juan@email.com").password(passwordEncoder.encode(user2Password)).role(Role.USER).build();

            User userAdmin =
                    User.builder().name("Admin").email("admin@email.com").password(passwordEncoder.encode(adminPassword)).role(Role.ADMIN).build();

            userRepository.save(user1);

            userRepository.save(user2);

            userRepository.save(userAdmin);

            Reservation res1 = Reservation.builder().reservationIdentifier("RSV-12L125H7BF1E").room(lasFuentesBasicRoom)
                    .startDate(LocalDate.parse("2026-03-02")).endDate(LocalDate.parse("2026-03-05"))
                    .createdAt(LocalDateTime.parse("2026-02-28T10:37:31")).reviewed(true).user(user1)
                    .status(ReservationStatus.ACTIVE).price(BigDecimal.valueOf(45)).build();

            Reservation res2 =
                    Reservation.builder().reservationIdentifier("RSV-K176J49N2N1N2").room(lasFuentesPremiumRoom)
                            .startDate(LocalDate.parse("2026-02-21")).endDate(LocalDate.parse("2026-02-28"))
                            .createdAt(LocalDateTime.parse("2026-01-26T11:45:37")).reviewed(false).user(user1)
                            .status(ReservationStatus.ACTIVE).price(BigDecimal.valueOf(140)).build();

            reservationRepository.save(res1);
            reservationRepository.save(res2);

            Review rev1 = Review.builder().user(user1).message("Muy buen servicio. Recomendado").rating(4.5)
                    .reservation(res1).createdAt(LocalDateTime.parse("2026-03-06T14:16:25")).build();

            reviewRepository.save(rev1);
        }
    }
}
