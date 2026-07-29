package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * AES 单元测试
 */
public class AESTest {

    // AES key 必须 16 bytes (128 bit)
    private static final String KEY_16 = "1234567890abcdef";
    // AES IV 必须 16 bytes
    private static final String IV_16 = "abcdef1234567890";
    private static final String PLAIN_TEXT = "Hello, AES encryption!";

    @Test
    public void testEcbEncryptDecryptString() {
        String encrypted = AES.encrypt(PLAIN_TEXT, KEY_16);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, KEY_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_16.getBytes();
        byte[] encrypted = AES.encrypt(plainBytes, keyBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = AES.decrypt(encrypted, keyBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_16, CipherAlgorithm.AES);
        String encrypted = AES.encrypt(PLAIN_TEXT, secretKey);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, secretKey);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptString() {
        String encrypted = AES.encrypt(PLAIN_TEXT, KEY_16, IV_16);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, KEY_16, IV_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_16.getBytes();
        byte[] ivBytes = IV_16.getBytes();
        byte[] encrypted = AES.encrypt(plainBytes, keyBytes, ivBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = AES.decrypt(encrypted, keyBytes, ivBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_16, CipherAlgorithm.AES);
        String encrypted = AES.encrypt(PLAIN_TEXT, secretKey, IV_16);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, secretKey, IV_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testGcmEncryptDecryptString() {
        // GCM nonce 至少 12 bytes
        String gcm = "123456789012";
        String aad = "additional-data";
        String encrypted = AES.encrypt(PLAIN_TEXT, KEY_16, gcm, aad);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, KEY_16, gcm, aad);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testGcmEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_16.getBytes();
        byte[] gcmBytes = "123456789012".getBytes();
        byte[] aadBytes = "additional-data".getBytes();
        byte[] encrypted = AES.encrypt(plainBytes, keyBytes, gcmBytes, aadBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = AES.decrypt(encrypted, keyBytes, gcmBytes, aadBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testGcmEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_16, CipherAlgorithm.AES);
        String gcm = "123456789012";
        String aad = "additional-data";
        String encrypted = AES.encrypt(PLAIN_TEXT, secretKey, gcm, aad);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, secretKey, gcm, aad);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        String key2 = "abcdef1234567890";
        String enc1 = AES.encrypt(PLAIN_TEXT, KEY_16);
        String enc2 = AES.encrypt(PLAIN_TEXT, key2);
        Assert.assertNotEquals(enc1, enc2);
    }

    @Test
    public void testEmptyStringEncryption() {
        String encrypted = AES.encrypt("", KEY_16);
        Assert.assertNotNull(encrypted);
        String decrypted = AES.decrypt(encrypted, KEY_16);
        Assert.assertEquals("", decrypted);
    }
}
