package com.saikiran.jobtracker.service;

import com.saikiran.jobtracker.model.Application;
import com.saikiran.jobtracker.model.ApplicationStatus;
import com.saikiran.jobtracker.model.User;
import com.saikiran.jobtracker.repository.ApplicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ApplicationService applicationService;

    private User currentUser;
    private Application application;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setEmail("test@example.com");

        application = new Application();
        application.setId(10L);
        application.setCompanyName("Google");
        application.setStatus(ApplicationStatus.APPLIED);
        application.setUser(currentUser);

        // Fake a logged-in user, the same way JwtAuthFilter does at runtime
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("test@example.com", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        when(userService.getUserByEmail("test@example.com")).thenReturn(currentUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getApplicationById_returnsApplication_whenOwnedByCurrentUser() {
        when(applicationRepository.findByIdAndUser(10L, currentUser)).thenReturn(Optional.of(application));

        Application result = applicationService.getApplicationById(10L);

        assertEquals("Google", result.getCompanyName());
    }

    @Test
    void getApplicationById_throwsException_whenNotFoundOrNotOwned() {
        when(applicationRepository.findByIdAndUser(99L, currentUser)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> applicationService.getApplicationById(99L));
    }

    @Test
    void createApplication_setsCurrentUserAsOwner() {
        Application newApp = new Application();
        newApp.setCompanyName("Meta");

        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Application result = applicationService.createApplication(newApp);

        assertEquals(currentUser, result.getUser());
        verify(applicationRepository, times(1)).save(newApp);
    }
}