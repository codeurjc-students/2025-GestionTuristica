package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.LoginRequest;
import com.urjc.plushotel.dtos.request.RegisterRequest;
import com.urjc.plushotel.dtos.response.LoginTokenDTO;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.EmailAlreadyRegisteredException;
import com.urjc.plushotel.repositories.UserRepository;
import com.urjc.plushotel.utils.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Test
    void registerUserSuccessfulTest() {

        RegisterRequest registerRequest = new RegisterRequest("John", "john@email.com", "john");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        authService.registerUser(registerRequest);

        verify(passwordEncoder, times(1)).encode("john");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUserAlreadyRegisteredTest() {

        RegisterRequest registerRequest = new RegisterRequest("John", "john@email.com", "john");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> authService.registerUser(registerRequest));
    }

    @Test
    void authenticateTest() {

        LoginRequest loginRequest = new LoginRequest("john@email.com", "john");

        User user = new User(1L, "John", "john@email.com", "john", Role.USER);

        String jwtTokenValue = "ey.asdsadk12321kj.jkj31k23j";

        when(userDetailsService.loadUserByUsername("john@email.com")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(jwtTokenValue);

        LoginTokenDTO token = authService.authenticate(loginRequest);

        assertEquals(jwtTokenValue, token.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
