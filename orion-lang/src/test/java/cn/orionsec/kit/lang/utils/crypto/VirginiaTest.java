package cn.orionsec.kit.lang.utils.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * Virginia 单元测试
 */
public class VirginiaTest {

    private static final String PLAIN_TEXT = "Hello World";
    private static final String KEY = "secret";

    @Test
    public void testEncryptDecrypt() {
        String encrypted = Virginia.encrypt(PLAIN_TEXT, KEY);
        Assert.assertNotNull(encrypted);
        Assert.assertNotEquals(PLAIN_TEXT, encrypted);
        String decrypted = Virginia.decrypt(encrypted, KEY);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        String enc1 = Virginia.encrypt(PLAIN_TEXT, "keyA");
        String enc2 = Virginia.encrypt(PLAIN_TEXT, "keyB");
        Assert.assertNotEquals(enc1, enc2);
    }

    @Test
    public void testEncryptDecryptWithNumbers() {
        String input = "Test123";
        String encrypted = Virginia.encrypt(input, KEY);
        String decrypted = Virginia.decrypt(encrypted, KEY);
        Assert.assertEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptWithSpecialChars() {
        String input = "Hello!@#$%";
        String encrypted = Virginia.encrypt(input, KEY);
        String decrypted = Virginia.decrypt(encrypted, KEY);
        Assert.assertEquals(input, decrypted);
    }

    @Test
    public void testLongText() {
        String input = "This is a longer text to test the Virginia cipher with longer messages";
        String encrypted = Virginia.encrypt(input, KEY);
        String decrypted = Virginia.decrypt(encrypted, KEY);
        Assert.assertEquals(input, decrypted);
    }
}
