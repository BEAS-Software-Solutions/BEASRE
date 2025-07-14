package com.beassolution.ruleengine.service;

import com.beassolution.ruleengine.model.DummyBody;
import com.beassolution.ruleengine.model.request.RuleEvaluateRequest;
import com.beassolution.ruleengine.model.response.RuleEvaluateResponse;
import com.beassolution.ruleengine.properties.RuleEngineProperties;
import com.beassolution.ruleengine.security.KeycloakAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * RuleEngineClient provides methods to interact with the BEAS Rule Engine backend service.
 * <p>
 * This client supports both synchronous and asynchronous rule evaluation requests.
 * It handles authentication, request construction, and error handling for integration scenarios.
 *
 * <p>Key features:
 * <ul>
 *   <li>Token management (Keycloak or client credentials)</li>
 *   <li>Synchronous and asynchronous evaluate methods</li>
 *   <li>Spring dependency injection compatibility</li>
 *   <li>Detailed error handling and logging</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>
 *   RuleEngineClient client = ...;
 *   Map<String, Object> params = ...;
 *   Map<String, Object> payload = ...;
 *   RuleEvaluateResponse resp = client.evaluate("myRule", params, payload).getBody();
 * </pre>
 *
 * @author BEAS Solution Team
 * @version 1.0
 * @since 1.0
 */
public class RuleEngineClient {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleEngineClient.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RestTemplate restTemplate;
    private final RuleEngineProperties properties;
    private final KeycloakAuthService keycloakAuthService;
    private final WebClient webClient;

    /**
     * Constructs a RuleEngineClient with required dependencies.
     *
     * @param restTemplate         Synchronous HTTP client
     * @param properties           Rule engine configuration properties
     * @param keycloakAuthService  Service for Keycloak token management
     * @param webClient            Reactive HTTP client for async operations
     */
    public RuleEngineClient(RestTemplate restTemplate, RuleEngineProperties properties, KeycloakAuthService keycloakAuthService, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.keycloakAuthService = keycloakAuthService;
        this.webClient = webClient;
    }

    /**
     * Retrieves the current Authorization header for requests.
     * <p>
     * If client token usage is disabled, fetches a new token from Keycloak.
     * Otherwise, extracts the token from the current HTTP request context.
     *
     * @return Bearer token string ("Bearer ...") or null if not found
     * @throws Exception if token retrieval fails
     */
    private String getCurrentAuthorizationHeader() throws Exception {
        if (!properties.isUseClientToken()) {
            String token = keycloakAuthService.fetchTokenWithClientCredentials();
            JsonNode root = OBJECT_MAPPER.readTree(token);
            return "Bearer "+root.get("access_token").asText();
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;

        HttpServletRequest request = attrs.getRequest();
        return request.getHeader("Authorization");
    }

    /**
     * Synchronously evaluates a rule by name with given parameters and payload.
     *
     * @param ruleName            The name of the rule to evaluate
     * @param requestParameterMap Map of rule parameters
     * @param requestPayloadMap   Map of payload data
     * @return ResponseEntity containing RuleEvaluateResponse from the backend
     * @throws IllegalStateException if the client is disabled or token is missing
     * @throws RuntimeException      if token retrieval fails or backend returns error
     */
    public ResponseEntity<RuleEvaluateResponse> evaluate(String ruleName, Map<String, Object> requestParameterMap, Map<String, Object> requestPayloadMap) {

        if (!properties.isEnabled()) {
            throw new IllegalStateException("Rule Engine Client is disabled.");
        }
        String dynamicToken;
        try {
            dynamicToken = getCurrentAuthorizationHeader();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Token retrieve error!");
        }

        if (dynamicToken == null) {
            throw new RuntimeException("Authorization token not found!");
        }

        RuleEvaluateRequest request = new RuleEvaluateRequest();
        request.setPayload(Objects.isNull(requestPayloadMap) ? new DummyBody() : requestPayloadMap.get("requestPayloadMap"));
        request.setRuleName(ruleName);
        request.setParameters(requestParameterMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", dynamicToken);


        HttpEntity<RuleEvaluateRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                properties.getBaseUrl() + "/evaluate",
                HttpMethod.POST,
                entity,
                RuleEvaluateResponse.class
        );
    }

    /**
     * Asynchronously evaluates a rule by name with given parameters and payload.
     *
     * @param ruleName            The name of the rule to evaluate
     * @param requestParameterMap Map of rule parameters
     * @param requestPayloadMap   Map of payload data
     * @return Mono emitting RuleEvaluateResponse or error
     *
     * <p>Errors:
     * <ul>
     *   <li>IllegalStateException if client is disabled</li>
     *   <li>RuntimeException if token retrieval fails or backend returns error</li>
     * </ul>
     */
    public Mono<RuleEvaluateResponse> evaluateAsync(String ruleName, Map<String, Object> requestParameterMap, Map<String, Object> requestPayloadMap) {
        if (!properties.isEnabled()) {
            return Mono.error(new IllegalStateException("Rule Engine Client is disabled."));
        }
        String dynamicToken;
        try {
            dynamicToken = getCurrentAuthorizationHeader();
        } catch (Exception e) {
            log.error("Token retrieve error!", e);
            return Mono.error(new RuntimeException("Token retrieve error!", e));
        }
        if (dynamicToken == null) {
            return Mono.error(new RuntimeException("Authorization token not found!"));
        }
        RuleEvaluateRequest request = new RuleEvaluateRequest();
        request.setPayload(Objects.isNull(requestPayloadMap) ? new DummyBody() : requestPayloadMap.get("requestPayloadMap"));
        request.setRuleName(ruleName);
        request.setParameters(requestParameterMap);
        return webClient.post()
                .uri("/evaluate")
                .header("Authorization", dynamicToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("Rule engine error: {}", errorBody);
                            return Mono.error(new RuntimeException("Rule engine error: " + errorBody));
                        })
                )
                .bodyToMono(RuleEvaluateResponse.class)
                .doOnError(e -> log.error("Async evaluate error", e));
    }
}

