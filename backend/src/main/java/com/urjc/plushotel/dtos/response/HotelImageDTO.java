package com.urjc.plushotel.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelImageDTO {

    private String url;
    private Long hotelId;
    private Long roomId;
    private int position;

    public HotelImageDTO(String url, Long hotelId) {
        this.url = url;
        this.hotelId = hotelId;
    }

    public HotelImageDTO(String url, Long hotelId, Long roomId) {
        this.url = url;
        this.hotelId = hotelId;
        this.roomId = roomId;
    }
}
