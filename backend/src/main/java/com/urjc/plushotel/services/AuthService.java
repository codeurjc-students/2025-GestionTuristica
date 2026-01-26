package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.LoginRequest;
import com.urjc.plushotel.dtos.request.RegisterRequest;
import com.urjc.plushotel.dtos.response.LoginTokenDTO;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.EmailAlreadyRegisteredException;
import com.urjc.plushotel.repositories.UserRepository;
import com.urjc.plushotel.utils.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       CustomUserDetailsService userDetailsService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public void registerUser(RegisterRequest registerRequest) {
        boolean registeredEmail = userRepository.existsByEmail(registerRequest.getEmail());
        if (registeredEmail) {
            throw new EmailAlreadyRegisteredException("This email is already registered");
        }

        User newUser = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER).build();

        userRepository.save(newUser);
    }

    public LoginTokenDTO authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(user);

        return new LoginTokenDTO(token);
    }
}