package com.urjc.plushotel.config;

import com.urjc.plushotel.entities.Hotel;
import com.urjc.plushotel.repositories.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    public DatabaseInitializer(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... args) {
        if (hotelRepository.count() == 0) {
            hotelRepository.save(new Hotel("Hotel Córdoba Center", "El Hotel Cordoba Center está situado en el bloque" +
                    " de oro de Córdoba, junto a la estación del AVE. Tiene una piscina de temporada en la 7.ª " +
                    "planta, con jacuzzi y vistas a la ciudad.", "España", "Córdoba", "Av. de la Libertad, 4, " +
                    "Noroeste, 14006 Córdoba", 4));

            hotelRepository.save(new Hotel("Spirit Hotel Ciudad de Burgos", "En el Spirit Hotel Ciudad de Burgos " +
                    "disfrutarás de unos días relajados y sin preocupaciones. Todo lo que necesites al alcance de tu " +
                    "mano. Tu bienestar es nuestra principal razón de ser.", "España", "Burgos", "N-1, km 249, 09199 " +
                    "Rubena, Burgos", 3));

            hotelRepository.save(new Hotel("Gran Hotel Las Fuentes", "En frente del mar de Alcocéber se encuentra el " +
                    "hotel Gran Hotel Las Fuentes ****, un prestigioso hotel con una privilegiada ubicación a pie de " +
                    "playa y del paseo marítimo.", "España", "Alcocéber", "Avinguda de les Fonts, 26, 12579 " +
                    "Alcossebre, Castelló", 4));
        }
    }
}
