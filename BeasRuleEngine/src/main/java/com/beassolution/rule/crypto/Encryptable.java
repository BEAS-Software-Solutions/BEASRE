package com.beassolution.rule.crypto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields that should be encrypted.
 * 
 * <p>This annotation is used to mark fields in entities that should be
 * automatically encrypted and decrypted by the cryptographic framework.
 * Only fields marked with this annotation will be processed during
 * encryption and decryption operations.
 * 
 * <p>Usage:
 * <ul>
 *   <li>Apply to String fields that contain sensitive data</li>
 *   <li>Fields must be in classes that extend CryptoState</li>
 *   <li>Only String fields are currently supported</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Encryptable{
}
