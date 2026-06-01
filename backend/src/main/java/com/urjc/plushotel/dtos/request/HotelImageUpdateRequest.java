package com.urjc.plushotel.dtos.request;

import com.urjc.plushotel.entities.ImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotelImageUpdateRequest {

    private Long roomId;
    private ImageType type;
    private int position;
}
