package com.beassolution.rule.model;

import com.beassolution.rule.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Entity representing a rule helper in the Beas Rule Engine.
 * 
 * <p>This class represents a helper class that provides additional functionality
 * and business logic for rules. Helper classes are loaded dynamically from JAR files
 * and instantiated for use within rule execution.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Helper name and description</li>
 *   <li>Package URL for JAR file location</li>
 *   <li>Package path for class scanning</li>
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
public class RuleHelper extends BaseModel implements Serializable {
    
    /**
     * Name of the rule helper.
     * 
     * <p>This field contains a unique identifier for the rule helper
     * that is used for lookup and instantiation purposes.
     */
    private String name;
    
    /**
     * Description of the rule helper.
     * 
     * <p>This field provides a human-readable description of what the
     * helper class does and its intended purpose.
     */
    private String description;
    
    /**
     * URL to the JAR file containing the helper classes.
     * 
     * <p>This field specifies the location of the JAR file that contains
     * the helper classes. The URL can be a file://, http://, or https:// URL.
     */
    private String packageUrl;
    
    /**
     * Package path for scanning helper classes.
     * 
     * <p>This field specifies the Java package path within the JAR file
     * where helper classes are located for dynamic loading.
     */
    private String packagePath;
    
    /**
     * Container name for organizing rule helpers.
     * 
     * <p>This field is used to group related rule helpers together
     * and can be used for filtering and organization purposes.
     */
    private String containerName;

}
