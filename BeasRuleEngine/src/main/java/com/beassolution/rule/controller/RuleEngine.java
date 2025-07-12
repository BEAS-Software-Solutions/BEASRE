package com.beassolution.rule.controller;

import com.beassolution.rule.components.CacheController;
import com.beassolution.rule.dto.request.RuleEvaluateRequest;
import com.beassolution.rule.dto.response.RuleEvaluateResponse;
import com.beassolution.rule.dto.response.base.BaseResponse;
import com.beassolution.rule.engine.cache.RuleCache;
import com.beassolution.rule.engine.cache.VariableCache;
import com.beassolution.rule.exception.OperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mvel2.MVEL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for rule engine operations.
 * 
 * <p>This controller provides endpoints for rule evaluation and cache management
 * in the Beas Rule Engine. It handles the execution of rules using MVEL
 * expressions and manages the caching of compiled rules and variables.
 * 
 * <p>Key operations include:
 * <ul>
 *   <li>Rule evaluation with parameters and payload</li>
 *   <li>Cache synchronization</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Validated
@RestController("ruleengine")
@RequestMapping(name = "ruleengine", path = "/rule-engine")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PATCH})
@Tag(name = "Rule Engine", description = "Endpoints for rule evaluation and cache management")
public class RuleEngine {
    
    /**
     * Cache controller for managing rule engine caches.
     */
    private final CacheController cacheController;
    
    /**
     * Cache for compiled rule expressions.
     */
    private final RuleCache ruleCache;
    
    /**
     * Cache for rule variables and context.
     */
    private final VariableCache variableCache;

    /**
     * Synchronizes all rule engine caches.
     * 
     * <p>This endpoint triggers a synchronization of all caches including
     * rules, functions, helpers, and variables. The operation is performed
     * asynchronously to avoid blocking the request.
     * 
     * @return CompletableFuture containing the HTTP status response
     */
    @GetMapping("/sync")
    @Operation(summary = "Sync caches", description = "Synchronizes all rule engine caches asynchronously")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cache sync initiated successfully")
    })
    public CompletableFuture<ResponseEntity<HttpStatus>> sync() {
        cacheController.syncCache();
        return CompletableFuture.completedFuture(ResponseEntity.ok(HttpStatus.OK));
    }

    /**
     * Evaluates a rule with the provided parameters and payload.
     * 
     * <p>This endpoint executes a rule by name with the given parameters and payload.
     * The rule is compiled and executed using MVEL, with all variables made available
     * in the execution context.
     * 
     * @param params Query parameters to include in the rule context
     * @param requestPayload The rule evaluation request containing rule name and data
     * @return ResponseEntity containing the rule evaluation result
     * @throws OperationException if the rule is not found
     */
    @PostMapping("/evaluate")
    @Operation(summary = "Evaluate rule", description = "Evaluates a rule with the provided parameters and payload")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule evaluated successfully",
                    content = @Content(schema = @Schema(implementation = RuleEvaluateResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request or rule not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error during rule execution")
    })
    public ResponseEntity<RuleEvaluateResponse> evaluate(
            @Parameter(description = "Query parameters to include in rule context") 
            @RequestParam Map<String, Object> params,
            @Parameter(description = "Rule evaluation request containing rule name and data") 
            @RequestBody @Valid RuleEvaluateRequest requestPayload) {
        
        String ruleName = requestPayload.getRuleName();
        var compiled = ruleCache.get(ruleName)
            .orElseThrow(() -> new OperationException("Rule not found: " + ruleName, HttpStatus.NOT_FOUND));

        // Use pattern matching for instanceof (Java 16+)
        Map<String, Object> vars = new HashMap<>();
        variableCache.get(ruleName)
            .filter(cachedVars -> cachedVars instanceof Map<?, ?>)
            .ifPresent(cachedVars -> vars.putAll((Map<? extends String, ?>) cachedVars));
        
        // Add query parameters
        if (!params.isEmpty()) {
            vars.putAll(params);
        }
        
        // Add payload and parameters from request
        if (requestPayload.getPayload() != null) {
            vars.put("payload", requestPayload.getPayload());
            vars.putAll(requestPayload.getParameters());
        }

        // Execute rule with context
        Object response = vars.isEmpty() 
            ? MVEL.executeExpression(compiled)
            : MVEL.executeExpression(compiled, vars);

        // Create response using constructor
        var resp = new RuleEvaluateResponse();
        resp.setResponse(response);
        resp.setStatus(new BaseResponse(HttpStatus.OK.getReasonPhrase(), "Validation Executed"));
            
        return ResponseEntity.ok(resp);
    }
}
