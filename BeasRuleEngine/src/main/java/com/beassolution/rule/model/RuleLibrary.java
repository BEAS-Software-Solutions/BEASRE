package com.beassolution.rule.model;

import com.beassolution.rule.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Entity representing a rule library in the Beas Rule Engine.
 * 
 * <p>This class represents a collection of rules that can be executed together.
 * A rule library contains the main rule logic (MVEL code) along with references
 * to helper classes and functions that support the rule execution.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Rule name and description</li>
 *   <li>MVEL code for rule logic</li>
 *   <li>References to helper classes</li>
 *   <li>References to function libraries</li>
 *   <li>Container name for organization</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class RuleLibrary extends BaseModel implements Serializable {
    
    /**
     * Name of the rule library.
     * 
     * <p>This field contains a unique identifier for the rule library
     * that is used for lookup and execution purposes.
     */
    private String name;
    
    /**
     * Description of the rule library.
     * 
     * <p>This field provides a human-readable description of what the
     * rule library does and its intended purpose.
     */
    private String description;
    
    /**
     * MVEL code for the rule logic.
     * 
     * <p>This field contains the actual rule logic written in MVEL (MVFLEX Expression Language).
     * The code is compiled and executed when the rule is evaluated.
     */
    private String mvlCode;
    
    /**
     * List of function library names referenced by this rule.
     * 
     * <p>This field contains the names of function libraries that provide
     * utility functions used within the rule logic.
     */
    private List<String> functions;
    
    /**
     * List of helper class names referenced by this rule.
     * 
     * <p>This field contains the names of helper classes that provide
     * additional functionality and business logic for the rule.
     */
    private List<String> helpers;
    
    /**
     * Container name for organizing rule libraries.
     * 
     * <p>This field is used to group related rule libraries together
     * and can be used for filtering and organization purposes.
     */
    private String containerName;

}
