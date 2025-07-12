package com.beassolution.rule.engine.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RuleCache class.
 * 
 * <p>This test class provides comprehensive coverage for the RuleCache
 * including all cache operations and thread safety:
 * <ul>
 *   <li>Put and get operations</li>
 *   <li>Bulk operations</li>
 *   <li>Cache management operations</li>
 *   <li>Thread safety verification</li>
 *   <li>Edge cases and validation</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class RuleCacheTest {

    private RuleCache ruleCache;
    private Serializable testRule;
    private String testRuleName;

    /**
     * Sets up the rule cache instance before each test.
     * 
     * <p>This method initializes the RuleCache instance and creates
     * test data for consistent test execution.
     */
    @BeforeEach
    void setUp() {
        ruleCache = new RuleCache();
        testRuleName = "testRule";
        testRule = new TestSerializable("test rule content");
    }

    /**
     * Tests successful rule storage in cache.
     * 
     * <p>This test verifies that rules can be stored successfully
     * in the cache and retrieved correctly.
     */
    @Test
    @DisplayName("Should successfully store and retrieve rule")
    void testPutAndGet() {
        // When: Store rule in cache
        ruleCache.put(testRuleName, testRule);

        // Then: Verify rule can be retrieved
        Optional<Serializable> result = ruleCache.get(testRuleName);
        assertTrue(result.isPresent());
        assertEquals(testRule, result.get());
    }

    /**
     * Tests retrieval of non-existent rule.
     * 
     * <p>This test verifies that attempting to retrieve a non-existent
     * rule returns an empty Optional.
     */
    @Test
    @DisplayName("Should return empty Optional for non-existent rule")
    void testGetNonExistentRule() {
        // When: Try to get non-existent rule
        Optional<Serializable> result = ruleCache.get("nonExistentRule");

        // Then: Verify result is empty
        assertFalse(result.isPresent());
    }

    /**
     * Tests successful rule removal from cache.
     * 
     * <p>This test verifies that rules can be removed successfully
     * from the cache.
     */
    @Test
    @DisplayName("Should successfully remove rule from cache")
    void testRemove() {
        // Given: Rule is stored in cache
        ruleCache.put(testRuleName, testRule);
        assertTrue(ruleCache.get(testRuleName).isPresent());

        // When: Remove rule from cache
        ruleCache.remove(testRuleName);

        // Then: Verify rule is no longer in cache
        assertFalse(ruleCache.get(testRuleName).isPresent());
    }

    /**
     * Tests removal of non-existent rule.
     * 
     * <p>This test verifies that removing a non-existent rule
     * does not cause any issues.
     */
    @Test
    @DisplayName("Should handle removal of non-existent rule")
    void testRemoveNonExistentRule() {
        // When & Then: Remove non-existent rule should not throw exception
        assertDoesNotThrow(() -> {
            ruleCache.remove("nonExistentRule");
        });
    }

    /**
     * Tests successful cache clearing.
     * 
     * <p>This test verifies that the cache can be cleared completely
     * and all stored rules are removed.
     */
    @Test
    @DisplayName("Should successfully clear all rules from cache")
    void testClear() {
        // Given: Multiple rules are stored in cache
        ruleCache.put("rule1", new TestSerializable("content1"));
        ruleCache.put("rule2", new TestSerializable("content2"));
        ruleCache.put("rule3", new TestSerializable("content3"));

        // Verify rules are stored
        assertTrue(ruleCache.get("rule1").isPresent());
        assertTrue(ruleCache.get("rule2").isPresent());
        assertTrue(ruleCache.get("rule3").isPresent());

        // When: Clear cache
        ruleCache.clear();

        // Then: Verify all rules are removed
        assertFalse(ruleCache.get("rule1").isPresent());
        assertFalse(ruleCache.get("rule2").isPresent());
        assertFalse(ruleCache.get("rule3").isPresent());
    }

    /**
     * Tests bulk rule storage.
     * 
     * <p>This test verifies that multiple rules can be stored
     * in the cache using the putAll method.
     */
    @Test
    @DisplayName("Should successfully store multiple rules using putAll")
    void testPutAll() {
        // Given: Map of rules to store
        Map<String, Serializable> rules = new HashMap<>();
        rules.put("rule1", new TestSerializable("content1"));
        rules.put("rule2", new TestSerializable("content2"));
        rules.put("rule3", new TestSerializable("content3"));

        // When: Store all rules using putAll
        ruleCache.putAll(rules);

        // Then: Verify all rules are stored
        assertTrue(ruleCache.get("rule1").isPresent());
        assertTrue(ruleCache.get("rule2").isPresent());
        assertTrue(ruleCache.get("rule3").isPresent());
        assertEquals("content1", ((TestSerializable) ruleCache.get("rule1").get()).getContent());
        assertEquals("content2", ((TestSerializable) ruleCache.get("rule2").get()).getContent());
        assertEquals("content3", ((TestSerializable) ruleCache.get("rule3").get()).getContent());
    }

    /**
     * Tests retrieval of all cached rules.
     * 
     * <p>This test verifies that all cached rules can be retrieved
     * using the getAll method.
     */
    @Test
    @DisplayName("Should successfully retrieve all cached rules")
    void testGetAll() {
        // Given: Multiple rules are stored in cache
        ruleCache.put("rule1", new TestSerializable("content1"));
        ruleCache.put("rule2", new TestSerializable("content2"));
        ruleCache.put("rule3", new TestSerializable("content3"));

        // When: Get all rules
        Map<String, Serializable> allRules = ruleCache.getAll();

        // Then: Verify all rules are returned
        assertEquals(3, allRules.size());
        assertTrue(allRules.containsKey("rule1"));
        assertTrue(allRules.containsKey("rule2"));
        assertTrue(allRules.containsKey("rule3"));
        assertEquals("content1", ((TestSerializable) allRules.get("rule1")).getContent());
        assertEquals("content2", ((TestSerializable) allRules.get("rule2")).getContent());
        assertEquals("content3", ((TestSerializable) allRules.get("rule3")).getContent());
    }

    /**
     * Tests rule existence checking.
     * 
     * <p>This test verifies that the contains method correctly
     * identifies whether a rule exists in the cache.
     */
    @Test
    @DisplayName("Should correctly check rule existence")
    void testContains() {
        // Given: Rule is stored in cache
        ruleCache.put(testRuleName, testRule);

        // When & Then: Verify contains method works correctly
        assertTrue(ruleCache.contains(testRuleName));
        assertFalse(ruleCache.contains("nonExistentRule"));
    }

    /**
     * Tests rule overwriting.
     * 
     * <p>This test verifies that storing a rule with an existing key
     * overwrites the previous value.
     */
    @Test
    @DisplayName("Should overwrite existing rule when storing with same key")
    void testOverwriteExistingRule() {
        // Given: Initial rule is stored
        ruleCache.put(testRuleName, testRule);
        assertEquals(testRule, ruleCache.get(testRuleName).get());

        // When: Store new rule with same key
        Serializable newRule = new TestSerializable("new content");
        ruleCache.put(testRuleName, newRule);

        // Then: Verify old rule is overwritten
        assertEquals(newRule, ruleCache.get(testRuleName).get());
        assertNotEquals(testRule, ruleCache.get(testRuleName).get());
    }

    /**
     * Tests storage of null rule.
     * 
     * <p>This test verifies that null rules can be stored
     * in the cache without issues.
     */
    @Test
    @DisplayName("Should handle storage of null rule")
    void testStoreNullRule() {
        // When: Store null rule
        ruleCache.put(testRuleName, null);

        // Then: Verify null rule can be retrieved
        Optional<Serializable> result = ruleCache.get(testRuleName);
        assertTrue(result.isPresent());
        assertNull(result.get());
    }

    /**
     * Tests storage of empty string key.
     * 
     * <p>This test verifies that empty string keys can be used
     * to store rules in the cache.
     */
    @Test
    @DisplayName("Should handle storage with empty string key")
    void testStoreWithEmptyKey() {
        // When: Store rule with empty key
        ruleCache.put("", testRule);

        // Then: Verify rule can be retrieved with empty key
        Optional<Serializable> result = ruleCache.get("");
        assertTrue(result.isPresent());
        assertEquals(testRule, result.get());
    }

    /**
     * Tests storage with null key.
     * 
     * <p>This test verifies that null keys can be used
     * to store rules in the cache.
     */
    @Test
    @DisplayName("Should handle storage with null key")
    void testStoreWithNullKey() {
        // When: Store rule with null key
        ruleCache.put(null, testRule);

        // Then: Verify rule can be retrieved with null key
        Optional<Serializable> result = ruleCache.get(null);
        assertTrue(result.isPresent());
        assertEquals(testRule, result.get());
    }

    /**
     * Tests concurrent access to cache.
     * 
     * <p>This test verifies that the cache is thread-safe
     * and can handle concurrent access from multiple threads.
     */
    @Test
    @DisplayName("Should handle concurrent access safely")
    void testConcurrentAccess() throws InterruptedException {
        // Given: Multiple threads accessing cache
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // When: Start multiple threads that access cache concurrently
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                String key = "rule" + threadId;
                Serializable value = new TestSerializable("content" + threadId);
                
                // Store rule
                ruleCache.put(key, value);
                
                // Retrieve rule
                Optional<Serializable> result = ruleCache.get(key);
                assertTrue(result.isPresent());
                assertEquals(value, result.get());
                
                // Check existence
                assertTrue(ruleCache.contains(key));
            });
            threads[i].start();
        }

        // Then: Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Verify all rules are stored
        assertEquals(threadCount, ruleCache.getAll().size());
    }

    /**
     * Tests cache performance with large number of rules.
     * 
     * <p>This test verifies that the cache can handle a large
     * number of rules efficiently.
     */
    @Test
    @DisplayName("Should handle large number of rules efficiently")
    void testLargeNumberOfRules() {
        // Given: Large number of rules
        int ruleCount = 1000;

        // When: Store large number of rules
        for (int i = 0; i < ruleCount; i++) {
            String key = "rule" + i;
            Serializable value = new TestSerializable("content" + i);
            ruleCache.put(key, value);
        }

        // Then: Verify all rules are stored and retrievable
        assertEquals(ruleCount, ruleCache.getAll().size());
        
        for (int i = 0; i < ruleCount; i++) {
            String key = "rule" + i;
            assertTrue(ruleCache.contains(key));
            Optional<Serializable> result = ruleCache.get(key);
            assertTrue(result.isPresent());
            assertEquals("content" + i, ((TestSerializable) result.get()).getContent());
        }
    }

    /**
     * Tests cache isolation between instances.
     * 
     * <p>This test verifies that different cache instances
     * are isolated from each other.
     */
    @Test
    @DisplayName("Should maintain isolation between cache instances")
    void testCacheIsolation() {
        // Given: Two separate cache instances
        RuleCache cache1 = new RuleCache();
        RuleCache cache2 = new RuleCache();

        // When: Store rules in different caches
        cache1.put("rule1", new TestSerializable("content1"));
        cache2.put("rule2", new TestSerializable("content2"));

        // Then: Verify caches are isolated
        assertTrue(cache1.get("rule1").isPresent());
        assertFalse(cache1.get("rule2").isPresent());
        assertFalse(cache2.get("rule1").isPresent());
        assertTrue(cache2.get("rule2").isPresent());
    }

    /**
     * Test serializable class for testing purposes.
     * 
     * <p>This class provides a simple serializable implementation
     * for testing the RuleCache functionality.
     */
    private static class TestSerializable implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String content;

        public TestSerializable(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestSerializable that = (TestSerializable) obj;
            return content.equals(that.content);
        }

        @Override
        public int hashCode() {
            return content.hashCode();
        }

        @Override
        public String toString() {
            return "TestSerializable{content='" + content + "'}";
        }
    }
} 