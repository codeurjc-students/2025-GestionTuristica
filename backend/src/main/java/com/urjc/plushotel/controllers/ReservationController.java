package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.ReservationRequest;
import com.urjc.plushotel.dtos.response.ReservationDTO;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.services.ReservationService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(EndpointConstants.ReservationsEndpoints.RESERVATIONS_BASE_URL)
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping(EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL)
    public ResponseEntity<ReservationDTO> getReservationByIdentifier(@PathVariable String reservationIdentifier) {
        ReservationDTO reservation = reservationService.getReservationByIdentifier(reservationIdentifier);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping(EndpointConstants.ReservationsEndpoints.RESERVED_DATES_BY_ROOM_ID)
    public ResponseEntity<List<ReservedDatesDTO>> getReservedDatesByRoomId(@PathVariable Long roomId) {
        List<ReservedDatesDTO> reservedDates = reservationService.getReservedDatesByRoomId(roomId);
        return ResponseEntity.ok(reservedDates);
    }

    @PostMapping(EndpointConstants.ReservationsEndpoints.RESERVATIONS_CREATE_URL)
    public ResponseEntity<ReservationDTO> reserveRoom(@PathVariable Long roomId,
                                                      @RequestBody Reservation reservation) {
        ReservationDTO createdReservation = reservationService.reserveRoom(roomId, reservation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL)
                .buildAndExpand(createdReservation.getReservationIdentifier())
                .toUri();

        return ResponseEntity.created(location).body(createdReservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL)
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable String reservationIdentifier,
                                                            @RequestBody ReservationRequest request) {
        ReservationDTO updatedReservation = reservationService.updateReservation(reservationIdentifier, request);
        return ResponseEntity.ok(updatedReservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(EndpointConstants.ReservationsEndpoints.RESERVATIONS_IDENTIFIER_URL)
    public ResponseEntity<Void> cancelReservation(@PathVariable String reservationIdentifier) {
        reservationService.cancelReservation(reservationIdentifier);
        return ResponseEntity.noContent().build();
    }
}
