package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private String userEmail;
    private String roomName;
    private String message;
    private Double rating;
    private String reservationIdentifier;
    private String creationTime;
}
