package com.example.lab10.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    // Configuration for proper error handling
    // Spring Boot will automatically throw NoHandlerFoundException for 404s
    // when configured in application.properties
}
