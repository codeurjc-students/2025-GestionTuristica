package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.dtos.request.RequestFilter;
import com.urjc.plushotel.dtos.response.ModificationRequestDTO;
import com.urjc.plushotel.services.ReservationChangeRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReservationChangeRequestController {

    private final ReservationChangeRequestService reservationChangeRequestService;

    public ReservationChangeRequestController(ReservationChangeRequestService reservationChangeRequestService) {
        this.reservationChangeRequestService = reservationChangeRequestService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/requests")
    public ResponseEntity<List<ModificationRequestDTO>> findReservationChangeRequest(@RequestParam(name = "status",
            defaultValue = "PENDING") RequestFilter status) {
        return ResponseEntity.ok(reservationChangeRequestService.findReservationChangeRequests(status));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/requests")
    public ResponseEntity<Void> createRequest(@RequestBody ModificationRequest request) {
        reservationChangeRequestService.createRequest(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/requests/approve/{requestId}")
    public ResponseEntity<Void> approveRequest(@PathVariable Long requestId) {
        reservationChangeRequestService.approveRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/requests/reject/{requestId}")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long requestId) {
        reservationChangeRequestService.rejectRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
