package com.beassolution.rule.engine.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache for function library code.
 * 
 * <p>This class provides a thread-safe cache for storing MVEL function code
 * from function libraries. The cache improves performance by avoiding
 * repeated loading of function code during rule compilation.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Thread-safe operations using ConcurrentHashMap</li>
 *   <li>MVEL function code storage</li>
 *   <li>Bulk operations support</li>
 *   <li>Cache management operations</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
public final class FunctionCache {

    /**
     * Thread-safe map for storing function library code.
     * 
     * <p>This map stores function library names as keys and MVEL code
     * as values. The code is stored as strings for inclusion in rule compilation.
     */
    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * Stores function code in the cache.
     * 
     * @param key The function library name
     * @param instance The MVEL function code
     */
    public void put(String key, String instance) {
        cache.put(key, instance);
    }

    /**
     * Stores multiple function codes in the cache.
     * 
     * @param map Map containing function library names and MVEL code
     */
    public void putAll(Map<String,String> map) {
        cache.putAll(map);
    }

    /**
     * Retrieves function code from the cache.
     * 
     * @param key The function library name
     * @return Optional containing the function code if found
     */
    public Optional<String> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Removes function code from the cache.
     * 
     * @param key The function library name to remove
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clears all function codes from the cache.
     * 
     * <p>This method removes all entries from the cache, typically used
     * during cache synchronization or system reset.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves all cached function codes.
     * 
     * @return Immutable copy of all cached function codes
     */
    public Map<String, String> getAll() {
        return Map.copyOf(cache);
    }

    /**
     * Checks if function code is cached.
     * 
     * @param key The function library name to check
     * @return true if the function code is cached, false otherwise
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
        return "FunctionCache{" +
                "entries=" + cache.size() +
                '}';
    }
}
