package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.crypto.enums.PaddingMode;
import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;
import cn.orionsec.kit.lang.utils.crypto.symmetric.EcbSymmetric;
import cn.orionsec.kit.lang.utils.crypto.symmetric.ParamSymmetric;
import cn.orionsec.kit.lang.utils.crypto.symmetric.SymmetricBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * SymmetricBuilder 及 symmetric 包单元测试
 */
public class SymmetricBuilderTest {

    private static final String KEY_16 = "1234567890abcdef";
    private static final String IV_16 = "abcdef1234567890";
    private static final String PLAIN_TEXT = "Hello Symmetric!";

    @Test
    public void testAesEcbBuilder() {
        EcbSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(KEY_16)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testAesCbcBuilder() {
        ParamSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(KEY_16)
                .ivSpec(IV_16)
                .buildParam();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testAesCfbBuilder() {
        ParamSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.CFB)
                .secretKey(KEY_16)
                .ivSpec(IV_16)
                .buildParam();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testAesOfbBuilder() {
        ParamSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.OFB)
                .secretKey(KEY_16)
                .ivSpec(IV_16)
                .buildParam();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testAesGcmBuilder() {
        String gcm = "123456789012";
        ParamSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(KEY_16)
                .gcmSpec(gcm)
                .aad("myaad")
                .buildParam();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDesEcbBuilder() {
        String desKey = "12345678";
        EcbSymmetric sym = SymmetricBuilder.des()
                .workingMode(WorkingMode.ECB)
                .secretKey(desKey)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testDes3EcbBuilder() {
        String des3Key = "123456789012345678901234";
        EcbSymmetric sym = SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(des3Key)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testSm4EcbBuilder() {
        EcbSymmetric sym = SymmetricBuilder.sm4()
                .workingMode(WorkingMode.ECB)
                .secretKey(KEY_16)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testGeneratorSecretKeyBuilder() {
        EcbSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .generatorSecretKey("anyKey")
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testSecretKeyBase64Builder() {
        SecretKey key = Keys.getSecretKey(KEY_16, CipherAlgorithm.AES);
        String base64Key = Keys.getSecretKey(key);
        EcbSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKeyBase64(base64Key)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }

    @Test
    public void testVerify() {
        ParamSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(KEY_16)
                .ivSpec(IV_16)
                .buildParam();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertTrue(sym.verify(PLAIN_TEXT, encrypted));
        Assert.assertFalse(sym.verify("wrong", encrypted));
    }

    @Test
    public void testCustomPaddingMode() {
        EcbSymmetric sym = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .paddingMode(PaddingMode.PKCS5_PADDING)
                .secretKey(KEY_16)
                .buildEcb();
        String encrypted = sym.encryptAsString(PLAIN_TEXT);
        Assert.assertNotNull(encrypted);
        String decrypted = sym.decryptAsString(encrypted);
        Assert.assertEquals(PLAIN_TEXT, decrypted);
    }
}
