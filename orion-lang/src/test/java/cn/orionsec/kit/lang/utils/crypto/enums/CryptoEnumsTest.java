package cn.orionsec.kit.lang.utils.crypto.enums;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Signature;

/**
 * enums 包下枚举类单元测试
 */
public class CryptoEnumsTest {

    // ==================== CipherAlgorithm ====================

    @Test
    public void testCipherAlgorithmValues() {
        CipherAlgorithm[] values = CipherAlgorithm.values();
        Assert.assertEquals(5, values.length);
        Assert.assertNotNull(CipherAlgorithm.RSA);
        Assert.assertNotNull(CipherAlgorithm.AES);
        Assert.assertNotNull(CipherAlgorithm.DES);
        Assert.assertNotNull(CipherAlgorithm.DES3);
        Assert.assertNotNull(CipherAlgorithm.SM4);
    }

    @Test
    public void testCipherAlgorithmGetMode() {
        Assert.assertEquals("RSA", CipherAlgorithm.RSA.getMode());
        Assert.assertEquals("AES", CipherAlgorithm.AES.getMode());
        Assert.assertEquals("DES", CipherAlgorithm.DES.getMode());
        Assert.assertEquals("DESEDE", CipherAlgorithm.DES3.getMode());
        Assert.assertEquals("SM4", CipherAlgorithm.SM4.getMode());
    }

    @Test
    public void testCipherAlgorithmGetCipher() {
        Cipher cipher = CipherAlgorithm.AES.getCipher();
        Assert.assertNotNull(cipher);
    }

    @Test
    public void testCipherAlgorithmGetCipherWithWorkAndPadding() {
        Cipher cipher = CipherAlgorithm.AES.getCipher(WorkingMode.ECB, PaddingMode.PKCS5_PADDING);
        Assert.assertNotNull(cipher);
    }

    @Test
    public void testCipherAlgorithmGetCipherByModel() {
        Cipher cipher = CipherAlgorithm.getCipher("AES");
        Assert.assertNotNull(cipher);
    }

    @Test
    public void testCipherAlgorithmGetCipherByModelNull() {
        Cipher cipher = CipherAlgorithm.getCipher("");
        Assert.assertNull(cipher);
    }

    @Test
    public void testCipherAlgorithmGetCipherByModelUnknown() {
        Cipher cipher = CipherAlgorithm.getCipher("UNKNOWN");
        Assert.assertNull(cipher);
    }

    // ==================== HashDigest ====================

    @Test
    public void testHashDigestValues() {
        HashDigest[] values = HashDigest.values();
        Assert.assertEquals(6, values.length);
        Assert.assertNotNull(HashDigest.MD5);
        Assert.assertNotNull(HashDigest.SHA1);
        Assert.assertNotNull(HashDigest.SHA224);
        Assert.assertNotNull(HashDigest.SHA256);
        Assert.assertNotNull(HashDigest.SHA384);
        Assert.assertNotNull(HashDigest.SHA512);
    }

    @Test
    public void testHashDigestGetDigest() {
        Assert.assertEquals("MD5", HashDigest.MD5.getDigest());
        Assert.assertEquals("SHA-1", HashDigest.SHA1.getDigest());
        Assert.assertEquals("SHA-224", HashDigest.SHA224.getDigest());
        Assert.assertEquals("SHA-256", HashDigest.SHA256.getDigest());
        Assert.assertEquals("SHA-384", HashDigest.SHA384.getDigest());
        Assert.assertEquals("SHA-512", HashDigest.SHA512.getDigest());
    }

    @Test
    public void testHashDigestGetMessageDigest() {
        for (HashDigest hd : HashDigest.values()) {
            MessageDigest md = hd.getMessageDigest();
            Assert.assertNotNull(md);
        }
    }

    @Test
    public void testHashDigestGetMessageDigestByString() {
        MessageDigest md = HashDigest.getMessageDigest("MD5");
        Assert.assertNotNull(md);
        MessageDigest sha256 = HashDigest.getMessageDigest("SHA-256");
        Assert.assertNotNull(sha256);
    }

    @Test
    public void testHashDigestGetMessageDigestByStringNull() {
        MessageDigest md = HashDigest.getMessageDigest("");
        Assert.assertNull(md);
    }

    @Test
    public void testHashDigestGetMessageDigestByStringUnknown() {
        MessageDigest md = HashDigest.getMessageDigest("UNKNOWN");
        Assert.assertNull(md);
    }

    // ==================== PaddingMode ====================

    @Test
    public void testPaddingModeValues() {
        PaddingMode[] values = PaddingMode.values();
        Assert.assertEquals(8, values.length);
    }

    @Test
    public void testPaddingModeGetMode() {
        Assert.assertEquals("PKCS1Padding", PaddingMode.PKCS1_PADDING.getMode());
        Assert.assertEquals("PKCS5Padding", PaddingMode.PKCS5_PADDING.getMode());
        Assert.assertEquals("PKCS7Padding", PaddingMode.PKCS7_PADDING.getMode());
        Assert.assertEquals("ISO10126Padding", PaddingMode.ISO_10126_PADDING.getMode());
        Assert.assertEquals("X9.23PADDING", PaddingMode.ANSI_X_923_PADDING.getMode());
        Assert.assertEquals("SSL3Padding", PaddingMode.SSL3_PADDING.getMode());
        Assert.assertEquals("NoPadding", PaddingMode.NO_PADDING.getMode());
        Assert.assertEquals("NoPadding", PaddingMode.ZERO_PADDING.getMode());
    }

