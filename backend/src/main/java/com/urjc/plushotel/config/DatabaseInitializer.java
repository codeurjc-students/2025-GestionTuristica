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

            Hotel cordobaCenter = new Hotel(
                    "Hotel Córdoba Center",
                    "El Hotel Cordoba Center " +
                            "está situado en el bloque de oro de Córdoba, junto a la estación del AVE. Tiene una " +
                            "piscina de " +
                            "temporada en la 7.ª planta, con jacuzzi y vistas a la ciudad.",
                    COUNTRY_SPAIN,
                    "Córdoba",
                    "Av. de la Libertad, 4, Noroeste, 14006 Córdoba",
                    4,
                    "hotel-cordoba-center",
                    new ArrayList<>()
            );

            Hotel spiritHotel = new Hotel(
                    "Spirit Hotel Ciudad de Burgos",
                    "En el Spirit Hotel" +
                            " Ciudad de Burgos disfrutarás de unos días relajados y sin preocupaciones. Todo lo que " +
                            "necesites" +
                            " al alcance de tu mano. Tu bienestar es nuestra principal razón de ser.",
                    COUNTRY_SPAIN,
                    "Rubena",
                    "N-1, km 249, 09199 Rubena, Burgos",
                    3,
                    "spirit-hotel-ciudad-de-burgos",
                    new ArrayList<>()
            );

            Hotel lasFuentes = new Hotel(
                    "Gran Hotel Las Fuentes",
                    "En frente del mar de " +
                            "Alcocéber se encuentra el hotel Gran Hotel Las Fuentes ****, un prestigioso hotel con " +
                            "una " +
                            "privilegiada ubicación a pie de playa y del paseo marítimo.",
                    COUNTRY_SPAIN,
                    "Alcocéber",
                    "Avinguda de les Fonts, 26, 12579 Alcossebre, Castelló",
                    4,
                    "gran-hotel-las-fuentes",
                    new ArrayList<>()
            );

            Room lasFuentesBasicRoom = new Room(
                    "Habitación básica",
                    "Habitación básica para" +
                            " una persona, incluye todas las comodidades básicas",
                    BigDecimal.valueOf(15),
                    lasFuentes
            );

            Room lasFuentesPremiumRoom = new Room(
                    "Habitación prémium",
                    "Habitación premium " +
                            "para hasta dos personas, incluye comodidades premium y acceso al spa y gimnasio",
                    BigDecimal.valueOf(20),
                    lasFuentes
            );

            lasFuentes.addRoom(lasFuentesBasicRoom);
            lasFuentes.addRoom(lasFuentesPremiumRoom);

            hotelRepository.save(cordobaCenter);

            hotelRepository.save(spiritHotel);

            hotelRepository.save(lasFuentes);

            User user1 = new User("John", "john@email.com", passwordEncoder.encode(user1Password), Role.USER);

            User user2 =
                    new User("Juan", "juan@email.com", passwordEncoder.encode(user2Password), Role.USER);

            User userAdmin =
                    new User("Admin", "admin@email.com", passwordEncoder.encode(adminPassword), Role.ADMIN);

            userRepository.save(user1);

            userRepository.save(user2);

            userRepository.save(userAdmin);

            Reservation res1 = new Reservation(
                    "RSV-12L125H7BF1E",
                    lasFuentesBasicRoom,
                    user1,
                    BigDecimal.valueOf(45),
                    true,
                    LocalDateTime.parse("2026-02-28T10:37:31"),
                    LocalDate.parse("2026-03-02"),
                    LocalDate.parse("2026-03-05")
            );

            Reservation res2 = new Reservation
                    (
                            "RSV-K176J49N2N1N2",
                            lasFuentesPremiumRoom,
                            user1,
                            BigDecimal.valueOf(140),
                            false,
                            LocalDateTime.parse("2026-01-26T11:45:37"),
                            LocalDate.parse("2026-02-21"),
                            LocalDate.parse("2026-02-28")
                    );

            reservationRepository.save(res1);
            reservationRepository.save(res2);

            Review rev1 = new Review
                    (
                            user1,
                            res1,
                            "Muy buen servicio. Recomendado",
                            4.5,
                            LocalDateTime.parse("2026-03-06T14:16:25")
                    );

            reviewRepository.save(rev1);
        }
    }
}
