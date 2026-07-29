package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * DES3 单元测试
 */
public class DES3Test {

    // 3DES key 必须 24 bytes (192 bit)
    private static final String KEY_24 = "123456789012345678901234";
    // 3DES IV 必须 8 bytes
    private static final String IV_8 = "abcdefgh";
    private static final String PLAIN_TEXT = "Hello, 3DES encryption!";

    @Test
    public void testEcbEncryptDecryptString() {
        String encrypted = DES3.encrypt(PLAIN_TEXT, KEY_24);
        Assert.assertNotNull(encrypted);
        String decrypted = DES3.decrypt(encrypted, KEY_24);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_24.getBytes();
        byte[] encrypted = DES3.encrypt(plainBytes, keyBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = DES3.decrypt(encrypted, keyBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_24, CipherAlgorithm.DES3);
        String encrypted = DES3.encrypt(PLAIN_TEXT, secretKey);
        Assert.assertNotNull(encrypted);
        String decrypted = DES3.decrypt(encrypted, secretKey);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptString() {
        String encrypted = DES3.encrypt(PLAIN_TEXT, KEY_24, IV_8);
        Assert.assertNotNull(encrypted);
        String decrypted = DES3.decrypt(encrypted, KEY_24, IV_8);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_24.getBytes();
        byte[] ivBytes = IV_8.getBytes();
        byte[] encrypted = DES3.encrypt(plainBytes, keyBytes, ivBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = DES3.decrypt(encrypted, keyBytes, ivBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_24, CipherAlgorithm.DES3);
        String encrypted = DES3.encrypt(PLAIN_TEXT, secretKey, IV_8);
        Assert.assertNotNull(encrypted);
        String decrypted = DES3.decrypt(encrypted, secretKey, IV_8);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        String key2 = "abcdefghijklmnopqrstuvwx";
        String enc1 = DES3.encrypt(PLAIN_TEXT, KEY_24);
        String enc2 = DES3.encrypt(PLAIN_TEXT, key2);
        Assert.assertNotEquals(enc1, enc2);
    }
}
