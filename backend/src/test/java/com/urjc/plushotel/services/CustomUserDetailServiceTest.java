package com.urjc.plushotel.services;

import com.urjc.plushotel.entities.Role;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameTest() {

        User user = User.builder().name("John").role(Role.USER).email("john@example.com").build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = userDetailsService.loadUserByUsername("john@example.com");

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());

        verify(userRepository, times(1)).findByEmail("john@example.com");
    }
}
