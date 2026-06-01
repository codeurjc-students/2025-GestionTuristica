package com.urjc.plushotel.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelImageDTO {

    private Long id;
    private String url;
    private Long hotelId;
    private Long roomId;
    private int position;

    public HotelImageDTO(Long id, String url, Long hotelId, int position) {
        this.id = id;
        this.url = url;
        this.hotelId = hotelId;
        this.position = position;
    }

    public HotelImageDTO(Long id, String url, Long hotelId, Long roomId, int position) {
        this.id = id;
        this.url = url;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.position = position;
    }
}
