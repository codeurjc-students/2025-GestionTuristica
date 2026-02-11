package com.urjc.plushotel.dtos.request;

import com.urjc.plushotel.entities.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ModificationRequest {

    private RequestType type;
    private LocalDate requestedStartDate;
    private LocalDate requestedEndDate;
    private String userEmail;
    private String reservationIdentifier;
}
