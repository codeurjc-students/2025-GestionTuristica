package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("The email introduced doesn't belong to any user")
        );
    }
}
