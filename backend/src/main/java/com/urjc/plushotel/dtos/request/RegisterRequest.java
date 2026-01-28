package com.urjc.plushotel.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
