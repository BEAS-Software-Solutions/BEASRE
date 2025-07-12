package com.beassolution.rule.model.base;

import com.beassolution.rule.crypto.CryptoState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Base model class for all entities in the Beas Rule Engine.
 * 
 * <p>This class provides common fields and functionality for all domain entities
 * including audit information and cryptographic state management. It extends
 * CryptoState to support field-level encryption capabilities.
 * 
 * <p>Common fields include:
 * <ul>
 *   <li>Unique identifier (UUID)</li>
 *   <li>Creation audit information</li>
 *   <li>Modification audit information</li>
 *   <li>Encryption state tracking</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class BaseModel extends CryptoState implements Serializable {
    
    /**
     * Unique identifier for the entity.
     * 
     * <p>This field is automatically generated as a UUID and serves as the
     * primary key for the entity in the database.
     */
    @Id
    private UUID id;
    
    /**
     * Username of the user who created the entity.
     * 
     * <p>This field is automatically populated when the entity is first saved
     * to the database.
     */
    @CreatedBy
    private String createdBy;
    
    /**
     * Timestamp when the entity was created.
     * 
     * <p>This field is automatically populated with the current date/time
     * when the entity is first saved to the database.
     */
    @CreatedDate
    private Date createdDate;
    
    /**
     * Timestamp when the entity was last modified.
     * 
     * <p>This field is automatically updated with the current date/time
     * whenever the entity is saved to the database.
     */
    @LastModifiedDate
    private Date lastModifiedDate;
    
    /**
     * Username of the user who last modified the entity.
     * 
     * <p>This field is automatically updated with the current user's username
     * whenever the entity is saved to the database.
     */
    @LastModifiedBy
    private String lastModifiedBy;
}