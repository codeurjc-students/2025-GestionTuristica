package com.urjc.plushotel.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Check(constraints = "stars >= 1 AND stars <= 5")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(unique = true, nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer stars;

    public Hotel(String name, String description, String country, String city, String address, Integer stars) {
        this.name = name;
        this.description = description;
        this.country = country;
        this.city = city;
        this.address = address;
        this.stars = stars;
    }
}
