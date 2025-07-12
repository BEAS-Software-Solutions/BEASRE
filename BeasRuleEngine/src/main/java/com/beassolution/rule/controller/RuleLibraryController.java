package com.beassolution.rule.controller;

import com.beassolution.rule.controller.base.CreateController;
import com.beassolution.rule.controller.base.DeleteController;
import com.beassolution.rule.controller.base.ReadController;
import com.beassolution.rule.controller.base.UpdateController;
import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.RuleLibrary;
import com.beassolution.rule.service.RuleLibraryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * REST controller for rule library operations.
 * 
 * <p>This controller provides CRUD operations for rule libraries in the
 * Beas Rule Engine. It manages the creation, reading, updating, and deletion
 * of rule library entities.
 * 
 * <p>Key operations include:
 * <ul>
 *   <li>Create new rule libraries</li>
 *   <li>Retrieve rule libraries with pagination and filtering</li>
 *   <li>Update existing rule libraries</li>
 *   <li>Delete rule libraries</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Validated
@RestController("rulelibrary")
@RequestMapping(name = "rulelibrary", path = "/rule-library")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PATCH})
@Tag(name = "Rule Library", description = "Endpoints for managing rule libraries")
public class RuleLibraryController implements CreateController<RuleLibrary>, ReadController<RuleLibrary>, UpdateController<RuleLibrary>, DeleteController<RuleLibrary> {
    
    /**
     * Service for rule library operations.
     */
    private final RuleLibraryService service;

    /**
     * Creates a new rule library.
     * 
     * @param obj The rule library to create
     * @return ResponseEntity containing the created rule library
     * @throws OperationException if creation fails
     */
    @Override
    public ResponseEntity<RuleLibrary> create(RuleLibrary obj) throws OperationException {
        return new ResponseEntity<>(service.create(obj), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of rule libraries with optional filtering.
     * 
     * @param rsql RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return ResponseEntity containing a page of rule libraries
     * @throws OperationException if retrieval fails
     */
    @Override
    public ResponseEntity<Page<RuleLibrary>> read(String rsql, Pageable pageable) throws OperationException {
        return new ResponseEntity<>(service.read(rsql, pageable), HttpStatus.OK);
    }

    /**
     * Retrieves a single rule library by its ID.
     * 
     * @param id The unique identifier of the rule library
     * @return ResponseEntity containing the found rule library
     * @throws OperationException if rule library is not found or retrieval fails
     */
    @Override
    public ResponseEntity<RuleLibrary> read(String id) throws OperationException {
        return new ResponseEntity<>(service.findById(UUID.fromString(id)), HttpStatus.OK);
    }

    /**
     * Updates an existing rule library.
     * 
     * @param obj The rule library with updated values
     * @return ResponseEntity containing the updated rule library
     * @throws OperationException if update fails or rule library not found
     */
    @Override
    public ResponseEntity<RuleLibrary> update(RuleLibrary obj) throws OperationException {
        return new ResponseEntity<>(service.update(obj), HttpStatus.OK);
    }

    /**
     * Deletes a rule library by its ID.
     * 
     * @param obj The unique identifier of the rule library to delete
     * @return ResponseEntity containing the HTTP status response
     * @throws OperationException if deletion fails or rule library not found
     */
    @Override
    public ResponseEntity<HttpResponse<HttpStatus>> delete(String obj) throws OperationException {
        UUID RuleLibraryId=UUID.fromString(obj);
        service.delete(RuleLibraryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
