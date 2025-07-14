package com.beassolution.ruleengine;

import com.beassolution.ruleengine.properties.RuleEngineProperties;
import com.beassolution.ruleengine.security.KeycloakAuthService;
import com.beassolution.ruleengine.service.RuleEngineClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(RuleEngineProperties.class)
@ConditionalOnProperty(prefix = "rule.engine", name = "enabled", havingValue = "true")
public class RuleEngineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient(RuleEngineProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(java.time.Duration.ofSeconds(10))
                                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                                .compress(true)
                ))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleEngineClient ruleEngineClient(RuleEngineProperties properties, RestTemplate restTemplate, KeycloakAuthService keycloakAuthService, WebClient webClient) {
        return new RuleEngineClient(restTemplate, properties, keycloakAuthService, webClient);
    }
}
