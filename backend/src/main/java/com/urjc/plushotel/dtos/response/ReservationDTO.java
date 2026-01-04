package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;
    private String reservationIdentifier;
    private Long roomId;
    private String roomName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
}
