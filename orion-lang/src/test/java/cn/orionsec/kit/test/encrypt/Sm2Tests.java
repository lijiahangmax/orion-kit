/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.test.encrypt;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.SM2;
import cn.orionsec.kit.lang.utils.math.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * SM2 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/2 15:30
 */
public class Sm2Tests {

    private final String s = "orion-kit sm2 test 1234567890";

    @Test
    public void generateKeyPair() {
        for (int i = 0; i < 10; i++) {
            String[] keyPair = SM2.generateKeyPair();
            String publicKey = keyPair[0];
            String privateKey = keyPair[1];
            // 公钥 04 || X || Y (130 位 hex)
            Assert.isTrue(publicKey.length() == 130, "public key length error " + publicKey.length());
            Assert.isTrue(publicKey.startsWith("04"), "public key prefix error");
            Assert.isTrue(publicKey.matches("[0-9a-f]+"), "public key format error");
            // 私钥 D (64 位 hex)
            Assert.isTrue(privateKey.length() == 64, "private key length error " + privateKey.length());
            Assert.isTrue(privateKey.matches("[0-9a-f]+"), "private key format error");
            // 密钥对可用
            String enc = SM2.encrypt(s, publicKey);
            Assert.isTrue(s.equals(SM2.decrypt(enc, privateKey)), "keyPair round trip error");
        }
    }

    @Test
    public void encryptAndDecrypt() {
        String[] keyPair = SM2.generateKeyPair();
        String enc = SM2.encrypt(s, keyPair[0]);
        Assert.isTrue(enc != null, "encrypt error");
        // 密文为 hex 字符串 (C1C3C2 无 04 前缀: C1=64 + C3=32 + C2=明文长度)
        Assert.isTrue(enc.matches("[0-9a-f]+"), "cipher hex format error " + enc);
        Assert.isTrue(enc.length() == (64 + 32 + s.getBytes(StandardCharsets.UTF_8).length) * 2, "cipher length error " + enc.length());
        Assert.isTrue(s.equals(SM2.decrypt(enc, keyPair[1])), "decrypt error");
        // 多次加密结果不同 (随机数)
        String enc2 = SM2.encrypt(s, keyPair[0]);
        Assert.isTrue(!enc.equals(enc2), "random cipher error");
        Assert.isTrue(s.equals(SM2.decrypt(enc2, keyPair[1])), "decrypt error 2");
    }

    @Test
    public void encryptAndDecryptBytes() {
        String[] keyPair = SM2.generateKeyPair();
        byte[] plain = Strings.bytes(s);
        byte[] enc = SM2.encrypt(plain, keyPair[0]);
        Assert.isTrue(enc != null, "encrypt bytes error");
        // 原始密文无 04 前缀 (C1C3C2: C1=64 + C3=32 + C2=明文长度)
        Assert.isTrue(enc.length == 64 + 32 + plain.length, "cipher length error");
        byte[] dec = SM2.decrypt(enc, keyPair[1]);
        Assert.isTrue(Arrays.equals(plain, dec), "decrypt bytes error");
        // byte[] 密文转 hex 后可以走字符串解密
        String hexEnc = Hex.bytesToHex(enc);
        Assert.isTrue(s.equals(SM2.decrypt(hexEnc, keyPair[1])), "bytes to hex decrypt error");
    }

    @Test
    public void decryptWith04Prefix() {
        String[] keyPair = SM2.generateKeyPair();
        // 自行构造带 04 前缀的密文 (BC 原始输出格式) 解密兼容
        byte[] cipher = SM2.encrypt(Strings.bytes(s), keyPair[0]);
        byte[] withPrefix = new byte[cipher.length + 1];
        withPrefix[0] = 0x04;
        System.arraycopy(cipher, 0, withPrefix, 1, cipher.length);
        byte[] dec = SM2.decrypt(withPrefix, keyPair[1]);
        Assert.isTrue(Arrays.equals(Strings.bytes(s), dec), "decrypt with 04 prefix error");
    }

    @Test
    public void chineseAndEmptyText() {
        String[] keyPair = SM2.generateKeyPair();
        // 中文
        String s1 = "你好世界 orion-kit";
        Assert.isTrue(s1.equals(SM2.decrypt(SM2.encrypt(s1, keyPair[0]), keyPair[1])), "chinese error");
        // 空字符串 BC SM2Engine 不支持 (DataLengthException) 返回 null
        Assert.isTrue(SM2.encrypt("", keyPair[0]) == null, "empty input should be null");
        Assert.isTrue(SM2.encrypt(new byte[0], keyPair[0]) == null, "empty bytes input should be null");
    }

    @Test
    public void randomRoundTrip() {
        for (int i = 0; i < 100; i++) {
            String[] keyPair = SM2.generateKeyPair();
            String val = Strings.randomChars((int) (Math.random() * 100) + 1);
            Assert.isTrue(val.equals(SM2.decrypt(SM2.encrypt(val, keyPair[0]), keyPair[1])), "round trip error " + i);
        }
    }

}
