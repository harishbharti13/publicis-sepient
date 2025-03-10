package com.publicis.orchestration_backend;

import com.publicis.orchestration_backend.config.WebClientTestConfig;
import com.publicis.orchestration_backend.exception.UserNotFoundException;
import com.publicis.orchestration_backend.repo.UserRepository;
import com.publicis.orchestration_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = WebClientTestConfig.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private UserService userService;

    @Value("${external.api.url}")
    private String externalApiUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        userService = new UserService(userRepository, webClientBuilder, externalApiUrl);
    }

    @Test
    void findByEmail_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail("test@publicis.com");
        });

        assertEquals("User with email test@publicis.com not found", exception.getMessage());

    }
}
