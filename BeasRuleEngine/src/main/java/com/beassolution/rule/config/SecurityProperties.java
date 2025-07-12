package com.beassolution.rule.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for security settings.
 * 
 * <p>This class holds security-related configuration values that are injected
 * from the application properties file. It provides centralized access to
 * security configuration parameters.
 * 
 * <p>Current properties include:
 * <ul>
 *   <li>JWK Set URI for OAuth2 JWT validation</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Data
public class SecurityProperties {
    
    /**
     * JWK Set URI for OAuth2 JWT token validation.
     * 
     * <p>This property specifies the endpoint where JSON Web Key Set (JWKS)
     * can be retrieved for validating JWT tokens. The value is injected
     * from the application configuration.
     */
    @Value("${spring.security.oauth2.authorizationserver.endpoint.jwk-set-uri}")
    private String jwkSetUri;
}
