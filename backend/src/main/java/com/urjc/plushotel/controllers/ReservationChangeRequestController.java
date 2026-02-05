package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.services.ReservationChangeRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ReservationChangeRequestController {

    private final ReservationChangeRequestService reservationChangeRequestService;

    public ReservationChangeRequestController(ReservationChangeRequestService reservationChangeRequestService) {
        this.reservationChangeRequestService = reservationChangeRequestService;
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> createRequest(@RequestBody ModificationRequest request) {
        reservationChangeRequestService.createRequest(request);
        return ResponseEntity.ok().build();
    }
}
