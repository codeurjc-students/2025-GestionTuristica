package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ReservationRequest;
import com.urjc.plushotel.dtos.response.ReservationDTO;
import com.urjc.plushotel.dtos.response.ReservedDatesDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.Room;
import com.urjc.plushotel.exceptions.InvalidReservationRangeException;
import com.urjc.plushotel.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;

    private static final SecureRandom RANDOM = new SecureRandom();

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> allReservations = reservationRepository.findAll();
        return allReservations.stream().map(this::convertToDTO).toList();

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

    public ReservationDTO reserveRoom(Long roomId, Reservation reservation) {
        List<ReservedDatesDTO> reservedDates = getReservedDatesByRoomId(roomId);
        for (ReservedDatesDTO reservedRange : reservedDates) {
            if (reservation.getStartDate().isBefore(reservedRange.getStartDate()) &&
                    reservation.getEndDate().isAfter(reservedRange.getEndDate())) {
                throw new InvalidReservationRangeException("The selected range includes already reserved dates");
            }
        }
        Room room = roomService.getRoomById(roomId);
        reservation.setRoom(room);
        reservation.setReservationIdentifier(generateReservationCode());
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

        reservationRepository.delete(reservation);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getReservationIdentifier(),
                reservation.getRoom().getId(),
                reservation.getRoom().getName(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getCreatedAt()
        );
    }
}
