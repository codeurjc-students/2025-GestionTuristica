package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {

    private Long id;
    private String name;
    private String description;
    private String country;
    private String city;
    private String address;
    private Integer stars;
    private String slug;
    private String mainImageUrl;
    private Double rating;

    public HotelDTO(Long id, String name, String description, String country, String city, String address,
                    Integer stars, String slug, Double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.country = country;
        this.city = city;
        this.address = address;
        this.stars = stars;
        this.slug = slug;
        this.rating = rating;
    }
}