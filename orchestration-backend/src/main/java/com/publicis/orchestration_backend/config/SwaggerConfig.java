package com.publicis.orchestration_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orchestration API")
                        .description("APIs for Orchestration")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Harish")
                                .email("harish@publicis.com")
                                .url("https://publicissapient.com")));
    }
}