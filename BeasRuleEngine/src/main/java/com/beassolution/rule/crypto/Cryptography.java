package com.beassolution.rule.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

/**
 * Component for cryptographic operations in the Beas Rule Engine.
 *
 * <p>This class provides encryption and decryption functionality using
 * TripleDES algorithm. It supports field-level encryption for entities
 * that extend CryptoState and are marked with the @Encryptable annotation.
 *
 * <p>Key features include:
 * <ul>
 *   <li>TripleDES encryption/decryption</li>
 *   <li>Field-level encryption support</li>
 *   <li>Automatic encryption state management</li>
 *   <li>Reflection-based field processing</li>
 * </ul>
 *
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class Cryptography extends BaseCrypto {

    /**
     * The encryption algorithm used.
     */
    private static final String ALGORITHM = "TripleDES";

    /**
     * The encryption transformation used.
     */
    private static final String TRANSFORM = "TripleDES/CBC/PKCS5Padding";

    /**
     * Cipher for encryption operations.
     */
    private final Cipher encryptCipher;

    /**
     * Cipher for decryption operations.
     */
    private final Cipher decryptCipher;

    /**
     * Constructor that initializes the cryptographic ciphers.
     *
     * <p>This constructor initializes the encryption and decryption ciphers
     * using the provided secret key and initialization vector. The ciphers
     * are configured for TripleDES encryption in CBC mode with PKCS5 padding.
     *
     * @param secretKey The secret key for encryption/decryption
     * @param vector    The initialization vector
     * @throws NoSuchPaddingException             if the padding scheme is not available
     * @throws NoSuchAlgorithmException           if the algorithm is not available
     * @throws InvalidAlgorithmParameterException if the parameters are invalid
     * @throws InvalidKeyException                if the key is invalid
     */
    public Cryptography(@Value("${cryptography.key}") String secretKey,
                        @Value("${cryptography.iv}") String vector)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException {

        var secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        var iv = vector.getBytes(StandardCharsets.UTF_8);
        var ivSpec = new IvParameterSpec(iv);

        encryptCipher = Cipher.getInstance(TRANSFORM);
        decryptCipher = Cipher.getInstance(TRANSFORM);

        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
    }

    /**
     * Encrypts all @Encryptable fields in an object.
     *
     * <p>This method processes all fields marked with @Encryptable annotation
     * in the given object and encrypts their values. The object must extend
     * CryptoState for this operation to work.
     *
     * @param source The object to encrypt
     * @throws UnsupportedEncodingException       if encoding is not supported
     * @throws InvalidAlgorithmParameterException if algorithm parameters are invalid
     * @throws InvalidKeyException                if the key is invalid
     */
    public void encrypt(Object source) throws UnsupportedEncodingException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {

        // Use pattern matching for instanceof (Java 16+)
        if (!(source instanceof CryptoState cryptoState)) {
            log.warn("Source is not instance of CryptoState");
            return;
        }

        if (Objects.equals(cryptoState.getEncrypted(), Boolean.TRUE)) {
            log.warn("Target is already encrypted!");
            return;
        }

        Arrays.stream(source.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Encryptable.class) != null)
                .filter(field -> field.getType() == String.class)
                .forEach(field -> encryptField(source, field.getName()));
    }

    /**
     * Encrypts a specific field in an object.
     *
     * @param source    The object containing the field
     * @param fieldName The name of the field to encrypt
     */
    private void encryptField(Object source, String fieldName) {
        try {
            String getMethodName = convertGetMethodName(fieldName);
            String setMethodName = convertSetMethodName(fieldName);

            var getMethod = source.getClass().getDeclaredMethod(getMethodName);
            var setMethod = source.getClass().getDeclaredMethod(setMethodName, String.class);

            var response = getMethod.invoke(source);

            if (response instanceof String stringValue && !stringValue.isEmpty()) {
                String encodedData = plainEncrypt(stringValue);
                setMethod.invoke(source, encodedData);
                ((CryptoState) source).setEncrypted(true);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error("Encryption Error for field {}: {}", fieldName, e.getMessage());
        }
    }

    /**
     * Encrypts a plain text string.
     *
     * @param data The plain text to encrypt
     * @return Base64 encoded encrypted string
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public String plainEncrypt(String data) throws IllegalBlockSizeException, BadPaddingException {
        var plainBytes = data.getBytes(StandardCharsets.UTF_8);
        var encryptedBytes = encryptCipher.doFinal(plainBytes);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts all @Encryptable fields in an object.
     *
     * <p>This method processes all fields marked with @Encryptable annotation
     * in the given object and decrypts their values. The object must extend
     * CryptoState and be in an encrypted state.
     *
     * @param target The object to decrypt
     */
    public void decrypt(Object target) {
        // Use pattern matching for instanceof (Java 16+)
        if (!(target instanceof CryptoState cryptoState)) {
            log.warn("Target is not instance of CryptoState");
            return;
        }

        if (!cryptoState.getEncrypted()) {
            log.warn("Target is not encrypted!");
            return;
        }

        Arrays.stream(target.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Encryptable.class) != null)
                .filter(field -> field.getType() == String.class)
                .forEach(field -> decryptField(target, field.getName()));
    }

    /**
     * Decrypts a specific field in an object.
     *
     * @param target    The object containing the field
     * @param fieldName The name of the field to decrypt
     */
    private void decryptField(Object target, String fieldName) {
        try {
            String getMethodName = convertGetMethodName(fieldName);
            String setMethodName = convertSetMethodName(fieldName);

            var getMethod = target.getClass().getDeclaredMethod(getMethodName);
            var setMethod = target.getClass().getDeclaredMethod(setMethodName, String.class);

            var response = getMethod.invoke(target);

            if (response instanceof String stringValue && !stringValue.isEmpty()) {
                String decodedData = plainDecrypt(stringValue);
                setMethod.invoke(target, decodedData);
                ((CryptoState) target).setEncrypted(false);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error("Decryption Error for field {}: {}", fieldName, e.getMessage());
        }
    }

    /**
     * Decrypts an encrypted string.
     *
     * @param data The Base64 encoded encrypted string
     * @return The decrypted plain text
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public String plainDecrypt(String data) throws IllegalBlockSizeException, BadPaddingException {
        var encryptedData = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        var plainBytes = decryptCipher.doFinal(encryptedData);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    /**
     * Converts a field name to a getter method name.
     *
     * @param name The field name
     * @return The getter method name
     */
    private String convertGetMethodName(String name) {
        return "get" + convertName(name);
    }

    /**
     * Converts a field name to a setter method name.
     *
     * @param name The field name
     * @return The setter method name
     */
    private String convertSetMethodName(String name) {
        return "set" + convertName(name);
    }

    /**
     * Converts a field name to proper case for method names.
     *
     * @param name The field name
     * @return The properly cased name
     */
    private String convertName(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }
}
