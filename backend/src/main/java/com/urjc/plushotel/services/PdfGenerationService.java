package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Reservation;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    private final Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 20);
    private final Font labelFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
    private final Font valueFont = FontFactory.getFont(FontFactory.TIMES, 12);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void generateReservationPdf(Reservation reservation, OutputStream outputStream) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();
        addLogo(document);
        addTitle(document);
        addReservationInfo(reservation, document);
        addUserInfo(reservation, document);
        addRoomInfo(reservation, document);

        document.close();
    }

    private void addTitle(Document document) {
        Paragraph title = new Paragraph("Resumen de reserva", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        LineSeparator separator = new LineSeparator();
        document.add(separator);
    }

    private static void addLogo(Document document) throws IOException {
        InputStream inputStream = PdfGenerationService.class.getClassLoader().getResourceAsStream("logo.jpg");
        Image logo = Image.getInstance(inputStream.readAllBytes());
        logo.scaleToFit(200, 100);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);
    }

    private void addReservationInfo(Reservation reservation, Document document) {
        Paragraph reservationInfo = createParagraph();

        reservationInfo.add(addField("Identificador de reserva", reservation.getReservationIdentifier()));
        reservationInfo.add(addField("Fecha de creación de la reserva",
                dateTimeFormatter.format(reservation.getCreatedAt())));

        document.add(reservationInfo);
    }

    private void addUserInfo(Reservation reservation, Document document) {
        Paragraph userInfo = createParagraph();

        userInfo.add(addField("Nombre", reservation.getUser().getName()));
        userInfo.add(addField("Email", reservation.getUser().getEmail()));

        document.add(userInfo);
    }

    private void addRoomInfo(Reservation reservation, Document document) {
        Paragraph roomInfo = createParagraph();

        roomInfo.add(addField("Hotel", reservation.getRoom().getHotel().getName()));
        roomInfo.add(addField("Habitación", reservation.getRoom().getName()));
        roomInfo.add(addField("Fecha entrada", dateFormatter.format(reservation.getStartDate())));
        roomInfo.add(addField("Fecha salida", dateFormatter.format(reservation.getEndDate())));
        roomInfo.add(addField("Precio", reservation.getPrice().toString() + " €"));

        document.add(roomInfo);
    }

    private Paragraph createParagraph() {
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(10);
        paragraph.setSpacingAfter(10);
        return paragraph;
    }


    private Phrase addField(String label, String value) {

        Phrase phrase = new Phrase();

        phrase.add(new Chunk(label + ": ", labelFont));
        phrase.add(new Chunk(value + "\n", valueFont));

        return phrase;
    }
}
