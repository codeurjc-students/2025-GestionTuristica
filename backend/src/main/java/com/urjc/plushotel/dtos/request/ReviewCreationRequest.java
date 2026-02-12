package com.urjc.plushotel.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreationRequest {

    private String userEmail;
    private String message;
    private String reservationIdentifier;
    private Double rating;
}
