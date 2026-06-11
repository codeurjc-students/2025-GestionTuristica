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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    void findReservationChangeRequestsPendingSuccessTest() {

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

        Page<ReservationChangeRequest> paginatedRequests = new PageImpl<>(requests);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.PENDING, Pageable.ofSize(5).withPage(0))).thenReturn(paginatedRequests);

        Page<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.PENDING, 0);

        List<ModificationRequestDTO> resultContent = result.getContent();

        assertEquals(requests.getFirst().getId(), resultContent.getFirst().getId());
        assertEquals(requests.getLast().getId(), resultContent.getLast().getId());
        assertEquals(requests.getFirst().getType(), resultContent.getFirst().getType());
        assertEquals(requests.getLast().getType(), resultContent.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), resultContent.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), resultContent.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.PENDING,
                Pageable.ofSize(5).withPage(0));
    }

    @Test
    void findReservationChangeRequestsResolvedSuccessTest() {

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

        PageImpl<ReservationChangeRequest> paginatedRequests = new PageImpl<>(requests);

        when(reservationChangeRequestRepository.findByStatusIn(List.of(RequestStatus.APPROVED,
                RequestStatus.REJECTED), Pageable.ofSize(5).withPage(0)))
                .thenReturn(paginatedRequests);

        Page<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.RESOLVED, 0);

        List<ModificationRequestDTO> resultContent = result.getContent();

        assertEquals(requests.getFirst().getId(), resultContent.getFirst().getId());
        assertEquals(requests.getLast().getId(), resultContent.getLast().getId());
        assertEquals(requests.getFirst().getType(), resultContent.getFirst().getType());
        assertEquals(requests.getLast().getType(), resultContent.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), resultContent.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), resultContent.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatusIn(
                List.of(RequestStatus.APPROVED, RequestStatus.REJECTED), Pageable.ofSize(5).withPage(0));
    }

    @Test
    void findReservationChangeRequestsApprovedSuccessTest() {

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

        PageImpl<ReservationChangeRequest> paginatedRequests = new PageImpl<>(requests);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.APPROVED, Pageable.ofSize(5).withPage(0))).thenReturn(paginatedRequests);

        Page<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.APPROVED, 0);

        List<ModificationRequestDTO> resultContent = result.getContent();

        assertEquals(requests.getFirst().getId(), resultContent.getFirst().getId());
        assertEquals(requests.getLast().getId(), resultContent.getLast().getId());
        assertEquals(requests.getFirst().getType(), resultContent.getFirst().getType());
        assertEquals(requests.getLast().getType(), resultContent.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), resultContent.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), resultContent.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.APPROVED,
                Pageable.ofSize(5).withPage(0));
    }

    @Test
    void findReservationChangeRequestsRejectedSuccessTest() {

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

        PageImpl<ReservationChangeRequest> paginatedRequests = new PageImpl<>(requests);

        when(reservationChangeRequestRepository.findByStatus(RequestStatus.REJECTED, Pageable.ofSize(5).withPage(0))).thenReturn(paginatedRequests);

        Page<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.REJECTED, 0);

        List<ModificationRequestDTO> resultContent = result.getContent();

        assertEquals(requests.getFirst().getId(), resultContent.getFirst().getId());
        assertEquals(requests.getLast().getId(), resultContent.getLast().getId());
        assertEquals(requests.getFirst().getType(), resultContent.getFirst().getType());
        assertEquals(requests.getLast().getType(), resultContent.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), resultContent.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), resultContent.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findByStatus(RequestStatus.REJECTED,
                Pageable.ofSize(5).withPage(0));
    }

    @Test
    void findReservationChangeRequestsAllSuccessTest() {

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

        PageImpl<ReservationChangeRequest> paginatedRequests = new PageImpl<>(requests);

        when(reservationChangeRequestRepository.findAll(any(Pageable.class))).thenReturn(paginatedRequests);

        Page<ModificationRequestDTO> result =
                reservationChangeRequestService.findReservationChangeRequests(RequestFilter.ALL, 0);

        List<ModificationRequestDTO> resultContent = result.getContent();

        assertEquals(requests.getFirst().getId(), resultContent.getFirst().getId());
        assertEquals(requests.getLast().getId(), resultContent.getLast().getId());
        assertEquals(requests.getFirst().getType(), resultContent.getFirst().getType());
        assertEquals(requests.getLast().getType(), resultContent.getLast().getType());
        assertEquals(requests.getFirst().getStatus(), resultContent.getFirst().getStatus());
        assertEquals(requests.getLast().getStatus(), resultContent.getLast().getStatus());

        verify(reservationChangeRequestRepository, times(1)).findAll(any(Pageable.class));
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
    void approveRequestCancellationSuccessTest() {

        Reservation reservation = Reservation.builder().id(1L).reservationIdentifier("RSV-123").build();

        ReservationChangeRequest reservationChangeRequest =
                ReservationChangeRequest.builder().id(1L).reservation(reservation).type(RequestType.CANCELLATION).build();

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.of(reservationChangeRequest));

        reservationChangeRequestService.approveRequest(1L);

        verify(reservationService, times(1)).cancelReservation("RSV-123");
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void approveRequestModificationSuccessTest() {

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
    void approveRequestModificationNotFoundTest() {

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationChangeRequestNotFoundException.class,
                () -> reservationChangeRequestService.approveRequest(1L)
        );
    }

    @Test
    void rejectRequestSuccessTest() {

        Reservation reservation = Reservation.builder().id(1L).reservationIdentifier("RSV-123").build();

        ReservationChangeRequest reservationChangeRequest =
                ReservationChangeRequest.builder().id(1L).reservation(reservation).type(RequestType.CANCELLATION).build();

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.of(reservationChangeRequest));

        reservationChangeRequestService.rejectRequest(1L);

        verify(reservationService, times(1)).updateRequestedModificationState(1L, ReservationStatus.ACTIVE);
        verify(reservationChangeRequestRepository, times(1)).save(any());
    }

    @Test
    void rejectRequestModificationNotFoundTest() {

        when(reservationChangeRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationChangeRequestNotFoundException.class,
                () -> reservationChangeRequestService.rejectRequest(1L)
        );
    }

    @Test
    void deleteChangeRequestsFromRoomTest() {

        reservationChangeRequestService.deleteChangeRequestsFromRoom(1L);

        verify(reservationChangeRequestRepository, times(1)).deleteByRoomId(1L);
    }
}
