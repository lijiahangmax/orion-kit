package cn.orionsec.kit.lang.utils.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * Caesars 单元测试
 */
public class CaesarsTest {

    private static final String PLAIN_TEXT = "HelloWorld";

    @Test
    public void testDefaultEncryptDecrypt() {
        Caesars caesars = new Caesars();
        String encrypted = caesars.encrypt(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        Assert.assertNotEquals(PLAIN_TEXT, encrypted);
        String decrypted = caesars.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testCustomKeyEncryptDecrypt() {
        Caesars caesars = new Caesars(7);
        String encrypted = caesars.encrypt(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = caesars.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testKeyZero() {
        Caesars caesars = new Caesars(0);
        String encrypted = caesars.encrypt(PLAIN_TEXT);
        // key=0 加密后应与明文相同
        Assert.assertEquals(PLAIN_TEXT, encrypted);
    }

    @Test
    public void testNonLetterCharactersUnchanged() {
        Caesars caesars = new Caesars(5);
        String input = "Hello, World! 123";
        String encrypted = caesars.encrypt(input);
        // 非字母字符保持不变
        Assert.assertTrue(encrypted.contains(","));
        Assert.assertTrue(encrypted.contains("!"));
        Assert.assertTrue(encrypted.contains(" "));
        Assert.assertTrue(encrypted.contains("123"));
        // 加密后能正确解密
        String decrypted = caesars.decrypt(encrypted);
        Assert.assertEquals(input, decrypted);
    }

    @Test
    public void testDifferentKeysProduceDifferentCiphertext() {
        Caesars caesars3 = new Caesars(3);
        Caesars caesars7 = new Caesars(7);
        String enc3 = caesars3.encrypt(PLAIN_TEXT);
        String enc7 = caesars7.encrypt(PLAIN_TEXT);
        Assert.assertNotEquals(enc3, enc7);
    }

    @Test
    public void testLargeKey() {
        Caesars caesars = new Caesars(51);
        String encrypted = caesars.encrypt(PLAIN_TEXT);
        String decrypted = caesars.decrypt(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testAllLetters() {
        Caesars caesars = new Caesars(5);
        String input = "AaBbCcZzYy";
        String encrypted = caesars.encrypt(input);
        String decrypted = caesars.decrypt(encrypted);
        Assert.assertEquals(input, decrypted);
    }
}
