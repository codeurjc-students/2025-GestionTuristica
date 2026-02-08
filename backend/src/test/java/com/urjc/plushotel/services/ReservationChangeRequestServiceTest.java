package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ModificationRequest;
import com.urjc.plushotel.dtos.request.RequestFilter;
import com.urjc.plushotel.dtos.response.ModificationRequestDTO;
import com.urjc.plushotel.entities.*;
import com.urjc.plushotel.exceptions.NullModificationDatesException;
import com.urjc.plushotel.exceptions.ReservationChangeRequestNotFoundException;
import com.urjc.plushotel.repositories.ReservationChangeRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationChangeRequestServiceTest {

    @InjectMocks
    private ReservationChangeRequestService reservationChangeRequestService;

    @Mock
    private ReservationChangeRequestRepository reservationChangeRequestRepository;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private ReservationService reservationService;

    @Test
    void findReservationChangeRequestsPendingSuccess() {

        User user = new User();

        Reservation r1 = new Reservation();

        Reservation r2 = new Reservation();

        ReservationChangeRequest request1 =
                ReservationChangeRequest.builder().id(1L).reservation(r1).user(user)
                        .type(RequestType.CANCELLATION).status(RequestStatus.PENDING).build();

        ReservationChangeRequest request2 =
                ReservationChangeRequest.builder().id(2L).reservation(r2).user(user)
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.PENDING).build();

        List<ReservationChangeRequest> requests = List.of(request1, request2);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.PENDING)).thenReturn(requests);

        List<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.PENDING);

        assertEquals(requests.getFirst().getId(), result.getFirst().getId());
        assertEquals(requests.getLast().getId(), result.getLast().getId());
        assertEquals(requests.getFirst().getType(), result.getFirst().getType());
        assertEquals(requests.getLast().getType(), result.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), result.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), result.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING);
    }

    @Test
    void findReservationChangeRequestsResolvedSuccess() {

        User user = new User();

        Reservation r1 = new Reservation();

        Reservation r2 = new Reservation();

        ReservationChangeRequest request1 =
                ReservationChangeRequest.builder().id(1L).reservation(r1).user(user)
                        .type(RequestType.CANCELLATION).status(RequestStatus.APPROVED).build();

        ReservationChangeRequest request2 =
                ReservationChangeRequest.builder().id(2L).reservation(r2).user(user)
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.REJECTED).build();

        List<ReservationChangeRequest> requests = List.of(request1, request2);

        when(reservationChangeRequestRepository.findByStatusIn(List.of(RequestStatus.APPROVED, RequestStatus.REJECTED)))
                .thenReturn(requests);

        List<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.RESOLVED);

        assertEquals(requests.getFirst().getId(), result.getFirst().getId());
        assertEquals(requests.getLast().getId(), result.getLast().getId());
        assertEquals(requests.getFirst().getType(), result.getFirst().getType());
        assertEquals(requests.getLast().getType(), result.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), result.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), result.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatusIn(
                List.of(RequestStatus.APPROVED, RequestStatus.REJECTED));
    }

    @Test
    void findReservationChangeRequestsApprovedSuccess() {

        User user = new User();

        Reservation r1 = new Reservation();

        Reservation r2 = new Reservation();

        ReservationChangeRequest request1 =
                ReservationChangeRequest.builder().id(1L).reservation(r1).user(user)
                        .type(RequestType.CANCELLATION).status(RequestStatus.APPROVED).build();

        ReservationChangeRequest request2 =
                ReservationChangeRequest.builder().id(2L).reservation(r2).user(user)
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.APPROVED).build();

        List<ReservationChangeRequest> requests = List.of(request1, request2);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.APPROVED)).thenReturn(requests);

        List<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.APPROVED);

        assertEquals(requests.getFirst().getId(), result.getFirst().getId());
        assertEquals(requests.getLast().getId(), result.getLast().getId());
        assertEquals(requests.getFirst().getType(), result.getFirst().getType());
        assertEquals(requests.getLast().getType(), result.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), result.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), result.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.APPROVED);
    }

    @Test
    void findReservationChangeRequestsRejectedSuccess() {

        User user = new User();

        Reservation r1 = new Reservation();

        Reservation r2 = new Reservation();

        ReservationChangeRequest request1 =
                ReservationChangeRequest.builder().id(1L).reservation(r1).user(user)
                        .type(RequestType.CANCELLATION).status(RequestStatus.REJECTED).build();

        ReservationChangeRequest request2 =
                ReservationChangeRequest.builder().id(2L).reservation(r2).user(user)
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.REJECTED).build();

        List<ReservationChangeRequest> requests = List.of(request1, request2);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.REJECTED)).thenReturn(requests);

        List<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.REJECTED);

        assertEquals(requests.getFirst().getId(), result.getFirst().getId());
        assertEquals(requests.getLast().getId(), result.getLast().getId());
        assertEquals(requests.getFirst().getType(), result.getFirst().getType());
        assertEquals(requests.getLast().getType(), result.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), result.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), result.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.REJECTED);
    }

    @Test
    void findReservationChangeRequestsAllSuccess() {

        User user = new User();

        Reservation r1 = new Reservation();

        Reservation r2 = new Reservation();

        ReservationChangeRequest request1 =
                ReservationChangeRequest.builder().id(1L).reservation(r1).user(user)
                        .type(RequestType.CANCELLATION).status(RequestStatus.PENDING).build();

        ReservationChangeRequest request2 =
                ReservationChangeRequest.builder().id(2L).reservation(r2).user(user)
                        .type(RequestType.MODIFICATION).requestedStartDate(LocalDate.now())
                        .requestedEndDate(LocalDate.now().plusDays(10)).status(RequestStatus.REJECTED).build();

        List<ReservationChangeRequest> requests = List.of(request1, request2);

        when(reservationChangeRequestRepository.findAll()).thenReturn(requests);

        List<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.ALL);

        assertEquals(requests.getFirst().getId(), result.getFirst().getId());
        assertEquals(requests.getLast().getId(), result.getLast().getId());
        assertEquals(requests.getFirst().getType(), result.getFirst().getType());
        assertEquals(requests.getLast().getType(), result.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), result.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), result.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findAll();
    }

    @Test
    void createRequestCancellationTest() {

        User user = new User();

        Reservation reservation = Reservation.builder().id(1L).build();

        ModificationRequest modificationRequest = ModificationRequest.builder().type(RequestType.CANCELLATION)
                .userEmail("john@test.com").reservationIdentifier("RSV-123").build();

        when(userDetailsService.loadUserByUsername("john@test.com")).thenReturn(user);
        when(reservationService.getReservationEntityByIdentifier("RSV-123")).thenReturn(reservation);

        reservationChangeRequestService.createRequest(modificationRequest);

        verify(reservationService, times(1)).updateRequestedModificationState(1L,
                ReservationStatus.CANCELLATION_REQUESTED);
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void createRequestModificationTest() {

        User user = new User();

        Reservation reservation = Reservation.builder().id(1L).build();

        ModificationRequest modificationRequest = ModificationRequest.builder().type(RequestType.MODIFICATION)
                .requestedStartDate(LocalDate.now()).requestedEndDate(LocalDate.now().plusDays(13))
                .userEmail("john@test.com").reservationIdentifier("RSV-123").build();

        when(userDetailsService.loadUserByUsername("john@test.com")).thenReturn(user);
        when(reservationService.getReservationEntityByIdentifier("RSV-123")).thenReturn(reservation);

        reservationChangeRequestService.createRequest(modificationRequest);

        verify(reservationService, times(1)).updateRequestedModificationState(1L,
                ReservationStatus.MODIFICATION_REQUESTED);
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void createRequestModificationMissingDatesTest() {

        ModificationRequest modificationRequest = ModificationRequest.builder().type(RequestType.MODIFICATION)
                .userEmail("john@test.com").reservationIdentifier("RSV-123").build();

        assertThrows(NullModificationDatesException.class,
                () -> reservationChangeRequestService.createRequest(modificationRequest)
        );
    }

    @Test
    void approveRequestCancellationSuccess() {

        Reservation reservation = Reservation.builder().id(1L).reservationIdentifier("RSV-123").build();

        ReservationChangeRequest reservationChangeRequest =
                ReservationChangeRequest.builder().id(1L).reservation(reservation).type(RequestType.CANCELLATION).build();

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.of(reservationChangeRequest));

        reservationChangeRequestService.approveRequest(1L);

        verify(reservationService, times(1)).cancelReservation("RSV-123");
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void approveRequestModificationSuccess() {

        Reservation reservation = Reservation.builder().id(1L).reservationIdentifier("RSV-123").build();

        ReservationChangeRequest reservationChangeRequest =
                ReservationChangeRequest.builder().id(1L).reservation(reservation).type(RequestType.MODIFICATION)
                        .requestedStartDate(LocalDate.now()).requestedEndDate(LocalDate.now().plusDays(10)).build();

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.of(reservationChangeRequest));

        reservationChangeRequestService.approveRequest(1L);

        verify(reservationService, times(1)).updateRequestedModificationState(1L, ReservationStatus.ACTIVE);
        verify(reservationService, times(1)).updateReservation(any(), any());
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void approveRequestModificationNotFound() {

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationChangeRequestNotFoundException.class,
                () -> reservationChangeRequestService.approveRequest(1L)
        );
    }

    @Test
    void rejectRequestSuccess() {

        Reservation reservation = Reservation.builder().id(1L).reservationIdentifier("RSV-123").build();

        ReservationChangeRequest reservationChangeRequest =
                ReservationChangeRequest.builder().id(1L).reservation(reservation).type(RequestType.CANCELLATION).build();

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.of(reservationChangeRequest));

        reservationChangeRequestService.rejectRequest(1L);

        verify(reservationService, times(1)).updateRequestedModificationState(1L, ReservationStatus.ACTIVE);
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void rejectRequestModificationNotFound() {

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationChangeRequestNotFoundException.class,
                () -> reservationChangeRequestService.rejectRequest(1L)
        );
    }
}
