package com.beassolution.rule.engine.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache for helper class instances.
 * 
 * <p>This class provides a thread-safe cache for storing instantiated helper
 * class objects. The cache improves performance by avoiding repeated
 * instantiation of helper classes during rule execution.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Thread-safe operations using ConcurrentHashMap</li>
 *   <li>Helper class instance storage</li>
 *   <li>Bulk operations support</li>
 *   <li>Cache management operations</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
public final class InstanceCache {

    /**
     * Thread-safe map for storing helper class instances.
     * 
     * <p>This map stores helper names as keys and maps of class names to
     * instantiated objects as values. The instances are created from JAR files.
     */
    private final ConcurrentMap<String, Map<String, Object>> cache = new ConcurrentHashMap<>();

    /**
     * Stores helper instances in the cache.
     * 
     * @param key The helper name
     * @param instance The map of class names to instantiated objects
     */
    public void put(String key, Map<String, Object> instance) {
        cache.put(key, instance);
    }

    /**
     * Stores multiple helper instance sets in the cache.
     * 
     * @param map Map containing helper names and instance maps
     */
    public void putAll(Map<String,Map<String, Object>> map) {
        cache.putAll(map);
    }

    /**
     * Retrieves helper instances from the cache.
     * 
     * @param key The helper name
     * @return Optional containing the helper instances if found
     */
    public Optional<Map<String, Object>> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Removes helper instances from the cache.
     * 
     * @param key The helper name to remove
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clears all helper instances from the cache.
     * 
     * <p>This method removes all entries from the cache, typically used
     * during cache synchronization or system reset.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves all cached helper instances.
     * 
     * @return Immutable copy of all cached helper instances
     */
    public Map<String, Map<String, Object>> getAll() {
        return Map.copyOf(cache);
    }

    /**
     * Checks if helper instances are cached.
     * 
     * @param key The helper name to check
     * @return true if helper instances are cached, false otherwise
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
        return "InstanceCache{" +
                "entries=" + cache.size() +
                '}';
    }
}
