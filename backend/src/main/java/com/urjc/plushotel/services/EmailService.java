package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.internal.ReservationEmailDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String CONFIRMATION_SUBJECT = "Confirmación reserva";
    private static final String CANCELLATION_SUBJECT = "Cancelación reserva";
    private static final String MODIFICATION_SUBJECT = "Modificación reserva";
    public static final String CONFIRMATION = "confirmation";
    public static final String CANCELLATION = "cancellation";
    public static final String MODIFICATION = "modification";
    private final JavaMailSender mailSender;
    private final TemplateService templateService;

    public EmailService(JavaMailSender mailSender, TemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    private void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Async
    public void sendReservationConfirmation(ReservationEmailDTO reservation) {
        sendEmail(reservation.getEmail(), CONFIRMATION_SUBJECT,
                templateService.generateTemplate(CONFIRMATION, reservation));
    }

    @Async
    public void sendCancellationConfirmation(ReservationEmailDTO reservation) {
        sendEmail(reservation.getEmail(), CANCELLATION_SUBJECT,
                templateService.generateTemplate(CANCELLATION, reservation));
    }

    @Async
    public void sendModificationConfirmation(ReservationEmailDTO reservation) {
        sendEmail(reservation.getEmail(), MODIFICATION_SUBJECT,
                templateService.generateTemplate(MODIFICATION, reservation));
    }
}
