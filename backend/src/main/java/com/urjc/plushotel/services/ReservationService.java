package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ReservationRequest;
import com.urjc.plushotel.dtos.response.ReservationDTO;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.ReservationStatus;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.InvalidReservationRangeException;
import com.urjc.plushotel.repositories.ReservationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final CustomUserDetailsService userDetailsService;

    private static final SecureRandom RANDOM = new SecureRandom();

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService,
                              CustomUserDetailsService userDetailsService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.userDetailsService = userDetailsService;
    }

    public List<ReservationDTO> getReservations(ReservationStatus status) {
        List<Reservation> reservations;
        if (status == null) {
            reservations = reservationRepository.findAll();
        } else {
            reservations = reservationRepository.findByStatus(status);
        }
        return reservations.stream().map(this::convertToDTO).toList();

    }

    public List<ReservedDatesDTO> getReservedDatesByRoomId(Long roomId) {
        return reservationRepository.findReservedDatesByRoomId(roomId);
    }

    public ReservationDTO getReservationByIdentifier(String reservationIdentifier) {
        Reservation reservation = reservationRepository.findByReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new RuntimeException("Reservation doesn't exist")
        );

        return convertToDTO(reservation);
    }

    private String generateReservationCode() {
        return "RSV-" + Long.toString(RANDOM.nextLong() & Long.MAX_VALUE, 36).toUpperCase();
    }

    public ReservationDTO reserveRoom(Long roomId, ReservationRequest request, Authentication authentication) {
        List<ReservedDatesDTO> reservedDates = getReservedDatesByRoomId(roomId);
        for (ReservedDatesDTO reservedRange : reservedDates) {
            if (request.getStartDate().isBefore(reservedRange.getStartDate()) &&
                    request.getEndDate().isAfter(reservedRange.getEndDate())) {
                throw new InvalidReservationRangeException("The selected range includes already reserved dates");
            }
        }
        User user = userDetailsService.loadUserByUsername(authentication.getName());
        Room room = roomService.getRoomById(roomId);
        Reservation reservation =
                Reservation.builder().user(user).startDate(request.getStartDate()).endDate(request.getEndDate())
                        .room(room).reservationIdentifier(generateReservationCode()).build();
        return convertToDTO(reservationRepository.save(reservation));
    }

    public ReservationDTO updateReservation(String reservationIdentifier, ReservationRequest request) {
        Reservation reservationToUpdate = reservationRepository.findByReservationIdentifier(reservationIdentifier)
                .orElseThrow(
                        () -> new RuntimeException("There's no reservation with such reservation identifier")
                );
        reservationToUpdate.setStartDate(request.getStartDate());
        reservationToUpdate.setEndDate(request.getEndDate());
        reservationRepository.save(reservationToUpdate);

        return convertToDTO(reservationToUpdate);
    }

    public void cancelReservation(String reservationIdentifier) {
        Reservation reservation = reservationRepository.findByReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new RuntimeException("This reservation identifier doesn't correspond to any reservation")
        );

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<ReservationDTO> findReservationsByUser(Long userId, ReservationStatus status) {
        List<Reservation> userReservations;
        if (status == null) {
            userReservations = reservationRepository.findByUserId(userId);
        } else {
            userReservations = reservationRepository.findByUserIdAndStatus(userId, status);
        }
        return userReservations.stream().map(this::convertToDTO).toList();
    }

    public Reservation getReservationEntityByIdentifier(String reservationIdentifier) {
        return reservationRepository.findByReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new RuntimeException("This reservation doesn't exist")
        );
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getReservationIdentifier(),
                reservation.getRoom().getId(),
                reservation.getRoom().getName(),
                reservation.getUser().getEmail(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}
