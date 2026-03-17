package com.urjc.plushotel.dtos.request;

import com.urjc.plushotel.entities.Room;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HotelRequest {

    private String name;
    private String description;
    private String country;
    private String city;
    private String address;
    private Integer stars;
    private String slug;
    private List<Room> rooms = new ArrayList<>();
}
