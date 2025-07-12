package com.beassolution.rule.engine;

import com.beassolution.rule.engine.cache.FunctionCache;
import com.beassolution.rule.engine.cache.InstanceCache;
import com.beassolution.rule.engine.cache.RuleCache;
import com.beassolution.rule.engine.cache.VariableCache;
import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.FunctionLibrary;
import com.beassolution.rule.model.RuleHelper;
import com.beassolution.rule.model.RuleLibrary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Manager class for rule engine operations and caching.
 * 
 * <p>This class is responsible for managing the compilation and caching of
 * rules, functions, and helpers in the Beas Rule Engine. It coordinates
 * the loading and caching of all components needed for rule execution.
 * 
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Caching helper class instances</li>
 *   <li>Caching function library code</li>
 *   <li>Compiling and caching rule expressions</li>
 *   <li>Managing rule execution context</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RuleEngineManager {

    /**
     * Cache for helper class instances.
     */
    private final InstanceCache helperCache;
    
    /**
     * Cache for compiled rule expressions.
     */
    private final RuleCache ruleCache;
    
    /**
     * Cache for function library code.
     */
    private final FunctionCache functionCache;
    
    /**
     * Cache for rule variables and context.
     */
    private final VariableCache variableCache;

    /**
     * Component for creating helper instances.
     */
    private final InstanceInitiator instanceInitiator;

    /**
     * Caches helper class instances for rule execution.
     * 
     * <p>This method loads and caches helper class instances from JAR files.
     * The helpers are instantiated and made available for use during rule
     * execution. The cache is cleared before loading new helpers.
     * 
     * @param ruleHelpers List of rule helper configurations
     * @throws OperationException if helpers list is null or empty
     */
    public void cacheHelpers(List<RuleHelper> ruleHelpers) {
        if (ruleHelpers == null || ruleHelpers.isEmpty()) {
            throw new OperationException("Rule Helpers cannot be null or empty!");
        }

        helperCache.clear();

        for (RuleHelper helper : ruleHelpers) {
            log.info("Helper '{}' initializing...", helper.getName());
            Map<String, Object> instances = instanceInitiator.create(helper);
            helperCache.put(helper.getName(), instances);
            log.info("Helper '{}' initialized.", helper.getName());
        }
    }

    /**
     * Caches function library code for rule execution.
     * 
     * <p>This method caches MVEL function code from function libraries.
     * The functions are stored as strings and will be included in rule
     * compilation. The cache is cleared before loading new functions.
     * 
     * @param functions List of function library configurations
     * @throws OperationException if functions list is null or empty
     */
    public void cacheFunctions(List<FunctionLibrary> functions) {
        if (functions == null || functions.isEmpty()) {
            throw new OperationException("Function Library cannot be null or empty!");
        }

        functionCache.clear();

        for (FunctionLibrary function : functions) {
            log.info("Function '{}' caching...", function.getName());
            functionCache.put(function.getName(), function.getMvlCode());
            log.info("Function '{}' cached.", function.getName());
        }
    }

    /**
     * Compiles and caches rules for execution.
     * 
     * <p>This method compiles rule libraries into executable MVEL expressions.
     * The compilation process includes helper instances and function code
     * to create a complete execution context for each rule.
     * 
     * @param rules List of rule library configurations
     * @throws OperationException if rules list is null or empty
     */
    public void cacheRules(List<RuleLibrary> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new OperationException("Rules cannot be null or empty!");
        }

        ruleCache.clear();

        Map<String, Object> vars = new HashMap<>();
        
        for (RuleLibrary rule : rules) {
            // Process helpers
            processHelpers(rule, vars);
            
            // Build MVEL code using text blocks (Java 15+)
            String mvelCode = buildMvelCode(rule);
            
            log.info("MVEL initializing {}", rule.getName());
            log.debug("Generated MVEL code: {}", mvelCode);

            // Compile and cache the rule
            Serializable compiled = compileRule(mvelCode);
            ruleCache.put(rule.getName(), compiled);
            variableCache.put(rule.getName(), vars);
            
            log.info("MVEL initialized {}", rule.getName());
        }
    }

    /**
     * Processes helper classes for a rule.
     * 
     * @param rule The rule to process helpers for
     * @param vars The variables map to populate
     */
    private void processHelpers(RuleLibrary rule, Map<String, Object> vars) {
        if (rule.getHelpers().isEmpty()) {
            return;
        }
        
        for (String helperName : rule.getHelpers()) {
            if (helperName.isEmpty()) {
                log.warn("HelperName is empty, skipping...");
                continue;
            }

            log.info("{} Helper adding...", helperName);
            var helper = helperCache.get(helperName);
            
            if (helper.isEmpty()) {
                log.warn("Helper couldn't find {}, skipping...", helperName);
                continue;
            }
            
            vars.put(helperName, helper.get());
            log.info("{} Helper added.", helperName);
        }
    }

    /**
     * Builds MVEL code for a rule using text blocks.
     * 
     * @param rule The rule to build code for
     * @return The complete MVEL code string
     */
    private String buildMvelCode(RuleLibrary rule) {
        StringBuilder functionsCode = new StringBuilder();
        
        // Process functions
        if (!rule.getFunctions().isEmpty()) {
            for (String functionName : rule.getFunctions()) {
                log.info("{} Function adding...", functionName);
                var function = functionCache.get(functionName);
                
                if (function.isEmpty()) {
                    log.warn("Function couldn't find {}, skipping...", functionName);
                    continue;
                }
                
                functionsCode.append(function.get()).append("\n");
                log.debug("Function code: {}", function.get());
                log.info("{} Function added.", functionName);
            }
        }

        // Use text blocks for better readability (Java 15+)
        return """
            (
            %s
            %s
            )
            """.formatted(functionsCode.toString(), rule.getMvlCode());
    }

    /**
     * Compiles a rule expression.
     * 
     * @param mvelCode The MVEL code to compile
     * @return The compiled Serializable expression
     */
    private Serializable compileRule(String mvelCode) {
        ParserContext context = new ParserContext();
        context.setStrongTyping(false);
        context.setRetainParserState(true);

        return MVEL.compileExpression(mvelCode, context);
    }
}
