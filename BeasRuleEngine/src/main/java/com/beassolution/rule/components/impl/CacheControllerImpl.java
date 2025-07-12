package com.beassolution.rule.components.impl;

import com.beassolution.rule.components.CacheController;
import com.beassolution.rule.engine.RuleEngineManager;
import com.beassolution.rule.model.FunctionLibrary;
import com.beassolution.rule.model.RuleHelper;
import com.beassolution.rule.model.RuleLibrary;
import com.beassolution.rule.repository.FunctionLibraryRepository;
import com.beassolution.rule.repository.RuleHelperRepository;
import com.beassolution.rule.repository.RuleLibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of cache controller for the Beas Rule Engine.
 * 
 * <p>This class provides the implementation for cache synchronization operations
 * in the rule engine. It coordinates the refresh of all caches including rules,
 * functions, and helpers based on the configured container name.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Complete cache synchronization</li>
 *   <li>Container-based filtering</li>
 *   <li>Asynchronous operation support</li>
 *   <li>Comprehensive logging</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CacheControllerImpl implements CacheController {
    
    /**
     * Repository for rule helper operations.
     */
    private final RuleHelperRepository ruleHelperRepository;
    
    /**
     * Repository for function library operations.
     */
    private final FunctionLibraryRepository functionLibraryRepository;
    
    /**
     * Repository for rule library operations.
     */
    private final RuleLibraryRepository ruleLibraryRepository;
    
    /**
     * Manager for rule engine operations.
     */
    private final RuleEngineManager ruleEngineManager;
    
    /**
     * Container name for filtering entities.
     */
    @Value("${rule.container.name}")
    private String containerName;

    /**
     * Synchronizes all rule engine caches.
     * 
     * <p>This method performs a complete refresh of all caches in the rule engine.
     * It loads entities from the database based on the configured container name
     * and updates the respective caches through the rule engine manager.
     */
    @Override
    public void syncCache() {
        log.info("Sync started...");
        log.info("Helpers caching...");
        List<RuleHelper> helpers = ruleHelperRepository.findByContainerName(containerName);
        if (!helpers.isEmpty())
            ruleEngineManager.cacheHelpers(helpers);
        else
            log.info("There is no helpers.");

        log.info("Functions caching...");
        List<FunctionLibrary> functions = functionLibraryRepository.findByContainerName(containerName);
        if (!functions.isEmpty())
            ruleEngineManager.cacheFunctions(functions);
        else
            log.info("There is no functions.");

        log.info("Rule caching...");
        List<RuleLibrary> rules = ruleLibraryRepository.findByContainerName(containerName);
        if (!rules.isEmpty())
            ruleEngineManager.cacheRules(rules);
        else
            log.info("There is no rules.");

        log.info("Sync completed.");
    }
}
