package com.beassolution.rule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for the Beas Rule Engine.
 * 
 * <p>This class configures web-related settings including:
 * <ul>
 *   <li>Static resource handling</li>
 *   <li>CORS configuration for web requests</li>
 *   <li>Web MVC settings</li>
 * </ul>
 * 
 * <p>The configuration enables proper handling of static resources and
 * cross-origin requests for web clients.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures static resource handlers.
     * 
     * <p>This method sets up handlers for serving static resources from
     * the classpath. It maps all requests to static resources located
     * in the classpath:/static/ directory.
     * 
     * @param registry ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /**
     * Configures CORS settings for web requests.
     * 
     * <p>This method sets up CORS configuration to allow cross-origin requests
     * from any origin with all common HTTP methods. Credentials are disabled
     * for security reasons.
     * 
     * @param registry CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
