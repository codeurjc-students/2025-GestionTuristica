package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.LoginRequest;
import com.urjc.plushotel.dtos.request.RegisterRequest;
import com.urjc.plushotel.dtos.response.LoginTokenDTO;
import com.urjc.plushotel.dtos.response.UserDTO;
import com.urjc.plushotel.services.AuthService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(EndpointConstants.AuthorizationEndpoints.REGISTER_URL)
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest request) {
        UserDTO user = authService.registerUser(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }

    @PostMapping(EndpointConstants.AuthorizationEndpoints.LOGIN_URL)
    public ResponseEntity<LoginTokenDTO> login(@RequestBody LoginRequest request) {
        LoginTokenDTO token = authService.authenticate(request);
        return ResponseEntity.ok(token);
    }
}
