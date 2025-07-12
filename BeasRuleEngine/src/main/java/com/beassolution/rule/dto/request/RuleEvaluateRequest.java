package com.beassolution.rule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Request DTO for rule evaluation operations.
 * 
 * <p>This class represents the request payload for evaluating rules in the
 * Beas Rule Engine. It contains the rule name to execute and optional
 * parameters and payload data.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Rule name (required)</li>
 *   <li>Optional parameters map</li>
 *   <li>Optional payload object</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class RuleEvaluateRequest {
    
    /**
     * Name of the rule to evaluate.
     * 
     * <p>This field is required and specifies which rule should be executed.
     * The rule name must correspond to an existing rule in the system.
     */
    @NotNull(message = "ruleName cannot be null!")
    private String ruleName;
    
    /**
     * Optional parameters to pass to the rule execution.
     * 
     * <p>This field contains key-value pairs that will be made available
     * as variables during rule execution. Parameters are merged with
     * the payload and other context variables.
     */
    private Map<String, Object> parameters;
    
    /**
     * Optional payload object for rule execution.
     * 
     * <p>This field contains the main data object that will be processed
     * by the rule. The payload is made available as a variable named "payload"
     * during rule execution.
     */
    private Object payload;
}
