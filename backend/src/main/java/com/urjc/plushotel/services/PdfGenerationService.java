package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Reservation;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    private final Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 20);
    private final Font labelFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
    private final Font valueFont = FontFactory.getFont(FontFactory.TIMES, 12);
    private final Font subtitleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 16);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void generateReservationPdf(Reservation reservation, OutputStream outputStream) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();
        addLogoAndDate(document);
        addTitle(document);
        addReservationInfo(reservation, document);
        addSubtitle(document, "Usuario");
        addUserInfo(reservation, document);
        addSubtitle(document, "Información de la estancia");
        addRoomInfo(reservation, document);
        addSubtitle(document, "Importe");
        addCostInfo(reservation, document);

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

    private void addLogoAndDate(Document document) throws IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        Image logo;
        try (InputStream inputStream = PdfGenerationService.class.getClassLoader().getResourceAsStream("logo.jpg")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Logo no encontrado");
            }
            logo = Image.getInstance(inputStream.readAllBytes());
        }
        logo.scaleToFit(200, 100);

        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph date = createParagraph();
        date.add(addField("Fecha",
                dateTimeFormatter.format(LocalDateTime.now())));
        PdfPCell dateCell = new PdfPCell(date);
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(logoCell);
        table.addCell(dateCell);
        document.add(table);
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
        roomInfo.add(addField("Dirección", reservation.getRoom().getHotel().getAddress()));
        roomInfo.add(addField("Habitación", reservation.getRoom().getName()));
        roomInfo.add(addField("Fecha entrada", dateFormatter.format(reservation.getStartDate())));
        roomInfo.add(addField("Fecha salida", dateFormatter.format(reservation.getEndDate())));

        document.add(roomInfo);
    }

    private void addCostInfo(Reservation reservation, Document document) {
        Paragraph costInfo = createParagraph();

        costInfo.add(addField("TOTAL", reservation.getPrice().toString() + " €"));

        document.add(costInfo);
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

    private void addSubtitle(Document document, String name) {
        Paragraph subtitle = new Paragraph(name, subtitleFont);
        subtitle.setSpacingBefore(10);
        subtitle.setSpacingAfter(10);
        document.add(subtitle);
        LineSeparator subtitleSeparator = new LineSeparator();
        subtitleSeparator.setLineWidth(0.5f);
        subtitleSeparator.setPercentage(80);
        subtitleSeparator.setLineColor(new Color(150, 150, 150));
        subtitleSeparator.setAlignment(Element.ALIGN_LEFT);
        document.add(subtitleSeparator);
    }
}
