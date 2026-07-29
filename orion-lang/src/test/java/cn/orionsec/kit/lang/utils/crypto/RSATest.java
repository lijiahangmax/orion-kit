package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.utils.crypto.enums.RSASignature;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA 单元测试
 */
public class RSATest {

    private static RSAPublicKey publicKey;
    private static RSAPrivateKey privateKey;
    private static String publicKeyStr;
    private static String privateKeyStr;
    private static final String PLAIN_TEXT = "Hello, RSA encryption!";

    @BeforeClass
    public static void setup() {
        Pair<RSAPublicKey, RSAPrivateKey> keys = RSA.generatorKeys();
        publicKey = keys.getKey();
        privateKey = keys.getValue();
        publicKeyStr = Keys.getPublicKey(publicKey);
        privateKeyStr = Keys.getPrivateKey(privateKey);
    }

    @Test
    public void testEncryptDecryptWithKeyObjects() {
        String encrypted = RSA.encrypt(PLAIN_TEXT, publicKey);
        Assert.assertNotNull(encrypted);
        String decrypted = RSA.decrypt(encrypted, privateKey);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEncryptDecryptWithKeyStrings() {
        String encrypted = RSA.encrypt(PLAIN_TEXT, publicKeyStr);
        Assert.assertNotNull(encrypted);
        String decrypted = RSA.decrypt(encrypted, privateKeyStr);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testEncryptDecryptBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] encrypted = RSA.encrypt(plainBytes, publicKey);
        Assert.assertNotNull(encrypted);
        byte[] decrypted = RSA.decrypt(encrypted, privateKey);
        Assert.assertArrayEquals(plainBytes, decrypted);
    }

    @Test
    public void testSignAndVerifyWithMD5() {
        String signature = RSA.sign(PLAIN_TEXT, privateKey);
        Assert.assertNotNull(signature);
        boolean verified = RSA.verify(PLAIN_TEXT, publicKey, signature);
        Assert.assertTrue(verified);
    }

    @Test
    public void testSignAndVerifyWithSHA256() {
        String signature = RSA.sign(PLAIN_TEXT, privateKey, RSASignature.SHA256);
        Assert.assertNotNull(signature);
        boolean verified = RSA.verify(PLAIN_TEXT, publicKey, signature, RSASignature.SHA256);
        Assert.assertTrue(verified);
    }

    @Test
    public void testSignAndVerifyWithSHA512() {
        String signature = RSA.sign(PLAIN_TEXT, privateKey, RSASignature.SHA512);
        Assert.assertNotNull(signature);
        boolean verified = RSA.verify(PLAIN_TEXT, publicKey, signature, RSASignature.SHA512);
        Assert.assertTrue(verified);
    }

    @Test
    public void testSignAndVerifyWithKeyStrings() {
        String signature = RSA.sign(PLAIN_TEXT, privateKeyStr);
        Assert.assertNotNull(signature);
        boolean verified = RSA.verify(PLAIN_TEXT, publicKeyStr, signature);
        Assert.assertTrue(verified);
    }

    @Test
    public void testVerifyFailsWithWrongData() {
        String signature = RSA.sign(PLAIN_TEXT, privateKey);
        boolean verified = RSA.verify("wrong data", publicKey, signature);
        Assert.assertFalse(verified);
    }

    @Test
    public void testSignBytes() {
        byte[] plainBytes = PLAIN_TEXT.getBytes();
        byte[] signature = RSA.sign(plainBytes, privateKey);
        Assert.assertNotNull(signature);
        boolean verified = RSA.verify(plainBytes, publicKey, signature);
        Assert.assertTrue(verified);
    }

    @Test
    public void testGeneratorKeys() {
        Pair<RSAPublicKey, RSAPrivateKey> keys = RSA.generatorKeys();
        Assert.assertNotNull(keys.getKey());
        Assert.assertNotNull(keys.getValue());
    }

    @Test
    public void testGeneratorKeysWithLength() {
        Pair<RSAPublicKey, RSAPrivateKey> keys = RSA.generatorKeys(1024);
        Assert.assertNotNull(keys.getKey());
        Assert.assertNotNull(keys.getValue());
    }

    @Test
    public void testGetPublicKeyFromPrivateKey() {
        RSAPublicKey derivedPublicKey = RSA.getPublicKey(privateKey);
        Assert.assertNotNull(derivedPublicKey);
        // 验证用派生的公钥也能验证签名
        String signature = RSA.sign(PLAIN_TEXT, privateKey);
        boolean verified = RSA.verify(PLAIN_TEXT, derivedPublicKey, signature);
        Assert.assertTrue(verified);
    }

    @Test
    public void testGetPrivateKeyFromString() {
        RSAPrivateKey key = RSA.getPrivateKey(privateKeyStr);
        Assert.assertNotNull(key);
    }

    @Test
    public void testGetPublicKeyFromString() {
        RSAPublicKey key = RSA.getPublicKey(publicKeyStr);
        Assert.assertNotNull(key);
    }

    @Test
    public void testSignWithAllRSASignatureModes() {
        for (RSASignature mode : RSASignature.values()) {
            if (mode == RSASignature.NONE) {
                // NONE 模式对数据长度有特殊要求，跳过
                continue;
            }
            String signature = RSA.sign(PLAIN_TEXT, privateKey, mode);
            Assert.assertNotNull("Signature should not be null for mode: " + mode, signature);
            boolean verified = RSA.verify(PLAIN_TEXT, publicKey, signature, mode);
            Assert.assertTrue("Verification should pass for mode: " + mode, verified);
        }
    }
}
