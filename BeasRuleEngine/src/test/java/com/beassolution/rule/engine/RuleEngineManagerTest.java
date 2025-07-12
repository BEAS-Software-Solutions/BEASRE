package com.beassolution.rule.engine;

import com.beassolution.rule.engine.cache.FunctionCache;
import com.beassolution.rule.engine.cache.InstanceCache;
import com.beassolution.rule.engine.cache.RuleCache;
import com.beassolution.rule.engine.cache.VariableCache;
import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.FunctionLibrary;
import com.beassolution.rule.model.RuleHelper;
import com.beassolution.rule.model.RuleLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RuleEngineManager class.
 * 
 * <p>This test class provides comprehensive coverage for the RuleEngineManager
 * including all caching operations and rule compilation scenarios:
 * <ul>
 *   <li>Helper caching operations</li>
 *   <li>Function caching operations</li>
 *   <li>Rule compilation and caching</li>
 *   <li>Error handling scenarios</li>
 *   <li>Edge cases and validation</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class RuleEngineManagerTest {

    @Mock
    private InstanceCache helperCache;

    @Mock
    private RuleCache ruleCache;

    @Mock
    private FunctionCache functionCache;

    @Mock
    private VariableCache variableCache;

    @Mock
    private InstanceInitiator instanceInitiator;

    @InjectMocks
    private RuleEngineManager ruleEngineManager;

    private RuleHelper testHelper;
    private FunctionLibrary testFunction;
    private RuleLibrary testRule;

    /**
     * Sets up test data before each test.
     * 
     * <p>This method initializes common test objects that are used
     * across multiple test methods.
     */
    @BeforeEach
    void setUp() {
        // Create test helper
        testHelper = new RuleHelper();
        testHelper.setName("testHelper");
        testHelper.setPackageUrl("/path/to/helper.jar");
        testHelper.setPackagePath("com.test.TestHelper");

        // Create test function
        testFunction = new FunctionLibrary();
        testFunction.setName("testFunction");
        testFunction.setMvlCode("function testFunction() { return 'test'; }");

        // Create test rule
        testRule = new RuleLibrary();
        testRule.setName("testRule");
        testRule.setMvlCode("return 'Hello ' + payload;");
        testRule.setHelpers(Arrays.asList("testHelper"));
        testRule.setFunctions(Arrays.asList("testFunction"));
    }

    /**
     * Tests successful caching of helper instances.
     * 
     * <p>This test verifies that helper instances are properly cached
     * and the cache is cleared before new helpers are loaded.
     */
    @Test
    @DisplayName("Should successfully cache helper instances")
    void testCacheHelpers() {
        // Given: Mock helper instances
        Map<String, Object> helperInstances = new HashMap<>();
        helperInstances.put("instance1", new Object());
        helperInstances.put("instance2", new Object());

        when(instanceInitiator.create(testHelper)).thenReturn(helperInstances);
        doNothing().when(helperCache).clear();
        doNothing().when(helperCache).put(anyString(), any());

        // When: Cache helpers
        ruleEngineManager.cacheHelpers(Arrays.asList(testHelper));

        // Then: Verify cache operations
        verify(helperCache, times(1)).clear();
        verify(instanceInitiator, times(1)).create(testHelper);
        verify(helperCache, times(1)).put("testHelper", helperInstances);
    }

    /**
     * Tests caching of multiple helper instances.
     * 
     * <p>This test verifies that multiple helpers can be cached
     * successfully in a single operation.
     */
    @Test
    @DisplayName("Should cache multiple helper instances")
    void testCacheMultipleHelpers() {
        // Given: Multiple test helpers
        RuleHelper helper1 = new RuleHelper();
        helper1.setName("helper1");
        helper1.setPackageUrl("/path/to/helper1.jar");
        helper1.setPackagePath("com.test.Helper1");

        RuleHelper helper2 = new RuleHelper();
        helper2.setName("helper2");
        helper2.setPackageUrl("/path/to/helper2.jar");
        helper2.setPackagePath("com.test.Helper2");

        Map<String, Object> instances1 = new HashMap<>();
        instances1.put("instance1", new Object());
        Map<String, Object> instances2 = new HashMap<>();
        instances2.put("instance2", new Object());

        when(instanceInitiator.create(helper1)).thenReturn(instances1);
        when(instanceInitiator.create(helper2)).thenReturn(instances2);
        doNothing().when(helperCache).clear();
        doNothing().when(helperCache).put(anyString(), any());

        // When: Cache multiple helpers
        ruleEngineManager.cacheHelpers(Arrays.asList(helper1, helper2));

        // Then: Verify all helpers were processed
        verify(helperCache, times(1)).clear();
        verify(instanceInitiator, times(1)).create(helper1);
        verify(instanceInitiator, times(1)).create(helper2);
        verify(helperCache, times(1)).put("helper1", instances1);
        verify(helperCache, times(1)).put("helper2", instances2);
    }

    /**
     * Tests error handling when helpers list is null.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when null or empty helper lists are provided.
     */
    @Test
    @DisplayName("Should throw exception when helpers list is null")
    void testCacheHelpersWithNullList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheHelpers(null);
        });
    }

    /**
     * Tests error handling when helpers list is empty.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when an empty helper list is provided.
     */
    @Test
    @DisplayName("Should throw exception when helpers list is empty")
    void testCacheHelpersWithEmptyList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheHelpers(Arrays.asList());
        });
    }

    /**
     * Tests successful caching of function libraries.
     * 
     * <p>This test verifies that function libraries are properly cached
     * and the cache is cleared before new functions are loaded.
     */
    @Test
    @DisplayName("Should successfully cache function libraries")
    void testCacheFunctions() {
        // Given: Mock cache operations
        doNothing().when(functionCache).clear();
        doNothing().when(functionCache).put(anyString(), anyString());

        // When: Cache functions
        ruleEngineManager.cacheFunctions(Arrays.asList(testFunction));

        // Then: Verify cache operations
        verify(functionCache, times(1)).clear();
        verify(functionCache, times(1)).put("testFunction", testFunction.getMvlCode());
    }

    /**
     * Tests caching of multiple function libraries.
     * 
     * <p>This test verifies that multiple functions can be cached
     * successfully in a single operation.
     */
    @Test
    @DisplayName("Should cache multiple function libraries")
    void testCacheMultipleFunctions() {
        // Given: Multiple test functions
        FunctionLibrary function1 = new FunctionLibrary();
        function1.setName("function1");
        function1.setMvlCode("function function1() { return 'test1'; }");

        FunctionLibrary function2 = new FunctionLibrary();
        function2.setName("function2");
        function2.setMvlCode("function function2() { return 'test2'; }");

        doNothing().when(functionCache).clear();
        doNothing().when(functionCache).put(anyString(), anyString());

        // When: Cache multiple functions
        ruleEngineManager.cacheFunctions(Arrays.asList(function1, function2));

        // Then: Verify all functions were processed
        verify(functionCache, times(1)).clear();
        verify(functionCache, times(1)).put("function1", function1.getMvlCode());
        verify(functionCache, times(1)).put("function2", function2.getMvlCode());
    }

    /**
     * Tests error handling when functions list is null.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when null function lists are provided.
     */
    @Test
    @DisplayName("Should throw exception when functions list is null")
    void testCacheFunctionsWithNullList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheFunctions(null);
        });
    }

    /**
     * Tests error handling when functions list is empty.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when empty function lists are provided.
     */
    @Test
    @DisplayName("Should throw exception when functions list is empty")
    void testCacheFunctionsWithEmptyList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheFunctions(Arrays.asList());
        });
    }

    /**
     * Tests successful caching of rules with helpers and functions.
     * 
     * <p>This test verifies that rules are properly compiled and cached
     * with their associated helpers and functions.
     */
    @Test
    @DisplayName("Should successfully cache rules with helpers and functions")
    void testCacheRules() {
        // Given: Mock cache operations and helper instances
        Map<String, Object> helperInstances = new HashMap<>();
        helperInstances.put("instance1", new Object());

        when(helperCache.get("testHelper")).thenReturn(java.util.Optional.of(helperInstances));
        when(functionCache.get("testFunction")).thenReturn(java.util.Optional.of(testFunction.getMvlCode()));
        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache rules
        ruleEngineManager.cacheRules(Arrays.asList(testRule));

        // Then: Verify cache operations
        verify(ruleCache, times(1)).clear();
        verify(helperCache, times(1)).get("testHelper");
        verify(functionCache, times(1)).get("testFunction");
        verify(ruleCache, times(1)).put("testRule", any());
        verify(variableCache, times(1)).put("testRule", any());
    }

    /**
     * Tests caching of rules without helpers or functions.
     * 
     * <p>This test verifies that rules can be cached successfully
     * even when they don't have any helpers or functions.
     */
    @Test
    @DisplayName("Should cache rules without helpers or functions")
    void testCacheRulesWithoutHelpersAndFunctions() {
        // Given: Rule without helpers or functions
        RuleLibrary simpleRule = new RuleLibrary();
        simpleRule.setName("simpleRule");
        simpleRule.setMvlCode("return 'Simple rule';");
        simpleRule.setHelpers(Arrays.asList());
        simpleRule.setFunctions(Arrays.asList());

        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache simple rule
        ruleEngineManager.cacheRules(Arrays.asList(simpleRule));

        // Then: Verify cache operations
        verify(ruleCache, times(1)).clear();
        verify(ruleCache, times(1)).put("simpleRule", any());
        verify(variableCache, times(1)).put("simpleRule", any());
    }

    /**
     * Tests caching of rules with missing helpers.
     * 
     * <p>This test verifies that rules can be cached even when
     * some referenced helpers are not found in the cache.
     */
    @Test
    @DisplayName("Should cache rules with missing helpers")
    void testCacheRulesWithMissingHelpers() {
        // Given: Rule with missing helper
        when(helperCache.get("missingHelper")).thenReturn(java.util.Optional.empty());
        when(functionCache.get("testFunction")).thenReturn(java.util.Optional.of(testFunction.getMvlCode()));
        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache rule with missing helper
        ruleEngineManager.cacheRules(Arrays.asList(testRule));

        // Then: Verify rule is still cached despite missing helper
        verify(ruleCache, times(1)).put("testRule", any());
        verify(variableCache, times(1)).put("testRule", any());
    }

    /**
     * Tests caching of rules with missing functions.
     * 
     * <p>This test verifies that rules can be cached even when
     * some referenced functions are not found in the cache.
     */
    @Test
    @DisplayName("Should cache rules with missing functions")
    void testCacheRulesWithMissingFunctions() {
        // Given: Rule with missing function
        Map<String, Object> helperInstances = new HashMap<>();
        helperInstances.put("instance1", new Object());

        when(helperCache.get("testHelper")).thenReturn(java.util.Optional.of(helperInstances));
        when(functionCache.get("missingFunction")).thenReturn(java.util.Optional.empty());
        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache rule with missing function
        ruleEngineManager.cacheRules(Arrays.asList(testRule));

        // Then: Verify rule is still cached despite missing function
        verify(ruleCache, times(1)).put("testRule", any());
        verify(variableCache, times(1)).put("testRule", any());
    }

    /**
     * Tests caching of multiple rules.
     * 
     * <p>This test verifies that multiple rules can be cached
     * successfully in a single operation.
     */
    @Test
    @DisplayName("Should cache multiple rules")
    void testCacheMultipleRules() {
        // Given: Multiple test rules
        RuleLibrary rule1 = new RuleLibrary();
        rule1.setName("rule1");
        rule1.setMvlCode("return 'Rule 1';");
        rule1.setHelpers(Arrays.asList());
        rule1.setFunctions(Arrays.asList());

        RuleLibrary rule2 = new RuleLibrary();
        rule2.setName("rule2");
        rule2.setMvlCode("return 'Rule 2';");
        rule2.setHelpers(Arrays.asList());
        rule2.setFunctions(Arrays.asList());

        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache multiple rules
        ruleEngineManager.cacheRules(Arrays.asList(rule1, rule2));

        // Then: Verify all rules were processed
        verify(ruleCache, times(1)).clear();
        verify(ruleCache, times(1)).put("rule1", any());
        verify(ruleCache, times(1)).put("rule2", any());
        verify(variableCache, times(1)).put("rule1", any());
        verify(variableCache, times(1)).put("rule2", any());
    }

    /**
     * Tests error handling when rules list is null.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when null rule lists are provided.
     */
    @Test
    @DisplayName("Should throw exception when rules list is null")
    void testCacheRulesWithNullList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheRules(null);
        });
    }

    /**
     * Tests error handling when rules list is empty.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when empty rule lists are provided.
     */
    @Test
    @DisplayName("Should throw exception when rules list is empty")
    void testCacheRulesWithEmptyList() {
        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            ruleEngineManager.cacheRules(Arrays.asList());
        });
    }

    /**
     * Tests caching of rules with empty helper names.
     * 
     * <p>This test verifies that rules can be cached successfully
     * even when some helper names are empty strings.
     */
    @Test
    @DisplayName("Should handle empty helper names gracefully")
    void testCacheRulesWithEmptyHelperNames() {
        // Given: Rule with empty helper name
        RuleLibrary ruleWithEmptyHelper = new RuleLibrary();
        ruleWithEmptyHelper.setName("ruleWithEmptyHelper");
        ruleWithEmptyHelper.setMvlCode("return 'Rule with empty helper';");
        ruleWithEmptyHelper.setHelpers(Arrays.asList("", "validHelper"));
        ruleWithEmptyHelper.setFunctions(Arrays.asList());

        Map<String, Object> helperInstances = new HashMap<>();
        helperInstances.put("instance1", new Object());

        when(helperCache.get("validHelper")).thenReturn(java.util.Optional.of(helperInstances));
        doNothing().when(ruleCache).clear();
        doNothing().when(ruleCache).put(anyString(), any());
        doNothing().when(variableCache).put(anyString(), any());

        // When: Cache rule with empty helper name
        ruleEngineManager.cacheRules(Arrays.asList(ruleWithEmptyHelper));

        // Then: Verify rule is cached and only valid helper is processed
        verify(ruleCache, times(1)).put("ruleWithEmptyHelper", any());
        verify(helperCache, times(1)).get("validHelper");
        verify(helperCache, never()).get("");
    }
} 