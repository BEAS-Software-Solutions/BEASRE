package com.beassolution.rule.service.base;

import com.beassolution.rule.exception.OperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.UUID;

/**
 * Base interface for CRUD operations in service layer.
 * 
 * <p>This interface defines the standard contract for CRUD (Create, Read, Update, Delete)
 * operations in the Beas Rule Engine service layer. It provides a consistent API
 * structure for all entity services.
 * 
 * <p>The interface includes:
 * <ul>
 *   <li>Entity creation</li>
 *   <li>Entity retrieval by ID</li>
 *   <li>Paginated entity retrieval with filtering</li>
 *   <li>Entity updates</li>
 *   <li>Entity deletion</li>
 * </ul>
 * 
 * @param <T> The type of entity to manage
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface BaseCrudOperation<T> {
    
    /**
     * Finds an entity by its unique identifier.
     * 
     * <p>This method retrieves a single entity from the database using its UUID.
     * If the entity is not found, an OperationException is thrown.
     * 
     * @param id The unique identifier of the entity
     * @return The found entity
     * @throws OperationException if entity is not found
     */
    T findById(UUID id);

    /**
     * Creates a new entity.
     * 
     * <p>This method persists a new entity to the database. The entity
     * is validated before persistence and audit fields are automatically
     * populated.
     * 
     * @param obj The entity to create
     * @return The created entity with generated ID and audit fields
     * @throws OperationException if creation fails
     */
    T create(T obj) throws OperationException;

    /**
     * Retrieves a paginated list of entities with optional filtering.
     * 
     * <p>This method retrieves entities from the database with support for
     * pagination and RSQL query filtering. The RSQL query allows for complex
     * filtering, sorting, and selection operations.
     * 
     * @param rsql RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return Page of entities matching the criteria
     * @throws OperationException if retrieval fails
     */
    Page<T> read(String rsql, @PageableDefault Pageable pageable) throws OperationException;

    /**
     * Updates an existing entity.
     * 
     * <p>This method updates an existing entity in the database. Only the
     * provided fields are updated, maintaining existing values for non-specified
     * fields. The entity must exist in the database.
     * 
     * @param obj The entity with updated values
     * @return The updated entity
     * @throws OperationException if update fails or entity not found
     */
    T update(T obj) throws OperationException;

    /**
     * Deletes an entity by its unique identifier.
     * 
     * <p>This method permanently removes an entity from the database using
     * its UUID. The entity must exist in the database.
     * 
     * @param obj The unique identifier of the entity to delete
     * @throws OperationException if deletion fails or entity not found
     */
    void delete(UUID obj) throws OperationException;
}
