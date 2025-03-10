package com.publicis.orchestration_backend.service;

import com.publicis.orchestration_backend.dto.UserResponseDto;
import com.publicis.orchestration_backend.entity.User;
import com.publicis.orchestration_backend.exception.UserNotFoundException;
import com.publicis.orchestration_backend.repo.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final WebClient webClient;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository, WebClient.Builder webClientBuilder,
                       @Value("${external.api.url}") String externalApiUrl) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.baseUrl(externalApiUrl).build();
    }

    @Transactional
    @CircuitBreaker(name = "externalApi", fallbackMethod = "fallbackLoadUsers")
    @Retry(name = "externalApi")
    @Bulkhead(name = "externalApi_bulkhead", type = Bulkhead.Type.SEMAPHORE)
    public void loadUsersFromExternalAPI() {
        log.info("Fetching users from external API...");

        UserResponseDto response = webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .retry(3)
                .block();

        if (response != null && response.getUsers() != null) {
            userRepository.saveAll(response.getUsers());
            log.info("Successfully loaded {} users.", response.getUsers().size());
        } else {
            log.warn("No users found in API response.");
        }
    }

    public void fallbackForLoadUsers(Exception e) {
        log.error("Fallback: External API failed. Using default behavior. Error: {}", e.getMessage());
    }

    public List<User> searchUsers(String query) {
        log.info("Searching users for query: {}", query);
        return userRepository.searchUsers(query);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }
}
