package com.beassolution.rule.controller;

import com.beassolution.rule.components.CacheController;
import com.beassolution.rule.dto.request.RuleEvaluateRequest;
import com.beassolution.rule.dto.response.RuleEvaluateResponse;
import com.beassolution.rule.dto.response.base.BaseResponse;
import com.beassolution.rule.engine.cache.RuleCache;
import com.beassolution.rule.engine.cache.VariableCache;
import com.beassolution.rule.exception.OperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mvel2.MVEL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the RuleEngine controller.
 * 
 * <p>This test class provides comprehensive coverage for all endpoints
 * in the RuleEngine controller including:
 * <ul>
 *   <li>Cache synchronization endpoint</li>
 *   <li>Rule evaluation endpoint</li>
 *   <li>Success scenarios</li>
 *   <li>Error handling scenarios</li>
 *   <li>Edge cases and validation</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */

@WebMvcTest(RuleEngine.class)
class RuleEngineTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheController cacheController;

    @MockBean
    private RuleCache ruleCache;

    @MockBean
    private VariableCache variableCache;

    private RuleEvaluateRequest validRequest;
    private Serializable compiledRule;

    /**
     * Sets up test data and mocks before each test.
     * 
     * <p>This method initializes common test data and configures
     * mock behaviors for consistent test execution.
     */
    @BeforeEach
    void setUp() {
        // Create a valid rule evaluation request
        validRequest = new RuleEvaluateRequest();
        validRequest.setRuleName("testRule");
        validRequest.setPayload("testPayload");
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", 42);
        validRequest.setParameters(parameters);

        // Create a compiled rule expression
        compiledRule = MVEL.compileExpression("payload + ' processed'");
    }

    /**
     * Tests successful cache synchronization.
     * 
     * <p>This test verifies that the cache sync endpoint returns
     * a successful response and calls the cache controller.
     */
    @Test
    @DisplayName("Should successfully sync caches")
    void testSyncCaches() throws Exception {
        // Given: Cache controller is mocked
        doNothing().when(cacheController).syncCache();

        // When & Then: Perform GET request to sync endpoint
        mockMvc.perform(get("/rule-engine/sync"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Verify cache controller was called
        verify(cacheController, times(1)).syncCache();
    }

    /**
     * Tests successful rule evaluation with parameters.
     * 
     * <p>This test verifies that a rule can be evaluated successfully
     * with both query parameters and request payload.
     */
    @Test
    @DisplayName("Should successfully evaluate rule with parameters")
    void testEvaluateRuleWithParameters() throws Exception {
        // Given: Rule cache returns compiled rule
        when(ruleCache.get("testRule")).thenReturn(Optional.of(compiledRule));
        when(variableCache.get("testRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request to evaluate endpoint
        mockMvc.perform(post("/rule-engine/evaluate")
                .param("queryParam", "queryValue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "testRule",
                        "payload": "testPayload",
                        "parameters": {
                            "param1": "value1",
                            "param2": 42
                        }
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("testPayload processed"))
                .andExpect(jsonPath("$.status.message").value("OK"))
                .andExpect(jsonPath("$.status.status").value("Validation Executed"));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("testRule");
        verify(variableCache, times(1)).get("testRule");
    }

    /**
     * Tests successful rule evaluation without parameters.
     * 
     * <p>This test verifies that a rule can be evaluated successfully
     * when no parameters are provided.
     */
    @Test
    @DisplayName("Should successfully evaluate rule without parameters")
    void testEvaluateRuleWithoutParameters() throws Exception {
        // Given: Rule cache returns compiled rule, no variables
        when(ruleCache.get("testRule")).thenReturn(Optional.of(compiledRule));
        when(variableCache.get("testRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request without parameters
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "testRule",
                        "payload": "testPayload"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("testPayload processed"));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("testRule");
        verify(variableCache, times(1)).get("testRule");
    }

    /**
     * Tests rule evaluation with cached variables.
     * 
     * <p>This test verifies that cached variables are properly
     * included in the rule execution context.
     */
    @Test
    @DisplayName("Should evaluate rule with cached variables")
    void testEvaluateRuleWithCachedVariables() throws Exception {
        // Given: Cached variables are available
        Map<String, Object> cachedVars = new HashMap<>();
        cachedVars.put("cachedVar", "cachedValue");
        
        when(ruleCache.get("testRule")).thenReturn(Optional.of(compiledRule));
        when(variableCache.get("testRule")).thenReturn(Optional.of(cachedVars));

        // When & Then: Perform POST request
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "testRule",
                        "payload": "testPayload"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("testPayload processed"));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("testRule");
        verify(variableCache, times(1)).get("testRule");
    }

    /**
     * Tests rule evaluation failure when rule is not found.
     * 
     * <p>This test verifies that appropriate error handling occurs
     * when a requested rule is not found in the cache.
     */
    @Test
    @DisplayName("Should return error when rule is not found")
    void testEvaluateRuleNotFound() throws Exception {
        // Given: Rule cache returns empty
        when(ruleCache.get("nonExistentRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request with non-existent rule
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "nonExistentRule",
                        "payload": "testPayload"
                    }
                    """))
                .andExpect(status().isNotFound());

        // Verify cache was checked
        verify(ruleCache, times(1)).get("nonExistentRule");
    }

    /**
     * Tests rule evaluation with invalid request payload.
     * 
     * <p>This test verifies that validation errors are properly
     * handled when the request payload is invalid.
     */
    @Test
    @DisplayName("Should return error for invalid request payload")
    void testEvaluateRuleWithInvalidPayload() throws Exception {
        // When & Then: Perform POST request with invalid JSON
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests rule evaluation with missing rule name.
     * 
     * <p>This test verifies that validation errors are properly
     * handled when the rule name is missing from the request.
     */
    @Test
    @DisplayName("Should return error when rule name is missing")
    void testEvaluateRuleWithMissingRuleName() throws Exception {
        // When & Then: Perform POST request without rule name
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "payload": "testPayload"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests rule evaluation with complex MVEL expression.
     * 
     * <p>This test verifies that complex MVEL expressions can be
     * executed successfully with proper variable context.
     */
    @Test
    @DisplayName("Should evaluate complex MVEL expression")
    void testEvaluateComplexMvelExpression() throws Exception {
        // Given: Complex compiled rule
        Serializable complexRule = MVEL.compileExpression("payload.length() > 5 ? 'Long payload' : 'Short payload'");
        when(ruleCache.get("complexRule")).thenReturn(Optional.of(complexRule));
        when(variableCache.get("complexRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request with complex rule
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "complexRule",
                        "payload": "Very long payload string"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Long payload"));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("complexRule");
    }

    /**
     * Tests rule evaluation with numeric operations.
     * 
     * <p>This test verifies that MVEL expressions can perform
     * mathematical operations on numeric parameters.
     */
    @Test
    @DisplayName("Should evaluate rule with numeric operations")
    void testEvaluateRuleWithNumericOperations() throws Exception {
        // Given: Numeric operation rule
        Serializable numericRule = MVEL.compileExpression("param1 + param2 * 2");
        when(ruleCache.get("numericRule")).thenReturn(Optional.of(numericRule));
        when(variableCache.get("numericRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request with numeric parameters
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "numericRule",
                        "parameters": {
                            "param1": 10,
                            "param2": 5
                        }
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(20));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("numericRule");
    }

    /**
     * Tests rule evaluation with boolean operations.
     * 
     * <p>This test verifies that MVEL expressions can perform
     * logical operations and return boolean results.
     */
    @Test
    @DisplayName("Should evaluate rule with boolean operations")
    void testEvaluateRuleWithBooleanOperations() throws Exception {
        // Given: Boolean operation rule
        Serializable booleanRule = MVEL.compileExpression("param1 == 'value1' && param2 > 40");
        when(ruleCache.get("booleanRule")).thenReturn(Optional.of(booleanRule));
        when(variableCache.get("booleanRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request with boolean logic
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "booleanRule",
                        "parameters": {
                            "param1": "value1",
                            "param2": 42
                        }
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(true));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("booleanRule");
    }

    /**
     * Tests rule evaluation with empty payload.
     * 
     * <p>This test verifies that rules can be evaluated successfully
     * even when the payload is null or empty.
     */
    @Test
    @DisplayName("Should evaluate rule with empty payload")
    void testEvaluateRuleWithEmptyPayload() throws Exception {
        // Given: Rule that handles empty payload
        Serializable emptyPayloadRule = MVEL.compileExpression("payload == null ? 'No payload' : 'Has payload'");
        when(ruleCache.get("emptyPayloadRule")).thenReturn(Optional.of(emptyPayloadRule));
        when(variableCache.get("emptyPayloadRule")).thenReturn(Optional.empty());

        // When & Then: Perform POST request without payload
        mockMvc.perform(post("/rule-engine/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "ruleName": "emptyPayloadRule"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("No payload"));

        // Verify cache interactions
        verify(ruleCache, times(1)).get("emptyPayloadRule");
    }
} 