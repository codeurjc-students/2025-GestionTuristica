package com.urjc.plushotel.config;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.repositories.HotelRepository;
import com.urjc.plushotel.repositories.UserRepository;
import com.urjc.plushotel.utils.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${demo.users.admin-password}")
    private String adminPassword;

    @Value("${demo.users.user1-password}")
    private String user1Password;

    @Value("${demo.users.user2-password}")
    private String user2Password;

    public DatabaseInitializer(HotelRepository hotelRepository, UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
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
                    "Alcossebre, Castelló").stars(4).slug("gran-hotel-las-fuentes").build();

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
        }
    }
}
