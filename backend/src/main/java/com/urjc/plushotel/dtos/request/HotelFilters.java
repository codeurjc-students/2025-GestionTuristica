package com.urjc.plushotel.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotelFilters {

    private String name;
    private String country;
    private String city;
    private Double stars;
    private Double rating;
}
