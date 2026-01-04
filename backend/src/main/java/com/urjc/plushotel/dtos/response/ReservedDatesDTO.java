package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservedDatesDTO {

    private LocalDate startDate;
    private LocalDate endDate;
}
