package com.urjc.plushotel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Double rating;

    public RoomDTO(Long id, String name, String description, BigDecimal price, Double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }
}
