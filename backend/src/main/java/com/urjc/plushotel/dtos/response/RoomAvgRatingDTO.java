package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvgRatingDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Double averageRating;

    public RoomAvgRatingDTO(Long id, String name, String description, BigDecimal price, Double averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.averageRating = averageRating;
    }
}
