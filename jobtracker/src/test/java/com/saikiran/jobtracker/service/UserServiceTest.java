package com.saikiran.jobtracker.service;

import com.saikiran.jobtracker.model.User;
import com.saikiran.jobtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User newUser;

    @BeforeEach
    void setUp() {
        newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("plainPassword123");
        newUser.setName("Sai Kiran");
    }

    @Test
    void registerUser_savesSuccessfully_whenEmailNotTaken() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.registerUser(newUser);

        assertNotNull(result);
        assertEquals("hashedPassword", result.getPassword());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void registerUser_throwsException_whenEmailAlreadyExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(newUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(newUser)
        );

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}