package com.beassolution.rule.service.base;

import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.base.BaseModel;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BaseService class.
 * 
 * <p>This test class provides comprehensive coverage for the BaseService
 * including all CRUD operations and RSQL querying:
 * <ul>
 *   <li>Create, Read, Update, Delete operations</li>
 *   <li>RSQL query parsing and execution</li>
 *   <li>Pagination support</li>
 *   <li>Error handling scenarios</li>
 *   <li>Edge cases and validation</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

    @Mock
    private RSQLParser rsqlParser;

    @Mock
    private MongoRepository<TestEntity, UUID> repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private Node mockNode;

    @InjectMocks
    private TestBaseService baseService;

    private TestEntity testEntity;
    private UUID testId;

    /**
     * Sets up test data before each test.
     * 
     * <p>This method initializes common test objects that are used
     * across multiple test methods.
     */
    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testEntity = new TestEntity();
        testEntity.setId(testId);
        testEntity.setName("Test Entity");
        testEntity.setDescription("Test Description");
    }

    /**
     * Tests successful entity creation.
     * 
     * <p>This test verifies that entities can be created successfully
     * and the repository save method is called.
     */
    @Test
    @DisplayName("Should successfully create entity")
    void testCreate() throws OperationException {
        // Given: Mock repository save operation
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When: Create entity
        TestEntity result = baseService.create(testEntity);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * Tests entity creation failure.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when entity creation fails.
     */
    @Test
    @DisplayName("Should throw exception when creation fails")
    void testCreateFailure() {
        // Given: Mock repository save to throw exception
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Save failed"));

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.create(testEntity);
        });
    }

    /**
     * Tests successful entity retrieval by ID.
     * 
     * <p>This test verifies that entities can be retrieved successfully
     * by their unique identifier.
     */
    @Test
    @DisplayName("Should successfully find entity by ID")
    void testFindById() {
        // Given: Mock repository findById operation
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When: Find entity by ID
        TestEntity result = baseService.findById(testId);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getName(), result.getName());
        verify(repository, times(1)).findById(testId);
    }

    /**
     * Tests entity retrieval failure when entity not found.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when an entity is not found by ID.
     */
    @Test
    @DisplayName("Should throw exception when entity not found by ID")
    void testFindByIdNotFound() {
        // Given: Mock repository findById to return empty
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.findById(testId);
        });
    }

    /**
     * Tests successful entity reading with pagination.
     * 
     * <p>This test verifies that entities can be retrieved successfully
     * with pagination support.
     */
    @Test
    @DisplayName("Should successfully read entities with pagination")
    void testReadWithPagination() throws OperationException {
        // Given: Mock repository findAll with pagination
        Pageable pageable = PageRequest.of(0, 10);
        List<TestEntity> entities = Arrays.asList(testEntity);
        Page<TestEntity> expectedPage = new PageImpl<>(entities, pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(expectedPage);

        // When: Read entities with pagination
        Page<TestEntity> result = baseService.read(null, pageable);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testEntity.getId(), result.getContent().get(0).getId());
        verify(repository, times(1)).findAll(pageable);
    }

    /**
     * Tests successful entity reading with RSQL query.
     * 
     * <p>This test verifies that entities can be retrieved successfully
     * using RSQL query strings.
     */
    @Test
    @DisplayName("Should successfully read entities with RSQL query")
    void testReadWithRSQLQuery() throws OperationException {
        // Given: Mock RSQL parsing and MongoDB query execution
        String rsqlQuery = "name==Test";
        Pageable pageable = PageRequest.of(0, 10);
        List<TestEntity> entities = Arrays.asList(testEntity);
        
        when(rsqlParser.parse(rsqlQuery)).thenReturn(mockNode);
        when(mongoTemplate.find(any(Query.class), eq(TestEntity.class))).thenReturn(entities);

        // When: Read entities with RSQL query
        Page<TestEntity> result = baseService.read(rsqlQuery, pageable);

        // Then: Verify result and interactions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testEntity.getId(), result.getContent().get(0).getId());
        verify(rsqlParser, times(1)).parse(rsqlQuery);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(TestEntity.class));
    }

    /**
     * Tests entity reading with empty RSQL query.
     * 
     * <p>This test verifies that empty or null RSQL queries are handled
     * gracefully and fall back to findAll.
     */
    @Test
    @DisplayName("Should handle empty RSQL query")
    void testReadWithEmptyRSQLQuery() throws OperationException {
        // Given: Mock repository findAll with pagination
        Pageable pageable = PageRequest.of(0, 10);
        List<TestEntity> entities = Arrays.asList(testEntity);
        Page<TestEntity> expectedPage = new PageImpl<>(entities, pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(expectedPage);

        // When: Read entities with empty RSQL query
        Page<TestEntity> result = baseService.read("", pageable);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    /**
     * Tests entity reading with whitespace RSQL query.
     * 
     * <p>This test verifies that RSQL queries containing only whitespace
     * are handled gracefully.
     */
    @Test
    @DisplayName("Should handle whitespace RSQL query")
    void testReadWithWhitespaceRSQLQuery() throws OperationException {
        // Given: Mock repository findAll with pagination
        Pageable pageable = PageRequest.of(0, 10);
        List<TestEntity> entities = Arrays.asList(testEntity);
        Page<TestEntity> expectedPage = new PageImpl<>(entities, pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(expectedPage);

        // When: Read entities with whitespace RSQL query
        Page<TestEntity> result = baseService.read("   ", pageable);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    /**
     * Tests successful entity update.
     * 
     * <p>This test verifies that entities can be updated successfully
     * and the repository operations are called correctly.
     */
    @Test
    @DisplayName("Should successfully update entity")
    void testUpdate() throws OperationException {
        // Given: Mock repository operations
        TestEntity updatedEntity = new TestEntity();
        updatedEntity.setId(testId);
        updatedEntity.setName("Updated Name");
        updatedEntity.setDescription("Updated Description");
        
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenReturn(updatedEntity);
        doNothing().when(modelMapper).map(any(TestEntity.class), any(TestEntity.class));

        // When: Update entity
        TestEntity result = baseService.update(updatedEntity);

        // Then: Verify result and repository interactions
        assertNotNull(result);
        assertEquals(updatedEntity.getId(), result.getId());
        verify(repository, times(1)).findById(testId);
        verify(modelMapper, times(1)).map(updatedEntity, testEntity);
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * Tests entity update failure when entity not found.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when trying to update a non-existent entity.
     */
    @Test
    @DisplayName("Should throw exception when updating non-existent entity")
    void testUpdateNotFound() {
        // Given: Mock repository findById to return empty
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.update(testEntity);
        });
    }

    /**
     * Tests entity update failure during save.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when the save operation fails during update.
     */
    @Test
    @DisplayName("Should throw exception when update save fails")
    void testUpdateSaveFailure() {
        // Given: Mock repository operations
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Save failed"));
        doNothing().when(modelMapper).map(any(TestEntity.class), any(TestEntity.class));

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.update(testEntity);
        });
    }

    /**
     * Tests successful entity deletion.
     * 
     * <p>This test verifies that entities can be deleted successfully
     * and the repository operations are called correctly.
     */
    @Test
    @DisplayName("Should successfully delete entity")
    void testDelete() throws OperationException {
        // Given: Mock repository operations
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        doNothing().when(repository).delete(any(TestEntity.class));

        // When: Delete entity
        baseService.delete(testId);

        // Then: Verify repository interactions
        verify(repository, times(1)).findById(testId);
        verify(repository, times(1)).delete(testEntity);
    }

    /**
     * Tests entity deletion failure when entity not found.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when trying to delete a non-existent entity.
     */
    @Test
    @DisplayName("Should throw exception when deleting non-existent entity")
    void testDeleteNotFound() {
        // Given: Mock repository findById to return empty
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.delete(testId);
        });
    }

    /**
     * Tests entity deletion failure during delete operation.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when the delete operation fails.
     */
    @Test
    @DisplayName("Should throw exception when delete operation fails")
    void testDeleteOperationFailure() {
        // Given: Mock repository operations
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        doThrow(new RuntimeException("Delete failed")).when(repository).delete(any(TestEntity.class));

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.delete(testId);
        });
    }

    /**
     * Tests entity count operation.
     * 
     * <p>This test verifies that the count operation returns
     * the correct number of entities.
     */
    @Test
    @DisplayName("Should successfully count entities")
    void testCount() {
        // Given: Mock repository count operation
        when(repository.count()).thenReturn(5L);

        // When: Count entities
        long result = baseService.count();

        // Then: Verify result and repository interaction
        assertEquals(5L, result);
        verify(repository, times(1)).count();
    }

    /**
     * Tests RSQL query parsing failure.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when RSQL query parsing fails.
     */
    @Test
    @DisplayName("Should throw exception when RSQL parsing fails")
    void testRSQLParsingFailure() {
        // Given: Mock RSQL parser to throw exception
        String invalidQuery = "invalid==query";
        Pageable pageable = PageRequest.of(0, 10);
        
        when(rsqlParser.parse(invalidQuery)).thenThrow(new RuntimeException("Parse failed"));

        // When & Then: Verify exception is thrown
        assertThrows(OperationException.class, () -> {
            baseService.read(invalidQuery, pageable);
        });
    }

    /**
     * Test entity class for testing purposes.
     * 
     * <p>This class provides a concrete implementation of BaseModel
     * for testing the BaseService functionality.
     */
    private static class TestEntity extends BaseModel {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    /**
     * Test implementation of BaseService for testing purposes.
     * 
     * <p>This class provides a concrete implementation of BaseService
     * for testing the base service functionality.
     */
    private static class TestBaseService extends BaseService<TestEntity> {
        public TestBaseService(RSQLParser rsqlParser, MongoRepository<TestEntity, UUID> repository,
                              ModelMapper modelMapper, MongoTemplate mongoTemplate) {
            super(rsqlParser, repository, modelMapper, mongoTemplate, TestEntity.class);
        }
    }
} 