    // ==================== RSASignature ====================

    @Test
    public void testRSASignatureValues() {
        RSASignature[] values = RSASignature.values();
        Assert.assertEquals(7, values.length);
        Assert.assertNotNull(RSASignature.NONE);
        Assert.assertNotNull(RSASignature.MD5);
        Assert.assertNotNull(RSASignature.SHA1);
        Assert.assertNotNull(RSASignature.SHA224);
        Assert.assertNotNull(RSASignature.SHA256);
        Assert.assertNotNull(RSASignature.SHA384);
        Assert.assertNotNull(RSASignature.SHA512);
    }

    @Test
    public void testRSASignatureGetModel() {
        Assert.assertEquals("NONEwithRSA", RSASignature.NONE.getModel());
        Assert.assertEquals("MD5withRSA", RSASignature.MD5.getModel());
        Assert.assertEquals("SHA1WithRSA", RSASignature.SHA1.getModel());
        Assert.assertEquals("SHA224WithRSA", RSASignature.SHA224.getModel());
        Assert.assertEquals("SHA256WithRSA", RSASignature.SHA256.getModel());
        Assert.assertEquals("SHA384WithRSA", RSASignature.SHA384.getModel());
        Assert.assertEquals("SHA512WithRSA", RSASignature.SHA512.getModel());
    }

    @Test
    public void testRSASignatureGetSignature() {
        for (RSASignature rs : RSASignature.values()) {
            Signature sig = rs.getSignature();
            Assert.assertNotNull(sig);
        }
    }

    @Test
    public void testRSASignatureGetSignatureByString() {
        Signature sig = RSASignature.getSignature("MD5withRSA");
        Assert.assertNotNull(sig);
    }

    @Test
    public void testRSASignatureGetSignatureByStringNull() {
        Signature sig = RSASignature.getSignature("");
        Assert.assertNull(sig);
    }

    @Test
    public void testRSASignatureGetSignatureByStringUnknown() {
        Signature sig = RSASignature.getSignature("UNKNOWN");
        Assert.assertNull(sig);
    }

    // ==================== SecretKeySpecMode ====================

    @Test
    public void testSecretKeySpecModeValues() {
        SecretKeySpecMode[] values = SecretKeySpecMode.values();
        Assert.assertEquals(10, values.length);
    }

    @Test
    public void testSecretKeySpecModeGetMode() {
        Assert.assertEquals("AES", SecretKeySpecMode.AES.getMode());
        Assert.assertEquals("DES", SecretKeySpecMode.DES.getMode());
        Assert.assertEquals("DESEDE", SecretKeySpecMode.DES3.getMode());
        Assert.assertEquals("SM4", SecretKeySpecMode.SM4.getMode());
        Assert.assertEquals("HmacMD5", SecretKeySpecMode.HMAC_MD5.getMode());
        Assert.assertEquals("HmacSHA1", SecretKeySpecMode.HMAC_SHA1.getMode());
        Assert.assertEquals("HmacSHA224", SecretKeySpecMode.HMAC_SHA224.getMode());
        Assert.assertEquals("HmacSHA256", SecretKeySpecMode.HMAC_SHA256.getMode());
        Assert.assertEquals("HmacSHA384", SecretKeySpecMode.HMAC_SHA384.getMode());
        Assert.assertEquals("HmacSHA512", SecretKeySpecMode.HMAC_SHA512.getMode());
    }

    @Test
    public void testSecretKeySpecModeGetSecretKeySpec() {
        byte[] key = "1234567890123456".getBytes();
        SecretKeySpec spec = SecretKeySpecMode.AES.getSecretKeySpec(key);
        Assert.assertNotNull(spec);
        Assert.assertEquals("AES", spec.getAlgorithm());
    }

    // ==================== WorkingMode ====================

    @Test
    public void testWorkingModeValues() {
        WorkingMode[] values = WorkingMode.values();
        Assert.assertEquals(7, values.length);
        Assert.assertNotNull(WorkingMode.NONE);
        Assert.assertNotNull(WorkingMode.ECB);
        Assert.assertNotNull(WorkingMode.CBC);
        Assert.assertNotNull(WorkingMode.CFB);
        Assert.assertNotNull(WorkingMode.OFB);
        Assert.assertNotNull(WorkingMode.FTP);
        Assert.assertNotNull(WorkingMode.GCM);
    }

    @Test
    public void testWorkingModeGetMode() {
        Assert.assertEquals("None", WorkingMode.NONE.getMode());
        Assert.assertEquals("ECB", WorkingMode.ECB.getMode());
        Assert.assertEquals("CBC", WorkingMode.CBC.getMode());
        Assert.assertEquals("CFB", WorkingMode.CFB.getMode());
        Assert.assertEquals("OFB", WorkingMode.OFB.getMode());
        Assert.assertEquals("FTP", WorkingMode.FTP.getMode());
        Assert.assertEquals("GCM", WorkingMode.GCM.getMode());
    }
}
