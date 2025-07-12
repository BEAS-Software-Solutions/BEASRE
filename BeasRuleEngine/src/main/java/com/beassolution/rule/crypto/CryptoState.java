package com.beassolution.rule.crypto;

import lombok.Data;

/**
 * Base class for cryptographic state management.
 * 
 * <p>This class provides a base implementation for managing the encryption
 * state of entities in the Beas Rule Engine. It tracks whether an entity
 * has been encrypted and provides the foundation for field-level encryption.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Encryption state tracking</li>
 *   <li>Integration with encryption framework</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class CryptoState {
    
    /**
     * Flag indicating whether the entity is encrypted.
     * 
     * <p>This field tracks the encryption state of the entity. When true,
     * the entity has been encrypted. When false, the entity is in plain text.
     * This flag is used by the encryption framework to determine whether
     * encryption or decryption operations are needed.
     */
    private Boolean encrypted;
}
