package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.SecretKeySpecMode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Signatures 单元测试
 */
public class SignaturesTest {

    private static final String PLAIN_TEXT = "Hello, Signatures!";
    private static final String HMAC_KEY = "my-hmac-key";

    @Test
    public void testMd5String() {
        String hash = Signatures.md5(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
        // 相同输入产生相同输出
        Assert.assertEquals(hash, Signatures.md5(PLAIN_TEXT));
    }

    @Test
    public void testMd5Bytes() {
        String hash = Signatures.md5(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
    }

    @Test
    public void testMd5WithSalt() {
        String hash = Signatures.md5(PLAIN_TEXT, "salt");
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
        // 不同 salt 产生不同的结果
        String hash2 = Signatures.md5(PLAIN_TEXT, "salt2");
        Assert.assertNotEquals(hash, hash2);
    }

    @Test
    public void testMd5WithSaltAndTimes() {
        String hash = Signatures.md5(PLAIN_TEXT, "salt", 3);
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
    }

    @Test
    public void testSha1() {
        String hash = Signatures.sha1(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(40, hash.length());
        Assert.assertEquals(hash, Signatures.sha1(PLAIN_TEXT));
    }

    @Test
    public void testSha1Bytes() {
        String hash = Signatures.sha1(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(40, hash.length());
    }

    @Test
    public void testSha224() {
        String hash = Signatures.sha224(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(56, hash.length());
    }

    @Test
    public void testSha224Bytes() {
        String hash = Signatures.sha224(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(56, hash.length());
    }

    @Test
    public void testSha256() {
        String hash = Signatures.sha256(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(64, hash.length());
    }

    @Test
    public void testSha256Bytes() {
        String hash = Signatures.sha256(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(64, hash.length());
    }

    @Test
    public void testSha384() {
        String hash = Signatures.sha384(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(96, hash.length());
    }

    @Test
    public void testSha384Bytes() {
        String hash = Signatures.sha384(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(96, hash.length());
    }

    @Test
    public void testSha512() {
        String hash = Signatures.sha512(PLAIN_TEXT);
        Assert.assertNotNull(hash);
        Assert.assertEquals(128, hash.length());
    }

    @Test
    public void testSha512Bytes() {
        String hash = Signatures.sha512(PLAIN_TEXT.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(128, hash.length());
    }

    @Test
    public void testSignWithType() {
        String md5 = Signatures.sign(PLAIN_TEXT, "MD5");
        Assert.assertNotNull(md5);
        Assert.assertEquals(32, md5.length());

        String sha256 = Signatures.sign(PLAIN_TEXT, "SHA-256");
        Assert.assertNotNull(sha256);
        Assert.assertEquals(64, sha256.length());
    }

    @Test
    public void testSignBytesWithType() {
        String md5 = Signatures.sign(PLAIN_TEXT.getBytes(), "MD5");
        Assert.assertNotNull(md5);
        Assert.assertEquals(32, md5.length());
    }

    @Test
    public void testHmacMd5() {
        String hash = Signatures.hmacMd5(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
        // 相同输入产生相同输出
        Assert.assertEquals(hash, Signatures.hmacMd5(PLAIN_TEXT, HMAC_KEY));
    }

    @Test
    public void testHmacMd5Bytes() {
        String hash = Signatures.hmacMd5(PLAIN_TEXT.getBytes(), HMAC_KEY.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(32, hash.length());
    }

    @Test
    public void testHmacSha1() {
        String hash = Signatures.hmacSha1(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(40, hash.length());
    }

    @Test
    public void testHmacSha1Bytes() {
        String hash = Signatures.hmacSha1(PLAIN_TEXT.getBytes(), HMAC_KEY.getBytes());
        Assert.assertNotNull(hash);
        Assert.assertEquals(40, hash.length());
    }

    @Test
    public void testHmacSha224() {
        String hash = Signatures.hmacSha224(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(56, hash.length());
    }

    @Test
    public void testHmacSha256() {
        String hash = Signatures.hmacSha256(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(64, hash.length());
    }

    @Test
    public void testHmacSha384() {
        String hash = Signatures.hmacSha384(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(96, hash.length());
    }

    @Test
    public void testHmacSha512() {
        String hash = Signatures.hmacSha512(PLAIN_TEXT, HMAC_KEY);
        Assert.assertNotNull(hash);
        Assert.assertEquals(128, hash.length());
    }

    @Test
    public void testHmacSignWithMode() {
        String hash = Signatures.hmacSign(PLAIN_TEXT, HMAC_KEY, SecretKeySpecMode.HMAC_SHA256);
        Assert.assertNotNull(hash);
        Assert.assertEquals(64, hash.length());
    }

    @Test
    public void testHmacSignBytesWithMode() {
        String hash = Signatures.hmacSign(PLAIN_TEXT.getBytes(), HMAC_KEY.getBytes(), SecretKeySpecMode.HMAC_SHA256);
        Assert.assertNotNull(hash);
        Assert.assertEquals(64, hash.length());
    }

    @Test
    public void testToHex() {
        byte[] data = {0x01, 0x0A, (byte) 0xFF};
        String hex = Signatures.toHex(data);
        Assert.assertEquals("010aff", hex);
    }

    @Test
    public void testDifferentInputsProduceDifferentHashes() {
        String hash1 = Signatures.md5("hello");
        String hash2 = Signatures.md5("world");
        Assert.assertNotEquals(hash1, hash2);
    }
}
