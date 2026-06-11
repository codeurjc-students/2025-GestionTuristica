package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.dtos.request.RequestFilter;
import com.urjc.plushotel.dtos.request.ReservationRequest;
import com.urjc.plushotel.dtos.response.ModificationRequestDTO;
import com.urjc.plushotel.entities.*;
import com.urjc.plushotel.exceptions.NullModificationDatesException;
import com.urjc.plushotel.exceptions.ReservationChangeRequestNotFoundException;
import com.urjc.plushotel.repositories.ReservationChangeRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationChangeRequestService {

    private final ReservationChangeRequestRepository reservationChangeRequestRepository;
    private final CustomUserDetailsService userDetailsService;
    private final ReservationService reservationService;

    public ReservationChangeRequestService(ReservationChangeRequestRepository reservationChangeRequestRepository,
                                           CustomUserDetailsService userDetailsService,
                                           ReservationService reservationService) {
        this.reservationChangeRequestRepository = reservationChangeRequestRepository;
        this.userDetailsService = userDetailsService;
        this.reservationService = reservationService;
    }

    public Page<ModificationRequestDTO> findReservationChangeRequests(RequestFilter status, int page) {
        Page<ReservationChangeRequest> requests = null;
        switch (status) {
            case PENDING -> requests = findAllByStatus(RequestStatus.PENDING, page);
            case RESOLVED -> requests = findAllResolvedReservationChangeRequest(page);
            case APPROVED -> requests = findAllByStatus(RequestStatus.APPROVED, page);
            case REJECTED -> requests = findAllByStatus(RequestStatus.REJECTED, page);
            default -> requests = findAll(page);
        }

        return requests.map(this::convertToDTO);
    }

    private Page<ReservationChangeRequest> findAllByStatus(RequestStatus status, int page) {
        return reservationChangeRequestRepository.findByStatus(status, Pageable.ofSize(5).withPage(page));
    }

    private Page<ReservationChangeRequest> findAllResolvedReservationChangeRequest(int page) {
        return reservationChangeRequestRepository.findByStatusIn(
                List.of(RequestStatus.APPROVED, RequestStatus.REJECTED), Pageable.ofSize(5).withPage(page)
        );
    }

    private Page<ReservationChangeRequest> findAll(int page) {
        return reservationChangeRequestRepository.findAll(Pageable.ofSize(5).withPage(page));
    }

    public void createRequest(ModificationRequest modificationRequest) {
        if (modificationRequest.getType().equals(RequestType.MODIFICATION) &&
                (modificationRequest.getRequestedStartDate() == null || modificationRequest.getRequestedEndDate() == null)) {
            throw new NullModificationDatesException("Modification requests need to have both start and end dates " +
                    "defined");
        }

        User user = userDetailsService.loadUserByUsername(modificationRequest.getUserEmail());
        Reservation reservation =
                reservationService.getReservationEntityByIdentifier(modificationRequest.getReservationIdentifier());

        ReservationChangeRequest request =
                ReservationChangeRequest.builder().reservation(reservation).user(user).type(modificationRequest.getType()).build();

        if (request.getType().equals(RequestType.MODIFICATION)) {
            request.setRequestedStartDate(modificationRequest.getRequestedStartDate());
            request.setRequestedEndDate(modificationRequest.getRequestedEndDate());
            reservationService.updateRequestedModificationState(reservation.getId(),
                    ReservationStatus.MODIFICATION_REQUESTED);
        } else {
            reservationService.updateRequestedModificationState(reservation.getId(),
                    ReservationStatus.CANCELLATION_REQUESTED);
        }

        reservationChangeRequestRepository.save(request);
    }

    public void approveRequest(Long requestId) {
        ReservationChangeRequest reservationChangeRequest =
                reservationChangeRequestRepository.findById(requestId).orElseThrow(
                        () -> new ReservationChangeRequestNotFoundException("Reservation change request with such id " +
                                "doesn't exist")
                );

        String reservationIdentifier = reservationChangeRequest.getReservation().getReservationIdentifier();

        if (reservationChangeRequest.getType().equals(RequestType.MODIFICATION)) {
            ReservationRequest newDates = new ReservationRequest(
                    reservationChangeRequest.getRequestedStartDate(),
                    reservationChangeRequest.getRequestedEndDate()
            );

            reservationService.updateRequestedModificationState(reservationChangeRequest.getReservation().getId(),
                    ReservationStatus.ACTIVE);
            reservationService.updateReservation(reservationIdentifier, newDates);
        } else {
            reservationService.cancelReservation(reservationIdentifier);
        }

        reservationChangeRequest.setStatus(RequestStatus.APPROVED);
        reservationChangeRequestRepository.save(reservationChangeRequest);
    }

    public void rejectRequest(Long requestId) {
        ReservationChangeRequest reservationChangeRequest =
                reservationChangeRequestRepository.findById(requestId).orElseThrow(
                        () -> new ReservationChangeRequestNotFoundException("Reservation change request with such id " +
                                "doesn't exist")
                );

        reservationService.updateRequestedModificationState(reservationChangeRequest.getReservation().getId(),
                ReservationStatus.ACTIVE);

        reservationChangeRequest.setStatus(RequestStatus.REJECTED);
        reservationChangeRequestRepository.save(reservationChangeRequest);
    }

    public void deleteChangeRequestsFromRoom(Long roomId) {
        reservationChangeRequestRepository.deleteByRoomId(roomId);
    }

    private ModificationRequestDTO convertToDTO(ReservationChangeRequest request) {
        return new ModificationRequestDTO(
                request.getId(),
                request.getStatus(),
                request.getReservation().getReservationIdentifier(),
                request.getRequestedStartDate(),
                request.getRequestedEndDate(),
                request.getType(),
                request.getUser().getEmail()
        );
    }
}
