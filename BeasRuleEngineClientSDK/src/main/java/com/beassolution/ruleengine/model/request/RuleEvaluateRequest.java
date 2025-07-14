package com.beassolution.ruleengine.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Request model for evaluating a rule via the BEAS Rule Engine.
 * <p>
 * Contains the rule name, parameters, and payload to be sent to the engine.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Data
public class RuleEvaluateRequest {
    /**
     * Name of the rule to evaluate. Cannot be null.
     */
    @NotNull(message = "ruleName cannot be null!")
    private String ruleName;
    /**
     * Parameters for the rule evaluation.
     */
    private Map<String, Object> parameters;
    /**
     * Payload data for the rule evaluation.
     */
    private Object payload;

    /**
     * Sets the payload data.
     * @param payload the payload object
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * Sets the rule name.
     * @param ruleName the rule name
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * Sets the parameters map.
     * @param parameters the parameters map
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
