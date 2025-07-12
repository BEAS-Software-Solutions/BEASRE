package com.beassolution.rule.controller.base;

import com.beassolution.rule.exception.OperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.http.HttpResponse;

/**
 * Base interface for delete operations in REST controllers.
 * 
 * <p>This interface defines the standard contract for deleting entities
 * in the Beas Rule Engine. It provides a consistent API structure for
 * DELETE operations across all entity types.
 * 
 * <p>The interface includes:
 * <ul>
 *   <li>Standard DELETE endpoint mapping</li>
 *   <li>Consistent response structure</li>
 *   <li>Exception handling</li>
 * </ul>
 * 
 * @param <T> The type of entity to delete
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface DeleteController<T> {
    
    /**
     * Deletes an entity by its ID.
     * 
     * <p>This method handles the deletion of an entity by its unique identifier.
     * The entity is permanently removed from the database. The operation
     * returns a success status if the deletion is completed successfully.
     * 
     * @param obj The unique identifier of the entity to delete
     * @return ResponseEntity containing the HTTP status response
     * @throws OperationException if deletion fails or entity not found
     */
    @DeleteMapping(path = "/delete/{record_id}")
    @Operation(summary = "Delete entity", description = "Deletes an entity by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error during deletion")
    })
    ResponseEntity<HttpResponse<HttpStatus>> delete(@Parameter(description = "Unique identifier of the entity to delete") 
                                                   @PathVariable("record_id") String obj) throws OperationException;
}
