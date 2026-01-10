package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.RegisterRequest;
import com.urjc.plushotel.dtos.response.UserDTO;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.EmailAlreadyRegisteredException;
import com.urjc.plushotel.repositories.UserRepository;
import com.urjc.plushotel.utils.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(RegisterRequest registerRequest) {
        boolean registeredEmail = userRepository.existsByEmail(registerRequest.getEmail());
        if (registeredEmail) {
            throw new EmailAlreadyRegisteredException("This email is already registered");
        }

        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER).build();

        User user = userRepository.save(newUser);

        return new UserDTO(user.getId(), user.getEmail(), user.getRole());
    }
}