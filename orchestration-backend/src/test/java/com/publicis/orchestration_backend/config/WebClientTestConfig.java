package com.publicis.orchestration_backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfig {

    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl("http://localhost:8080");
    }
}
