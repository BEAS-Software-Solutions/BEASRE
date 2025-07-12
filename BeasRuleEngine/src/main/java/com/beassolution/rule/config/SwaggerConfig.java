package com.beassolution.rule.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * 
 * <p>This class configures the Swagger UI and OpenAPI documentation for the
 * Beas Rule Engine REST API. It sets up the API documentation with proper
 * grouping and customizations.
 * 
 * <p>The configuration includes:
 * <ul>
 *   <li>Public API group configuration</li>
 *   <li>Path matching for all endpoints</li>
 *   <li>Custom schema modifications</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * Creates and configures the public API documentation group.
     * 
     * <p>This bean configures the OpenAPI documentation for all public endpoints
     * in the application. It includes customizations to remove specific schemas
     * that are not needed in the documentation.
     * 
     * @return Configured GroupedOpenApi instance for public API documentation
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getComponents().getSchemas().remove("AcroFields");
                })
                .build();
    }
}
