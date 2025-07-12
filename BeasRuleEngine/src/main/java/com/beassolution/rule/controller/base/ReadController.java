package com.beassolution.rule.controller.base;

import com.beassolution.rule.exception.OperationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Base interface for read operations in REST controllers.
 * 
 * <p>This interface defines the standard contract for reading entities
 * in the Beas Rule Engine. It provides a consistent API structure for
 * GET operations across all entity types, including pagination and filtering.
 * 
 * <p>The interface includes:
 * <ul>
 *   <li>Standard GET endpoint mappings</li>
 *   <li>Pagination support</li>
 *   <li>RSQL query filtering</li>
 *   <li>Individual entity retrieval</li>
 * </ul>
 * 
 * @param <T> The type of entity to read
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public interface ReadController<T> {
    
    /**
     * Retrieves a paginated list of entities with optional filtering.
     * 
     * <p>This method handles the retrieval of entities with support for
     * pagination and RSQL query filtering. The RSQL query allows for
     * complex filtering, sorting, and selection operations.
     * 
     * @param rsql RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return ResponseEntity containing a page of entities
     * @throws OperationException if retrieval fails
     */
    @GetMapping(path = "/read")
    @Operation(summary = "Read entities", description = "Retrieves a paginated list of entities with optional RSQL filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entities retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid query parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error during retrieval")
    })
    ResponseEntity<Page<T>> read(@Parameter(description = "RSQL query string for filtering and sorting") 
                                 @RequestParam(value = "search", required = false) String rsql, 
                                 Pageable pageable) throws OperationException;

    /**
     * Retrieves a single entity by its ID.
     * 
     * <p>This method handles the retrieval of a specific entity by its
     * unique identifier. The ID is provided as a path variable.
     * 
     * @param id The unique identifier of the entity
     * @return ResponseEntity containing the found entity
     * @throws OperationException if entity is not found or retrieval fails
     */
    @GetMapping(path = "/read/{id}")
    @Operation(summary = "Read entity by ID", description = "Retrieves a single entity by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
        @ApiResponse(responseCode = "404", description = "Entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error during retrieval")
    })
    ResponseEntity<T> read(@Parameter(description = "Unique identifier of the entity") 
                          @PathVariable(value = "id", required = true) String id) throws OperationException;
}
