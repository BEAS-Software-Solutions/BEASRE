package com.beassolution.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Main Spring Boot application class for the Beas Rule Engine.
 * 
 * <p>This is the entry point for the rule engine application that provides:
 * <ul>
 *   <li>Rule evaluation and execution capabilities</li>
 *   <li>Function library management</li>
 *   <li>Rule helper management</li>
 *   <li>Rule library management</li>
 *   <li>Caching mechanisms for improved performance</li>
 *   <li>Cryptographic operations for data security</li>
 * </ul>
 * 
 * <p>The application uses MongoDB as the primary data store and supports
 * asynchronous operations for better scalability.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@EnableWebMvc
@EnableMongoRepositories
@EnableMongoAuditing
@EnableAsync
public class BeasReApplication {
    
    /**
     * Default constructor for the Beas Rule Engine application.
     */
    public BeasReApplication() {

    }

    /**
     * Main method to start the Spring Boot application.
     * 
     * <p>This method initializes the Spring application context and starts
     * the embedded web server. The application will be available on the
     * configured port (default: 8080).
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BeasReApplication.class, args);
    }

}
