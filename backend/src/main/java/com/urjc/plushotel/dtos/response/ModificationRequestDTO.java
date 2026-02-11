package com.urjc.plushotel.dtos.response;

import com.urjc.plushotel.entities.RequestStatus;
import com.urjc.plushotel.entities.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ModificationRequestDTO {

    private Long id;
    private RequestStatus status;
    private String reservationIdentifier;
    private LocalDate requestedStartDate;
    private LocalDate requestedEndDate;
    private RequestType type;
    private String userEmail;
}
