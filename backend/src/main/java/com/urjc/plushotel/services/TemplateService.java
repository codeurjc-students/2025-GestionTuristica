package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.internal.ReservationEmailDTO;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateService {

    private final TemplateEngine templateEngine;

    public TemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateTemplate(String template, ReservationEmailDTO reservation) {
        Context context = new Context();
        context.setVariable("username", reservation.getUsername());
        context.setVariable("reservationIdentifier", reservation.getReservationIdentifier());
        context.setVariable("hotelName", reservation.getHotelName());

        return templateEngine.process(template, context);
    }
}
