package com.beassolution.ruleengine.model;

import lombok.Data;

/**
 * DummyBody is a placeholder payload used when no actual payload is provided to the rule engine.
 * <p>
 * This class is used internally to ensure that the payload is never null.
 *
 * <p>Example usage:
 * <pre>
 *   RuleEvaluateRequest req = new RuleEvaluateRequest();
 *   req.setPayload(new DummyBody());
 * </pre>
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Data
public class DummyBody {
    /**
     * Indicates this is a dummy payload.
     */
    private boolean dummy = true;
}
