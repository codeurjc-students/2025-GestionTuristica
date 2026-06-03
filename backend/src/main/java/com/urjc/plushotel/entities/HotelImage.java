package com.urjc.plushotel.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private ImageType type;
    @ManyToOne
    private Hotel hotel;
    @ManyToOne
    private Room room;
    private int position;

    public HotelImage(String fileName, Hotel hotel, int position) {
        this.fileName = fileName;
        this.hotel = hotel;
        this.type = ImageType.HOTEL;
        this.position = position;
    }

    public HotelImage(String fileName, Hotel hotel, Room room, int position) {
        this.fileName = fileName;
        this.hotel = hotel;
        this.room = room;
        this.type = ImageType.ROOM;
        this.position = position;
    }
}
