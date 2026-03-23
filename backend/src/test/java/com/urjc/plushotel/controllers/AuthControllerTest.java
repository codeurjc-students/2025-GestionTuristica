package com.urjc.plushotel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urjc.plushotel.config.JwtAuthenticationFilter;
import com.urjc.plushotel.dtos.request.LoginRequest;
import com.urjc.plushotel.dtos.request.RegisterRequest;
import com.urjc.plushotel.dtos.response.LoginTokenDTO;
import com.urjc.plushotel.services.AuthService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerUserTest() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("John", "john@email.com", "john");

        mockMvc.perform(post("/api/v1/auth" + EndpointConstants.AuthorizationEndpoints.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void loginTest() throws Exception {

        LoginRequest registerRequest = new LoginRequest("john@email.com", "john");

        when(authService.authenticate(any())).thenReturn(new LoginTokenDTO("token"));

        mockMvc.perform(post("/api/v1/auth" + EndpointConstants.AuthorizationEndpoints.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}
