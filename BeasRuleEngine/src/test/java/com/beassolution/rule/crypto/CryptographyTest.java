package com.beassolution.rule.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Cryptography class.
 * 
 * <p>This test class provides comprehensive coverage for the Cryptography
 * class including all encryption and decryption operations:
 * <ul>
 *   <li>Plain text encryption and decryption</li>
 *   <li>Field-level encryption and decryption</li>
 *   <li>Error handling scenarios</li>
 *   <li>Edge cases and validation</li>
 *   <li>Security state management</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class CryptographyTest {

    private Cryptography cryptography;
    private static final String TEST_KEY = "123456789012345678901234";
    private static final String TEST_IV = "12345678";

    /**
     * Sets up the cryptography instance before each test.
     * 
     * <p>This method initializes the Cryptography instance with test
     * encryption key and initialization vector.
     */
    @BeforeEach
    void setUp() throws Exception {
        cryptography = new Cryptography(TEST_KEY, TEST_IV);
    }

    /**
     * Tests successful plain text encryption.
     * 
     * <p>This test verifies that plain text can be encrypted successfully
     * and the result is a valid Base64 encoded string.
     */
    @Test
    @DisplayName("Should successfully encrypt plain text")
    void testPlainEncrypt() throws IllegalBlockSizeException, BadPaddingException {
        // Given: Plain text to encrypt
        String plainText = "Hello, World!";

        // When: Encrypt the text
        String encryptedText = cryptography.plainEncrypt(plainText);

        // Then: Verify encryption result
        assertNotNull(encryptedText);
        assertNotEquals(plainText, encryptedText);
        assertTrue(encryptedText.length() > 0);
        
        // Verify it's valid Base64
        assertDoesNotThrow(() -> {
            java.util.Base64.getDecoder().decode(encryptedText);
        });
    }

    /**
     * Tests successful plain text decryption.
     * 
     * <p>This test verifies that encrypted text can be decrypted
     * back to the original plain text.
     */
    @Test
    @DisplayName("Should successfully decrypt encrypted text")
    void testPlainDecrypt() throws IllegalBlockSizeException, BadPaddingException {
        // Given: Plain text to encrypt and decrypt
        String originalText = "Test message for decryption";

        // When: Encrypt and then decrypt
        String encryptedText = cryptography.plainEncrypt(originalText);
        String decryptedText = cryptography.plainDecrypt(encryptedText);

        // Then: Verify decryption result
        assertEquals(originalText, decryptedText);
    }

    /**
     * Tests encryption and decryption of empty string.
     * 
     * <p>This test verifies that empty strings can be handled
     * properly during encryption and decryption.
     */
    @Test
    @DisplayName("Should handle empty string encryption and decryption")
    void testEmptyStringEncryption() throws IllegalBlockSizeException, BadPaddingException {
        // Given: Empty string
        String emptyString = "";

        // When: Encrypt and decrypt empty string
        String encryptedText = cryptography.plainEncrypt(emptyString);
        String decryptedText = cryptography.plainDecrypt(encryptedText);

        // Then: Verify result
        assertEquals(emptyString, decryptedText);
    }

    /**
     * Tests encryption and decryption of special characters.
     * 
     * <p>This test verifies that strings with special characters
     * can be encrypted and decrypted correctly.
     */
    @Test
    @DisplayName("Should handle special characters in encryption")
    void testSpecialCharactersEncryption() throws IllegalBlockSizeException, BadPaddingException {
        // Given: String with special characters
        String specialText = "Hello! @#$%^&*()_+-=[]{}|;':\",./<>?";

        // When: Encrypt and decrypt special characters
        String encryptedText = cryptography.plainEncrypt(specialText);
        String decryptedText = cryptography.plainDecrypt(encryptedText);

        // Then: Verify result
        assertEquals(specialText, decryptedText);
    }

    /**
     * Tests encryption and decryption of unicode characters.
     * 
     * <p>This test verifies that unicode characters can be
     * encrypted and decrypted correctly.
     */
    @Test
    @DisplayName("Should handle unicode characters in encryption")
    void testUnicodeCharactersEncryption() throws IllegalBlockSizeException, BadPaddingException {
        // Given: String with unicode characters
        String unicodeText = "Hello 世界! Привет! こんにちは!";

        // When: Encrypt and decrypt unicode text
        String encryptedText = cryptography.plainEncrypt(unicodeText);
        String decryptedText = cryptography.plainDecrypt(encryptedText);

        // Then: Verify result
        assertEquals(unicodeText, decryptedText);
    }

    /**
     * Tests field-level encryption of CryptoState object.
     * 
     * <p>This test verifies that fields marked with @Encryptable
     * annotation are properly encrypted.
     */
    @Test
    @DisplayName("Should encrypt fields marked with @Encryptable annotation")
    void testEncryptFields() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // Given: Test object with encryptable fields
        TestCryptoState testObject = new TestCryptoState();
        testObject.setSecretField("secret value");
        testObject.setPublicField("public value");
        testObject.setEncrypted(false);

        // When: Encrypt the object
        cryptography.encrypt(testObject);

        // Then: Verify encryption state and field values
        assertTrue(testObject.getEncrypted());
        assertNotEquals("secret value", testObject.getSecretField());
        assertEquals("public value", testObject.getPublicField()); // Should not be encrypted
    }

    /**
     * Tests field-level decryption of CryptoState object.
     * 
     * <p>This test verifies that fields marked with @Encryptable
     * annotation are properly decrypted.
     */
    @Test
    @DisplayName("Should decrypt fields marked with @Encryptable annotation")
    void testDecryptFields() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Given: Test object with encrypted fields
        TestCryptoState testObject = new TestCryptoState();
        testObject.setSecretField("secret value");
        testObject.setPublicField("public value");
        testObject.setEncrypted(false);

        // When: Encrypt and then decrypt the object
        cryptography.encrypt(testObject);
        cryptography.decrypt(testObject);

        // Then: Verify decryption result
        assertFalse(testObject.getEncrypted());
        assertEquals("secret value", testObject.getSecretField());
        assertEquals("public value", testObject.getPublicField());
    }

    /**
     * Tests encryption of null object.
     * 
     * <p>This test verifies that null objects are handled
     * gracefully during encryption.
     */
    @Test
    @DisplayName("Should handle null object during encryption")
    void testEncryptNullObject() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // When & Then: Encrypt null object should not throw exception
        assertDoesNotThrow(() -> {
            cryptography.encrypt(null);
        });
    }

    /**
     * Tests encryption of non-CryptoState object.
     * 
     * <p>This test verifies that non-CryptoState objects are handled
     * gracefully during encryption.
     */
    @Test
    @DisplayName("Should handle non-CryptoState object during encryption")
    void testEncryptNonCryptoStateObject() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // Given: Non-CryptoState object
        Object nonCryptoObject = new Object();

        // When & Then: Encrypt non-CryptoState object should not throw exception
        assertDoesNotThrow(() -> {
            cryptography.encrypt(nonCryptoObject);
        });
    }

    /**
     * Tests encryption of already encrypted object.
     * 
     * <p>This test verifies that already encrypted objects are
     * handled gracefully and not re-encrypted.
     */
    @Test
    @DisplayName("Should handle already encrypted object")
    void testEncryptAlreadyEncryptedObject() throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // Given: Already encrypted object
        TestCryptoState testObject = new TestCryptoState();
        testObject.setSecretField("secret value");
        testObject.setEncrypted(true);

        String originalEncryptedValue = testObject.getSecretField();

        // When: Try to encrypt already encrypted object
        cryptography.encrypt(testObject);

        // Then: Verify value remains unchanged
        assertEquals(originalEncryptedValue, testObject.getSecretField());
        assertTrue(testObject.getEncrypted());
    }

    /**
     * Tests decryption of null object.
     * 
     * <p>This test verifies that null objects are handled
     * gracefully during decryption.
     */
    @Test
    @DisplayName("Should handle null object during decryption")
    void testDecryptNullObject() {
        // When & Then: Decrypt null object should not throw exception
        assertDoesNotThrow(() -> {
            cryptography.decrypt(null);
        });
    }

    /**
     * Tests decryption of non-CryptoState object.
     * 
     * <p>This test verifies that non-CryptoState objects are handled
     * gracefully during decryption.
     */
    @Test
    @DisplayName("Should handle non-CryptoState object during decryption")
    void testDecryptNonCryptoStateObject() {
        // Given: Non-CryptoState object
        Object nonCryptoObject = new Object();

        // When & Then: Decrypt non-CryptoState object should not throw exception
        assertDoesNotThrow(() -> {
            cryptography.decrypt(nonCryptoObject);
        });
    }

    /**
     * Tests decryption of non-encrypted object.
     * 
     * <p>This test verifies that non-encrypted objects are
     * handled gracefully during decryption.
     */
    @Test
    @DisplayName("Should handle non-encrypted object during decryption")
    void testDecryptNonEncryptedObject() {
        // Given: Non-encrypted object
        TestCryptoState testObject = new TestCryptoState();
        testObject.setSecretField("secret value");
        testObject.setEncrypted(false);

        String originalValue = testObject.getSecretField();

        // When: Try to decrypt non-encrypted object
        cryptography.decrypt(testObject);

        // Then: Verify value remains unchanged
        assertEquals(originalValue, testObject.getSecretField());
        assertFalse(testObject.getEncrypted());
    }

    /**
     * Tests encryption with invalid key.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when invalid encryption keys are provided.
     */
    @Test
    @DisplayName("Should throw exception with invalid key")
    void testEncryptionWithInvalidKey() {
        // Given: Invalid key (too short)
        String invalidKey = "short";

        // When & Then: Verify exception is thrown
        assertThrows(InvalidKeyException.class, () -> {
            new Cryptography(invalidKey, TEST_IV);
        });
    }

    /**
     * Tests encryption with invalid initialization vector.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when invalid initialization vectors are provided.
     */
    @Test
    @DisplayName("Should throw exception with invalid initialization vector")
    void testEncryptionWithInvalidIV() {
        // Given: Invalid IV (too short)
        String invalidIV = "short";

        // When & Then: Verify exception is thrown
        assertThrows(InvalidAlgorithmParameterException.class, () -> {
            new Cryptography(TEST_KEY, invalidIV);
        });
    }

    /**
     * Tests decryption of invalid Base64 string.
     * 
     * <p>This test verifies that appropriate exceptions are thrown
     * when invalid Base64 encoded strings are provided for decryption.
     */
    @Test
    @DisplayName("Should throw exception when decrypting invalid Base64")
    void testDecryptInvalidBase64() {
        // Given: Invalid Base64 string
        String invalidBase64 = "invalid-base64-string";

        // When & Then: Verify exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            cryptography.plainDecrypt(invalidBase64);
        });
    }

    /**
     * Tests encryption and decryption of large text.
     * 
     * <p>This test verifies that large text blocks can be
     * encrypted and decrypted correctly.
     */
    @Test
    @DisplayName("Should handle large text encryption and decryption")
    void testLargeTextEncryption() throws IllegalBlockSizeException, BadPaddingException {
        // Given: Large text block
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeText.append("This is a large text block for testing encryption and decryption. ");
        }
        String originalText = largeText.toString();

        // When: Encrypt and decrypt large text
        String encryptedText = cryptography.plainEncrypt(originalText);
        String decryptedText = cryptography.plainDecrypt(encryptedText);

        // Then: Verify result
        assertEquals(originalText, decryptedText);
    }

    /**
     * Test class for CryptoState implementation.
     * 
     * <p>This class provides a test implementation of CryptoState
     * with fields marked for encryption testing.
     */
    private static class TestCryptoState extends CryptoState {
        @Encryptable
        private String secretField;
        
        private String publicField;

        public String getSecretField() {
            return secretField;
        }

        public void setSecretField(String secretField) {
            this.secretField = secretField;
        }

        public String getPublicField() {
            return publicField;
        }

        public void setPublicField(String publicField) {
            this.publicField = publicField;
        }
    }
} 