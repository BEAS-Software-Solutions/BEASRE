package com.beassolution.rule.controller.base;

import com.beassolution.rule.exception.OperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Base interface for update operations in REST controllers.
 * 
 * <p>This interface defines the standard contract for updating entities
 * in the Beas Rule Engine. It provides a consistent API structure for
 * PATCH operations across all entity types.
 * 
 * <p>The interface includes:
 * <ul>
 *   <li>Standard PATCH endpoint mapping</li>
 *   <li>Consistent response structure</li>
 *   <li>Exception handling</li>
 * </ul>
 * 
 * @param <T> The type of entity to update
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface UpdateController<T> {
    
    /**
     * Updates an existing entity.
     * 
     * <p>This method handles the update of an existing entity instance.
     * The entity is validated and the changes are persisted to the database.
     * Only the provided fields are updated, maintaining existing values for
     * non-specified fields.
     * 
     * @param obj The entity object with updated values
     * @return ResponseEntity containing the updated entity
     * @throws OperationException if update fails or entity not found
     */
    @PatchMapping(path = "/update")
    @Operation(summary = "Update entity", description = "Updates an existing entity with partial data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity updated successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
        @ApiResponse(responseCode = "400", description = "Invalid entity data"),
        @ApiResponse(responseCode = "404", description = "Entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error during update")
    })
    ResponseEntity<T> update(@Parameter(description = "Entity object with updated values") @RequestBody T obj) throws OperationException;
}
