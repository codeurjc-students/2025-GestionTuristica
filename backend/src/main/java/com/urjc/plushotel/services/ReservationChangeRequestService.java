package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.entities.RequestType;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.ReservationChangeRequest;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.NullModificationDatesException;
import com.urjc.plushotel.repositories.ReservationChangeRequestRepository;
import org.springframework.stereotype.Service;

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
}
