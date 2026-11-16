package com.urjc.plushotel.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column
    private Double rating;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    private Hotel hotel;
    @OneToMany(mappedBy = "room", cascade = CascadeType.DETACH, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;
    private boolean deleted = false;

    public Room(String name, String description, BigDecimal price, Hotel hotel) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.hotel = hotel;
    }
}
