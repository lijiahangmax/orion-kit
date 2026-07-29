package cn.orionsec.kit.lang.utils.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * RC4 单元测试
 */
public class RC4Test {

    private static final String KEY = "testSecretKey";
    private static final String PLAIN_TEXT = "Hello, RC4 encryption!";

    @Test
    public void testEncryptDecryptString() {
        RC4 rc4Enc = new RC4(KEY);
        String encrypted = rc4Enc.encrypt(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        Assert.assertNotEquals(PLAIN_TEXT, encrypted);

        RC4 rc4Dec = new RC4(KEY);
        String decrypted = rc4Dec.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        RC4 rc4Enc = new RC4(KEY);
        byte[] encrypted = rc4Enc.encrypt(plainBytes);
        Assert.assertNotNull(encrypted);

        RC4 rc4Dec = new RC4(KEY);
        byte[] decrypted = rc4Dec.decrypt(encrypted);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        RC4 rc4a = new RC4("keyAAA");
        RC4 rc4b = new RC4("keyBBB");
        String encA = rc4a.encrypt(PLAIN_TEXT);
        String encB = rc4b.encrypt(PLAIN_TEXT);
        Assert.assertNotEquals(encA, encB);
    }

    @Test
    public void testConstructorWithBytes() {
        byte[] keyBytes = KEY.getBytes();
        RC4 rc4Enc = new RC4(keyBytes);
        String encrypted = rc4Enc.encrypt(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);

        RC4 rc4Dec = new RC4(keyBytes);
        String decrypted = rc4Dec.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testInitKeyString() {
        RC4 rc4 = new RC4(KEY);
        rc4.initKey("newKeyValue");
        String encrypted = rc4.encrypt(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);

        RC4 rc4Dec = new RC4("newKeyValue");
        String decrypted = rc4Dec.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test(expected = Exception.class)
    public void testKeyTooShortThrowsException() {
        new RC4("abcd");
    }

    @Test
    public void testEncryptOrDecryptSymmetry() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        RC4 rc4 = new RC4(KEY);
        byte[] encrypted = rc4.encryptOrDecrypt(plainBytes);
        // 再次使用相同的 key 创建新的 RC4 实例
        RC4 rc4b = new RC4(KEY);
        byte[] decrypted = rc4b.encryptOrDecrypt(encrypted);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }
}
