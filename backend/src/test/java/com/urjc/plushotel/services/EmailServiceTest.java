package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.internal.ReservationEmailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private TemplateService templateService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void sendReservationConfirmationTest() {

        ReservationEmailDTO emailDTO = new ReservationEmailDTO("john@test.com", "John", "RSV-123", "Hotel1");

        String emailMessage = """
                Hola John,
                
                Su reserva con identificador RSV-123 en el hotel Hotel1 ha sido confirmada. Puede consultar más detalles en la página web.
                
                Muchas gracias por confirmar en nosotros.""";

        when(templateService.generateTemplate("confirmation", emailDTO)).thenReturn(emailMessage);

        emailService.sendReservationConfirmation(emailDTO);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(emailDTO.getEmail(), message.getTo()[0]);
        assertEquals(emailMessage, message.getText());

        verify(templateService, times(1)).generateTemplate("confirmation", emailDTO);
    }

    @Test
    void sendModificationConfirmationTest() {

        ReservationEmailDTO emailDTO = new ReservationEmailDTO("john@test.com", "John", "RSV-123", "Hotel1");

        String emailMessage = """
                Hola John,
                
                Su reserva con identificador RSV-123 en el hotel Hotel1 ha sido modificada, puede revisar las nuevas fechas en la página web de Plushotel.
                
                Muchas gracias por confirmar en nosotros.""";

        when(templateService.generateTemplate("modification", emailDTO)).thenReturn(emailMessage);

        emailService.sendModificationConfirmation(emailDTO);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(emailDTO.getEmail(), message.getTo()[0]);
        assertEquals(emailMessage, message.getText());

        verify(templateService, times(1)).generateTemplate("modification", emailDTO);
    }

    @Test
    void sendCancellationConfirmationTest() {

        ReservationEmailDTO emailDTO = new ReservationEmailDTO("john@test.com", "John", "RSV-123", "Hotel1");

        String emailMessage = """
                Hola John,
                
                Su reserva con identificador RSV-123 en el hotel Hotel1 ha sido cancelada.
                
                Muchas gracias por confirmar en nosotros.""";

        when(templateService.generateTemplate("cancellation", emailDTO)).thenReturn(emailMessage);

        emailService.sendCancellationConfirmation(emailDTO);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(emailDTO.getEmail(), message.getTo()[0]);
        assertEquals(emailMessage, message.getText());

        verify(templateService, times(1)).generateTemplate("cancellation", emailDTO);
    }
}
