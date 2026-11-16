package com.urjc.plushotel.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

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
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(unique = true, nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer stars;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column
    private Double rating;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Room> rooms = new ArrayList<>();
    private boolean deleted;

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public Hotel(String name, String description, String country, String city, String address, Integer stars,
                 String slug, List<Room> rooms) {
        this.name = name;
        this.description = description;
        this.country = country;
        this.city = city;
        this.address = address;
        this.stars = stars;
        this.slug = slug;
        this.rooms = rooms;
    }


}
