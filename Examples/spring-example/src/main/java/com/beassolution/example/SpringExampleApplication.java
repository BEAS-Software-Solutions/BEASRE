package com.beassolution.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Example project demonstrating integration with BeasRuleEngineClientSDK.
 * <p>
 * This application shows how to configure, inject, and use the SDK in a real Spring Boot environment.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@SpringBootApplication
public class SpringExampleApplication {

    /**
     * Starts the Spring Boot application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringExampleApplication.class, args);
    }
}
