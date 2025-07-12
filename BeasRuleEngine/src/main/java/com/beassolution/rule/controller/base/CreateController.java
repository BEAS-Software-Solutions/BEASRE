package com.beassolution.rule.controller.base;

import com.beassolution.rule.exception.OperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Base interface for create operations in REST controllers.
 * 
 * <p>This interface defines the standard contract for creating new entities
 * in the Beas Rule Engine. It provides a consistent API structure for
 * POST operations across all entity types.
 * 
 * <p>The interface includes:
 * <ul>
 *   <li>Standard POST endpoint mapping</li>
 *   <li>Consistent response structure</li>
 *   <li>Exception handling</li>
 * </ul>
 * 
 * @param <T> The type of entity to create
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface CreateController<T> {
    
    /**
     * Creates a new entity.
     * 
     * <p>This method handles the creation of a new entity instance.
     * The entity is validated and persisted to the database.
     * 
     * @param obj The entity object to create
     * @return ResponseEntity containing the created entity
     * @throws OperationException if creation fails
     */
    @PostMapping(path = "/create")
    @Operation(summary = "Create entity", description = "Creates a new entity instance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entity created successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
        @ApiResponse(responseCode = "400", description = "Invalid entity data"),
        @ApiResponse(responseCode = "500", description = "Internal server error during creation")
    })
    ResponseEntity<T> create(@Parameter(description = "Entity object to create") @RequestBody T obj) throws OperationException;
}
