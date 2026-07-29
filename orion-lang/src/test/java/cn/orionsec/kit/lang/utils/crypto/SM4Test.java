package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * SM4 单元测试
 */
public class SM4Test {

    // SM4 key 必须 16 bytes (128 bit)
    private static final String KEY_16 = "1234567890abcdef";
    // SM4 IV 必须 16 bytes
    private static final String IV_16 = "abcdef1234567890";
    private static final String PLAIN_TEXT = "Hello, SM4 encryption!";

    @Test
    public void testEcbEncryptDecryptString() {
        String encrypted = SM4.encrypt(PLAIN_TEXT, KEY_16);
        Assert.assertNotNull(encrypted);
        String decrypted = SM4.decrypt(encrypted, KEY_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_16.getBytes();
        byte[] encrypted = SM4.encrypt(plainBytes, keyBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = SM4.decrypt(encrypted, keyBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_16, CipherAlgorithm.SM4);
        String encrypted = SM4.encrypt(PLAIN_TEXT, secretKey);
        Assert.assertNotNull(encrypted);
        String decrypted = SM4.decrypt(encrypted, secretKey);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptString() {
        String encrypted = SM4.encrypt(PLAIN_TEXT, KEY_16, IV_16);
        Assert.assertNotNull(encrypted);
        String decrypted = SM4.decrypt(encrypted, KEY_16, IV_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_16.getBytes();
        byte[] ivBytes = IV_16.getBytes();
        byte[] encrypted = SM4.encrypt(plainBytes, keyBytes, ivBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = SM4.decrypt(encrypted, keyBytes, ivBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_16, CipherAlgorithm.SM4);
        String encrypted = SM4.encrypt(PLAIN_TEXT, secretKey, IV_16);
        Assert.assertNotNull(encrypted);
        String decrypted = SM4.decrypt(encrypted, secretKey, IV_16);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        String key2 = "abcdef1234567890";
        String enc1 = SM4.encrypt(PLAIN_TEXT, KEY_16);
        String enc2 = SM4.encrypt(PLAIN_TEXT, key2);
        Assert.assertNotEquals(enc1, enc2);
    }
}
