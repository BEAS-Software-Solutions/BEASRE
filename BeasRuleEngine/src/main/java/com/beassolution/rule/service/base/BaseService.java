package com.beassolution.rule.service.base;

import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.base.BaseModel;
import com.beassolution.rule.rsql.EntityRSQLVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

/**
 * Base service implementation for CRUD operations.
 * 
 * <p>This class provides a concrete implementation of the BaseCrudOperation
 * interface for all entity services in the Beas Rule Engine. It handles
 * common CRUD operations using MongoDB and includes support for RSQL querying.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Standard CRUD operations</li>
 *   <li>RSQL query parsing and execution</li>
 *   <li>Object mapping with ModelMapper</li>
 *   <li>Pagination support</li>
 *   <li>Exception handling</li>
 * </ul>
 * 
 * @param <T> The type of entity to manage
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class BaseService<T extends BaseModel> implements BaseCrudOperation<T> {
    
    /**
     * RSQL parser for query parsing.
     */
    private final RSQLParser rsqlParser;
    
    /**
     * MongoDB repository for entity operations.
     */
    private final MongoRepository<T, UUID> repository;
    
    /**
     * Model mapper for object mapping.
     */
    private final ModelMapper modelMapper;
    
    /**
     * MongoDB template for custom queries.
     */
    private final MongoTemplate mongoTemplate;
    
    /**
     * Class of the entity type.
     */
    private final Class<T> entityClass;

    /**
     * Finds an entity by its unique identifier.
     * 
     * @param id The unique identifier of the entity
     * @return The found entity
     * @throws OperationException if entity is not found
     */
    public T findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new OperationException("Entity not found with ID: " + id, HttpStatus.NOT_FOUND));
    }

    /**
     * Creates a new entity.
     * 
     * @param obj The entity to create
     * @return The created entity
     * @throws OperationException if creation fails
     */
    public T create(T obj) throws OperationException {
        try {
            return repository.save(obj);
        } catch (Exception e) {
            throw new OperationException("Failed to create entity: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a paginated list of entities with optional filtering.
     * 
     * @param restQuery RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return Page of entities matching the criteria
     * @throws OperationException if retrieval fails
     */
    public Page<T> read(String restQuery, Pageable pageable) throws OperationException {
        try {
            if (restQuery == null || restQuery.trim().isEmpty()) {
                return repository.findAll(pageable);
            }

            Node rootNode = rsqlParser.parse(restQuery);
            Query query = rootNode.accept(new EntityRSQLVisitor(), new Query());
            List<T> results = mongoTemplate.find(query.with(pageable), entityClass);
            
            return new PageImpl<>(results, pageable, results.size());
        } catch (Exception e) {
            throw new OperationException("Failed to retrieve entities: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing entity.
     * 
     * @param obj The entity with updated values
     * @return The updated entity
     * @throws OperationException if update fails or entity not found
     */
    public T update(T obj) throws OperationException {
        try {
            T currentObject = repository.findById(obj.getId())
                .orElseThrow(() -> new OperationException("Entity not found with ID: " + obj.getId(), HttpStatus.NOT_FOUND));
            
            modelMapper.map(obj, currentObject);
            return repository.save(currentObject);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException("Failed to update entity: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes an entity by its unique identifier.
     * 
     * @param id The unique identifier of the entity to delete
     * @throws OperationException if deletion fails or entity not found
     */
    public void delete(UUID id) throws OperationException {
        try {
            T currentObject = repository.findById(id)
                .orElseThrow(() -> new OperationException("Entity not found with ID: " + id, HttpStatus.NOT_FOUND));
            
            repository.delete(currentObject);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException("Failed to delete entity: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Counts the total number of entities.
     * 
     * @return The total count of entities
     */
    public long count() {
        return repository.count();
    }
}
