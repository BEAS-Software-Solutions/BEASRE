package com.beassolution.example.conf;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to enable component scanning for BeasRuleEngineClientSDK beans.
 * <p>
 * This ensures that all SDK beans (services, properties, etc.) are discovered and injected by Spring.
 * Also provides a singleton OkHttpClient bean for HTTP operations.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Configuration
@ComponentScan(basePackages = {"com.beassolution.ruleengine"})
public class BeasRuleEngineSDKScanConfig {
    /**
     * Provides a singleton OkHttpClient bean for HTTP operations.
     * @return OkHttpClient instance
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
} 