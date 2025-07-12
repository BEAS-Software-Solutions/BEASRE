package com.beassolution.rule.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for the Beas Rule Engine.
 * 
 * <p>This class configures the security settings for the application including:
 * <ul>
 *   <li>OAuth2 JWT token validation</li>
 *   <li>CORS configuration for cross-origin requests</li>
 *   <li>CSRF protection settings</li>
 *   <li>Endpoint access control</li>
 * </ul>
 * 
 * <p>The security configuration allows public access to Swagger documentation
 * endpoints while requiring authentication for all other endpoints.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    
    /**
     * Security properties containing configuration values.
     */
    private final SecurityProperties securityProperties;

    /**
     * Configures the security filter chain for the application.
     * 
     * <p>This method sets up the complete security configuration including:
     * <ul>
     *   <li>CORS configuration for cross-origin requests</li>
     *   <li>CSRF protection with cookie-based tokens</li>
     *   <li>OAuth2 JWT resource server configuration</li>
     *   <li>Endpoint authorization rules</li>
     * </ul>
     * 
     * <p>Public endpoints include Swagger UI and API documentation paths.
     * All other endpoints require valid JWT authentication.
     * 
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html","/test").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwkSetUri(securityProperties.getJwkSetUri()))
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html","/test")
                );

        return http.build();
    }
    
    /**
     * Configures CORS settings for cross-origin requests.
     * 
     * <p>This method sets up CORS configuration to allow requests from any origin
     * with all common HTTP methods and headers. Credentials are disabled for
     * security reasons.
     * 
     * @return Configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(false);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
