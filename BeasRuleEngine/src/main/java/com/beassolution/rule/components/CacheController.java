package com.beassolution.rule.components;

import org.springframework.scheduling.annotation.Async;

/**
 * Interface for cache management operations in the Beas Rule Engine.
 * 
 * <p>This interface defines the contract for cache synchronization operations
 * that refresh all caches in the rule engine. The operations are designed
 * to be asynchronous to avoid blocking the calling thread.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Asynchronous cache synchronization</li>
 *   <li>Complete cache refresh capabilities</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface CacheController {
    
    /**
     * Synchronizes all rule engine caches asynchronously.
     * 
     * <p>This method triggers a complete refresh of all caches including
     * rules, functions, helpers, and variables. The operation is performed
     * asynchronously to avoid blocking the calling thread.
     */
    @Async
    void syncCache();
}
