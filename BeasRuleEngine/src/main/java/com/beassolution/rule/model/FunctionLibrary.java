package com.beassolution.rule.model;

import com.beassolution.rule.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Entity representing a function library in the Beas Rule Engine.
 * 
 * <p>This class represents a collection of utility functions that can be used
 * within rule logic. Function libraries contain MVEL code that defines reusable
 * functions for common operations and calculations.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Function library name and description</li>
 *   <li>MVEL code containing function definitions</li>
 *   <li>Category for organization</li>
 *   <li>Container name for grouping</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class FunctionLibrary extends BaseModel implements Serializable {
    
    /**
     * Name of the function library.
     * 
     * <p>This field contains a unique identifier for the function library
     * that is used for lookup and inclusion purposes.
     */
    private String name;
    
    /**
     * Description of the function library.
     * 
     * <p>This field provides a human-readable description of what the
     * function library contains and its intended purpose.
     */
    private String description;
    
    /**
     * MVEL code containing function definitions.
     * 
     * <p>This field contains the actual MVEL code that defines utility functions.
     * The functions are compiled and made available for use within rule logic.
     */
    private String mvlCode;
    
    /**
     * Category for organizing function libraries.
     * 
     * <p>This field is used to categorize function libraries by their purpose
     * or domain, such as "mathematical", "string", "date", etc.
     */
    private String category;
    
    /**
     * Container name for organizing function libraries.
     * 
     * <p>This field is used to group related function libraries together
     * and can be used for filtering and organization purposes.
     */
    private String containerName;

}
