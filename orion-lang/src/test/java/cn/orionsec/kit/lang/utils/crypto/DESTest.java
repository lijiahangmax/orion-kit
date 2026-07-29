package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * DES 单元测试
 */
public class DESTest {

    // DES key 必须 8 bytes (64 bit)
    private static final String KEY_8 = "12345678";
    // DES IV 必须 8 bytes
    private static final String IV_8 = "abcdefgh";
    private static final String PLAIN_TEXT = "Hello, DES encryption!";

    @Test
    public void testEcbEncryptDecryptString() {
        String encrypted = DES.encrypt(PLAIN_TEXT, KEY_8);
        Assert.assertNotNull(encrypted);
        String decrypted = DES.decrypt(encrypted, KEY_8);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_8.getBytes();
        byte[] encrypted = DES.encrypt(plainBytes, keyBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = DES.decrypt(encrypted, keyBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testEcbEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_8, CipherAlgorithm.DES);
        String encrypted = DES.encrypt(PLAIN_TEXT, secretKey);
        Assert.assertNotNull(encrypted);
        String decrypted = DES.decrypt(encrypted, secretKey);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptString() {
        String encrypted = DES.encrypt(PLAIN_TEXT, KEY_8, IV_8);
        Assert.assertNotNull(encrypted);
        String decrypted = DES.decrypt(encrypted, KEY_8, IV_8);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] keyBytes = KEY_8.getBytes();
        byte[] ivBytes = IV_8.getBytes();
        byte[] encrypted = DES.encrypt(plainBytes, keyBytes, ivBytes);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = DES.decrypt(encrypted, keyBytes, ivBytes);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testCbcEncryptDecryptWithSecretKey() {
        SecretKey secretKey = Keys.getSecretKey(KEY_8, CipherAlgorithm.DES);
        String encrypted = DES.encrypt(PLAIN_TEXT, secretKey, IV_8);
        Assert.assertNotNull(encrypted);
        String decrypted = DES.decrypt(encrypted, secretKey, IV_8);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        String key2 = "abcdefgh";
        String enc1 = DES.encrypt(PLAIN_TEXT, KEY_8);
        String enc2 = DES.encrypt(PLAIN_TEXT, key2);
        Assert.assertNotEquals(enc1, enc2);
    }
}
