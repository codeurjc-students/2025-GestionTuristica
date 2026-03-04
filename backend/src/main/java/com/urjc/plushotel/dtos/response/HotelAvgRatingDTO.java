package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelAvgRatingDTO {

    private Long id;
    private String name;
    private String description;
    private String country;
    private String city;
    private String address;
    private Integer stars;
    private String slug;
    private Double averageRating;
}