package com.beassolution.rule.controller;

import com.beassolution.rule.controller.base.CreateController;
import com.beassolution.rule.controller.base.DeleteController;
import com.beassolution.rule.controller.base.ReadController;
import com.beassolution.rule.controller.base.UpdateController;
import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.RuleHelper;
import com.beassolution.rule.service.RuleHelperService;
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
 * REST controller for rule helper operations.
 * 
 * <p>This controller provides CRUD operations for rule helpers in the
 * Beas Rule Engine. It manages the creation, reading, updating, and deletion
 * of rule helper entities.
 * 
 * <p>Key operations include:
 * <ul>
 *   <li>Create new rule helpers</li>
 *   <li>Retrieve rule helpers with pagination and filtering</li>
 *   <li>Update existing rule helpers</li>
 *   <li>Delete rule helpers</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Validated
@RestController("rulehelper")
@RequestMapping(name = "rulehelper", path = "/rule-helper")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PATCH})
@Tag(name = "Rule Helper", description = "Endpoints for managing rule helpers")
public class RuleHelperController implements CreateController<RuleHelper>, ReadController<RuleHelper>, UpdateController<RuleHelper>, DeleteController<RuleHelper> {
    
    /**
     * Service for rule helper operations.
     */
    private final RuleHelperService service;

    /**
     * Creates a new rule helper.
     * 
     * @param obj The rule helper to create
     * @return ResponseEntity containing the created rule helper
     * @throws OperationException if creation fails
     */
    @Override
    public ResponseEntity<RuleHelper> create(RuleHelper obj) throws OperationException {
        return new ResponseEntity<>(service.create(obj), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of rule helpers with optional filtering.
     * 
     * @param rsql RSQL query string for filtering and sorting
     * @param pageable Pagination parameters
     * @return ResponseEntity containing a page of rule helpers
     * @throws OperationException if retrieval fails
     */
    @Override
    public ResponseEntity<Page<RuleHelper>> read(String rsql, Pageable pageable) throws OperationException {
        return new ResponseEntity<>(service.read(rsql, pageable), HttpStatus.OK);
    }

    /**
     * Retrieves a single rule helper by its ID.
     * 
     * @param id The unique identifier of the rule helper
     * @return ResponseEntity containing the found rule helper
     * @throws OperationException if rule helper is not found or retrieval fails
     */
    @Override
    public ResponseEntity<RuleHelper> read(String id) throws OperationException {
        return new ResponseEntity<>(service.findById(UUID.fromString(id)), HttpStatus.OK);
    }

    /**
     * Updates an existing rule helper.
     * 
     * @param obj The rule helper with updated values
     * @return ResponseEntity containing the updated rule helper
     * @throws OperationException if update fails or rule helper not found
     */
    @Override
    public ResponseEntity<RuleHelper> update(RuleHelper obj) throws OperationException {
        return new ResponseEntity<>(service.update(obj), HttpStatus.OK);
    }

    /**
     * Deletes a rule helper by its ID.
     * 
     * @param obj The unique identifier of the rule helper to delete
     * @return ResponseEntity containing the HTTP status response
     * @throws OperationException if deletion fails or rule helper not found
     */
    @Override
    public ResponseEntity<HttpResponse<HttpStatus>> delete(String obj) throws OperationException {
        UUID ruleHelperId=UUID.fromString(obj);
        service.delete(ruleHelperId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
