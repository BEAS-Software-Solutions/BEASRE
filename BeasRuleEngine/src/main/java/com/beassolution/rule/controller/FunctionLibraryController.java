package com.beassolution.rule.controller;

import com.beassolution.rule.controller.base.CreateController;
import com.beassolution.rule.controller.base.DeleteController;
import com.beassolution.rule.controller.base.ReadController;
import com.beassolution.rule.controller.base.UpdateController;
import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.FunctionLibrary;
import com.beassolution.rule.service.FunctionLibraryService;
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
 * REST controller for function library operations.
 * 
 * <p>This controller provides CRUD operations for function libraries in the
 * Beas Rule Engine. It manages the creation, reading, updating, and deletion
 * of function library entities.
 * 
 * <p>Key operations include:
 * <ul>
 *   <li>Create new function libraries</li>
 *   <li>Retrieve function libraries with pagination and filtering</li>
 *   <li>Update existing function libraries</li>
 *   <li>Delete function libraries</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Validated
@RestController("functionlibrary")
@RequestMapping(name = "functionlibrary", path = "/function-library")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PATCH})
@Tag(name = "Function Library", description = "Endpoints for managing function libraries")
public class FunctionLibraryController implements CreateController<FunctionLibrary>, ReadController<FunctionLibrary>, UpdateController<FunctionLibrary>, DeleteController<FunctionLibrary> {
    
    /**
     * Service for function library operations.
     */
    private final FunctionLibraryService service;

    /**
     * Creates a new function library.
     * 
     * @param obj The function library to create
     * @return ResponseEntity containing the created function library
     * @throws OperationException if creation fails
     */
    @Override
    public ResponseEntity<FunctionLibrary> create(FunctionLibrary obj) throws OperationException {
        return new ResponseEntity<>(service.create(obj), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of function libraries with optional filtering.
     * 
     * @param rsql RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return ResponseEntity containing a page of function libraries
     * @throws OperationException if retrieval fails
     */
    @Override
    public ResponseEntity<Page<FunctionLibrary>> read(String rsql, Pageable pageable) throws OperationException {
        return new ResponseEntity<>(service.read(rsql, pageable), HttpStatus.OK);
    }

    /**
     * Retrieves a single function library by its ID.
     * 
     * @param id The unique identifier of the function library
     * @return ResponseEntity containing the found function library
     * @throws OperationException if function library is not found or retrieval fails
     */
    @Override
    public ResponseEntity<FunctionLibrary> read(String id) throws OperationException {
        return new ResponseEntity<>(service.findById(UUID.fromString(id)), HttpStatus.OK);
    }

    /**
     * Updates an existing function library.
     * 
     * @param obj The function library with updated values
     * @return ResponseEntity containing the updated function library
     * @throws OperationException if update fails or function library not found
     */
    @Override
    public ResponseEntity<FunctionLibrary> update(FunctionLibrary obj) throws OperationException {
        return new ResponseEntity<>(service.update(obj), HttpStatus.OK);
    }

    /**
     * Deletes a function library by its ID.
     * 
     * @param obj The unique identifier of the function library to delete
     * @return ResponseEntity containing the HTTP status response
     * @throws OperationException if deletion fails or function library not found
     */
    @Override
    public ResponseEntity<HttpResponse<HttpStatus>> delete(String obj) throws OperationException {
        UUID FunctionLibraryId=UUID.fromString(obj);
        service.delete(FunctionLibraryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
