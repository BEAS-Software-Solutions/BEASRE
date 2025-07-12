package com.beassolution.rule.crypto;

import com.beassolution.rule.exception.OperationException;
import org.springframework.http.HttpStatus;

/**
 * Base class for cryptographic operations.
 * 
 * <p>This class provides common functionality for cryptographic operations
 * in the Beas Rule Engine. It extends CryptoState to provide encryption
 * state management and includes utility methods for error handling.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Encryption state inheritance</li>
 *   <li>Standardized error handling</li>
 *   <li>HTTP status code integration</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public class BaseCrypto extends CryptoState {
    
    /**
     * Throws a runtime exception with a custom message.
     * 
     * <p>This method creates and throws an OperationException with the
     * specified message and a default HTTP status code of BAD_REQUEST.
     * 
     * @param message The error message
     * @throws RuntimeException always thrown
     */
    protected final void throwError(String message) throws RuntimeException {
        throw new OperationException(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Throws a runtime exception with a custom message and HTTP status.
     * 
     * <p>This method creates and throws an OperationException with the
     * specified message and HTTP status code.
     * 
     * @param message The error message
     * @param status The HTTP status code
     * @throws RuntimeException always thrown
     */
    protected final void throwError(String message, HttpStatus status) throws RuntimeException {
        throw new OperationException(message, status);
    }
}
