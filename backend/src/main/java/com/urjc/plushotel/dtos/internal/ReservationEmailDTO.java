package com.urjc.plushotel.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEmailDTO {

    private String email;
    private String username;
    private String reservationIdentifier;
    private String hotelName;
}
