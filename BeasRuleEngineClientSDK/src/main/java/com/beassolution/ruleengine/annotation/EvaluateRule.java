package com.beassolution.ruleengine.annotation;

import java.lang.annotation.*;

/**
 * Annotation for marking methods that should trigger rule evaluation via the BEAS Rule Engine.
 *
 * <p>Usage example:
 * <pre>
 *   @EvaluateRule(ruleName = "myRule", throwIfInvalid = true)
 *   public void myMethod(...) { ... }
 * </pre>
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EvaluateRule {
    /**
     * The name of the rule to evaluate.
     */
    String ruleName() default "";

    /**
     * Whether to throw an exception if the rule is invalid.
     */
    boolean throwIfInvalid() default true;
}
