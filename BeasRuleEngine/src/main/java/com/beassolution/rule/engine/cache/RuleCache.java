package com.beassolution.rule.engine.cache;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache for compiled rule expressions.
 * 
 * <p>This class provides a thread-safe cache for storing compiled MVEL expressions
 * that represent rules in the Beas Rule Engine. The cache improves performance
 * by avoiding re-compilation of rules on each execution.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Thread-safe operations using ConcurrentHashMap</li>
 *   <li>Compiled MVEL expressions storage</li>
 *   <li>Bulk operations support</li>
 *   <li>Cache management operations</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
public final class RuleCache {

    /**
     * Thread-safe map for storing compiled rule expressions.
     * 
     * <p>This map stores rule names as keys and compiled MVEL expressions
     * as values. The expressions are Serializable for potential persistence.
     */
    private final ConcurrentMap<String, Serializable> cache = new ConcurrentHashMap<>();

    /**
     * Stores a compiled rule expression in the cache.
     * 
     * @param key The rule name
     * @param instance The compiled MVEL expression
     */
    public void put(String key, Serializable instance) {
        cache.put(key, instance);
    }

    /**
     * Stores multiple compiled rule expressions in the cache.
     * 
     * @param map Map containing rule names and compiled expressions
     */
    public void putAll(Map<String,Serializable> map) {
        cache.putAll(map);
    }

    /**
     * Retrieves a compiled rule expression from the cache.
     * 
     * @param key The rule name
     * @return Optional containing the compiled expression if found
     */
    public Optional<Serializable> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Removes a compiled rule expression from the cache.
     * 
     * @param key The rule name to remove
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clears all compiled rule expressions from the cache.
     * 
     * <p>This method removes all entries from the cache, typically used
     * during cache synchronization or system reset.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves all cached rule expressions.
     * 
     * @return Immutable copy of all cached expressions
     */
    public Map<String, Serializable> getAll() {
        return Map.copyOf(cache);
    }

    /**
     * Checks if a rule expression is cached.
     * 
     * @param key The rule name to check
     * @return true if the rule is cached, false otherwise
     */
    public boolean contains(String key) {
        return cache.containsKey(key);
    }
}
