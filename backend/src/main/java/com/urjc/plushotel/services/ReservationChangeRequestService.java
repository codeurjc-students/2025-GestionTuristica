package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.dtos.request.RequestFilter;
import com.urjc.plushotel.dtos.request.ReservationRequest;
import com.urjc.plushotel.dtos.response.ModificationRequestDTO;
import com.urjc.plushotel.entities.*;
import com.urjc.plushotel.exceptions.NullModificationDatesException;
import com.urjc.plushotel.repositories.ReservationChangeRequestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ModificationRequestDTO> findReservationChangeRequests(RequestFilter status) {
        List<ReservationChangeRequest> requests = new ArrayList<>();
        switch (status) {
            case PENDING -> requests = findAllByStatus(RequestStatus.PENDING);
            case RESOLVED -> requests = findAllResolvedReservationChangeRequest();
            case APPROVED -> requests = findAllByStatus(RequestStatus.APPROVED);
            case REJECTED -> requests = findAllByStatus(RequestStatus.REJECTED);
            case ALL -> requests = findAll();
        }

        return requests.stream().map(this::convertToDTO).toList();
    }

    private List<ReservationChangeRequest> findAllByStatus(RequestStatus status) {
        return reservationChangeRequestRepository.findByStatus(status);
    }

    private List<ReservationChangeRequest> findAllResolvedReservationChangeRequest() {
        return reservationChangeRequestRepository.findByStatusIn(
                List.of(RequestStatus.APPROVED, RequestStatus.REJECTED)
        );
    }

    private List<ReservationChangeRequest> findAll() {
        return reservationChangeRequestRepository.findAll();
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
        }

        reservationChangeRequestRepository.save(request);
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
