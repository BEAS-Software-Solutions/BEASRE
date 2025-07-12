package com.beassolution.rule.engine.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache for rule variables and context.
 * 
 * <p>This class provides a thread-safe cache for storing variables and context
 * data that are used during rule execution in the Beas Rule Engine. The cache
 * stores rule-specific variables that are made available during MVEL expression
 * evaluation.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Thread-safe operations using ConcurrentHashMap</li>
 *   <li>Rule-specific variable storage</li>
 *   <li>Bulk operations support</li>
 *   <li>Cache management operations</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
public final class VariableCache {

    /**
     * Thread-safe map for storing rule variables and context.
     * 
     * <p>This map stores rule names as keys and variable maps as values.
     * The variables are made available during rule execution.
     */
    private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    /**
     * Stores variables for a rule in the cache.
     * 
     * @param key The rule name
     * @param instance The variables map for the rule
     */
    public void put(String key, Object instance) {
        cache.put(key, instance);
    }

    /**
     * Stores multiple rule variable sets in the cache.
     * 
     * @param map Map containing rule names and variable sets
     */
    public void putAll(Map<String,Object> map) {
        cache.putAll(map);
    }

    /**
     * Retrieves variables for a rule from the cache.
     * 
     * @param key The rule name
     * @return Optional containing the variables if found
     */
    public Optional<Object> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Removes variables for a rule from the cache.
     * 
     * @param key The rule name to remove
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clears all rule variables from the cache.
     * 
     * <p>This method removes all entries from the cache, typically used
     * during cache synchronization or system reset.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves all cached rule variables.
     * 
     * @return Immutable copy of all cached variables
     */
    public Map<String, Object> getAll() {
        return Map.copyOf(cache);
    }

    /**
     * Checks if variables for a rule are cached.
     * 
     * @param key The rule name to check
     * @return true if variables are cached, false otherwise
     */
    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    /**
     * Returns a string representation of the cache.
     * 
     * @return String containing cache statistics
     */
    @Override
    public String toString() {
        return "RuleCache{" +
                "entries=" + cache.size() +
                '}';
    }
}
