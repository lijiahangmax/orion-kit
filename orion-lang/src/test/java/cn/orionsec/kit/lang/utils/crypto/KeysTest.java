package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Keys 单元测试
 */
public class KeysTest {

    @Test
    public void testGetKeySpecBitLengthAES() {
        int length = Keys.getKeySpecBitLength(CipherAlgorithm.AES);
        Assert.assertEquals(CryptoConst.AES_KEY_LENGTH_BITS, length);
    }

    @Test
    public void testGetKeySpecBitLengthDES() {
        int length = Keys.getKeySpecBitLength(CipherAlgorithm.DES);
        Assert.assertEquals(CryptoConst.DES_KEY_LENGTH_BITS, length);
    }

    @Test
    public void testGetKeySpecBitLengthDES3() {
        int length = Keys.getKeySpecBitLength(CipherAlgorithm.DES3);
        Assert.assertEquals(CryptoConst.DES3_KEY_LENGTH_BITS, length);
    }

    @Test
    public void testGetKeySpecBitLengthSM4() {
        int length = Keys.getKeySpecBitLength(CipherAlgorithm.SM4);
        Assert.assertEquals(CryptoConst.SM4_KEY_LENGTH_BITS, length);
    }

    @Test
    public void testGetKeySpecByteLength() {
        int byteLength = Keys.getKeySpecByteLength(CipherAlgorithm.AES);
        Assert.assertEquals(CryptoConst.AES_KEY_LENGTH_BITS / Byte.SIZE, byteLength);
    }

    @Test
    public void testGetIvSpecBitLengthAES() {
        int length = Keys.getIvSpecBitLength(CipherAlgorithm.AES);
        Assert.assertEquals(CryptoConst.AES_IV_LENGTH_BITS, length);
    }

    @Test
    public void testGetIvSpecBitLengthDES() {
        int length = Keys.getIvSpecBitLength(CipherAlgorithm.DES);
        Assert.assertEquals(CryptoConst.DES_IV_LENGTH_BITS, length);
    }

    @Test
    public void testGetIvSpecByteLength() {
        int byteLength = Keys.getIvSpecByteLength(CipherAlgorithm.AES);
        Assert.assertEquals(CryptoConst.AES_IV_LENGTH_BITS / Byte.SIZE, byteLength);
    }

    @Test
    public void testGetGcmSpecLength() {
        int length = Keys.getGcmSpecLength(CipherAlgorithm.AES);
        Assert.assertEquals(CryptoConst.GCM_TAG_LENGTH_BITS, length);
    }

    @Test
    public void testGetIvSpec() {
        byte[] iv = "1234567890123456".getBytes();
        IvParameterSpec spec = Keys.getIvSpec(iv);
        Assert.assertNotNull(spec);
        Assert.assertArrayEquals(iv, spec.getIV());
    }

    @Test
    public void testGetIvSpecWithMode() {
        byte[] iv = "1234567890123456".getBytes();
        IvParameterSpec spec = Keys.getIvSpec(CipherAlgorithm.AES, iv);
        Assert.assertNotNull(spec);
    }

    @Test
    public void testGetGcmSpec() {
        byte[] gcm = "123456789012".getBytes();
        GCMParameterSpec spec = Keys.getGcmSpec(gcm);
        Assert.assertNotNull(spec);
    }

    @Test
    public void testGetGcmSpecWithMode() {
        byte[] gcm = "123456789012".getBytes();
        GCMParameterSpec spec = Keys.getGcmSpec(CipherAlgorithm.AES, gcm);
        Assert.assertNotNull(spec);
    }

    @Test
    public void testGetSecretKeyString() {
        SecretKey key = Keys.getSecretKey("1234567890abcdef", CipherAlgorithm.AES);
        Assert.assertNotNull(key);
        Assert.assertEquals("AES", key.getAlgorithm());
    }

    @Test
    public void testGetSecretKeyBytes() {
        byte[] keyBytes = "1234567890abcdef".getBytes();
        SecretKey key = Keys.getSecretKey(keyBytes, CipherAlgorithm.AES);
        Assert.assertNotNull(key);
        Assert.assertEquals("AES", key.getAlgorithm());
    }

    @Test
    public void testGetSecretKeyBase64String() {
        // 先生成一个 key，获取 base64
        SecretKey original = Keys.getSecretKey("1234567890abcdef", CipherAlgorithm.AES);
        String base64Key = Keys.getSecretKey(original);
        // 再用 base64 还原
        SecretKey restored = Keys.getSecretKeyBase64(base64Key, CipherAlgorithm.AES);
        Assert.assertNotNull(restored);
        Assert.assertArrayEquals(original.getEncoded(), restored.getEncoded());
    }

    @Test
    public void testGeneratorKeyAES() {
        SecretKey key = Keys.generatorKey("mySecretKey", CipherAlgorithm.AES);
        Assert.assertNotNull(key);
        Assert.assertEquals("AES", key.getAlgorithm());
    }

    @Test
    public void testGeneratorKeyDES() {
        SecretKey key = Keys.generatorKey("12345678", CipherAlgorithm.DES);
        Assert.assertNotNull(key);
    }

    @Test
    public void testGeneratorKeyDES3() {
        SecretKey key = Keys.generatorKey("123456789012345678901234", CipherAlgorithm.DES3);
        Assert.assertNotNull(key);
    }

    @Test
    public void testGeneratorKeySM4() {
        SecretKey key = Keys.generatorKey("1234567890abcdef", CipherAlgorithm.SM4);
        Assert.assertNotNull(key);
    }

    @Test
    public void testGetPublicKeyFromKeyPair() {
        var keys = RSA.generatorKeys();
        String pubKeyStr = Keys.getPublicKey(keys.getKey());
        Assert.assertNotNull(pubKeyStr);
        Assert.assertFalse(pubKeyStr.isEmpty());
    }

    @Test
    public void testGetPrivateKeyFromKeyPair() {
        var keys = RSA.generatorKeys();
        String privKeyStr = Keys.getPrivateKey(keys.getValue());
        Assert.assertNotNull(privKeyStr);
        Assert.assertFalse(privKeyStr.isEmpty());
    }

    @Test
    public void testGetSecretKeyFromSecretKey() {
        SecretKey key = Keys.getSecretKey("1234567890abcdef", CipherAlgorithm.AES);
        String keyStr = Keys.getSecretKey(key);
        Assert.assertNotNull(keyStr);
        Assert.assertFalse(keyStr.isEmpty());
    }
}
