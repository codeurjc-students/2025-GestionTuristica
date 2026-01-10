package com.urjc.plushotel.dtos.response;

import com.urjc.plushotel.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private Role role;
}
