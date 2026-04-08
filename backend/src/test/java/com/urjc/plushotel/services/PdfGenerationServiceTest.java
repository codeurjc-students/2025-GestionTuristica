package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PdfGenerationServiceTest {

    @InjectMocks
    PdfGenerationService pdfGenerationService;

    @Test
    void generatePdfTest() throws IOException {

        Hotel hotel = new Hotel();

        Room room = Room.builder().name("Room").build();

        hotel.addRoom(room);

        User user = User.builder().email("john@example.com").name("John").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, user, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        pdfGenerationService.generateReservationPdf(reservation, outputStream);

        byte[] resultArray = outputStream.toByteArray();
        
        assertNotNull(resultArray);
        assertTrue(resultArray.length > 0);
    }
}
