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
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.crypto.*;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.crypto.enums.CryptoCodec;
import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;
import cn.orionsec.kit.lang.utils.crypto.symmetric.EcbSymmetric;
import cn.orionsec.kit.lang.utils.crypto.symmetric.ParamSymmetric;
import cn.orionsec.kit.lang.utils.crypto.symmetric.SymmetricBuilder;
import cn.orionsec.kit.lang.utils.random.Randoms;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.Arrays;

/**
 * CryptoCodec 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/2 15:30
 */
public class CryptoCodecTests {

    private final String s = "orion-kit crypto codec test 1234567890";

    private final String aesKey = Randoms.randomAscii(CryptoConst.AES_KEY_LENGTH_BITS / 8);
    private final String aesIv = Randoms.randomAscii(CryptoConst.AES_IV_LENGTH_BITS / 8);

    @Test
    public void codecRoundTrip() {
        for (int i = 0; i < 100; i++) {
            byte[] bytes = new byte[Randoms.randomInt(1, 64)];
            Randoms.RANDOM.nextBytes(bytes);
            for (CryptoCodec codec : CryptoCodec.values()) {
                byte[] encoded = codec.encode(bytes);
                byte[] decoded = codec.decode(encoded);
                Assert.isTrue(Arrays.equals(bytes, decoded), codec + " round trip error");
                if (codec != CryptoCodec.NONE) {
                    // NONE 为原始字节 无法无损转换为字符串 只能通过 byte[] API 使用
                    Assert.isTrue(Arrays.equals(bytes, codec.decodeToBytes(codec.encodeToString(bytes))), codec + " string round trip error");
                }
            }
        }
    }

    @Test
    public void codecFormat() {
        byte[] bytes = {0x00, 0x01, 0x0a, (byte) 0xff};
        String hex = CryptoCodec.HEX.encodeToString(bytes);
        Assert.isTrue(hex.equals("00010aff"), "hex format error " + hex);
        // base64 输出可以被 Base64s 解码
        String base64 = CryptoCodec.BASE64.encodeToString(bytes);
        Assert.isTrue(Arrays.equals(bytes, Base64s.decodeToBytes(base64)), "base64 format error");
        // none 不做处理
        Assert.isTrue(Arrays.equals(bytes, CryptoCodec.NONE.encode(bytes)), "none encode error");
        Assert.isTrue(Arrays.equals(bytes, CryptoCodec.NONE.decode(bytes)), "none decode error");
    }

    @Test
    public void ecbSymmetricCodec() {
        SecretKey key = Keys.getSecretKey(aesKey, CipherAlgorithm.AES);
        for (CryptoCodec codec : CryptoCodec.values()) {
            EcbSymmetric sy = SymmetricBuilder.aes()
                    .secretKey(key)
                    .codec(codec)
                    .buildEcb();
            byte[] enc = sy.encrypt(Strings.bytes(s));
            byte[] dec = sy.decrypt(enc);
            Assert.isTrue(Arrays.equals(Strings.bytes(s), dec), codec + " ecb bytes error");
            if (codec != CryptoCodec.NONE) {
                // NONE 为原始字节 无法通过字符串 API 无损往返
                Assert.isTrue(s.equals(sy.decryptAsString(sy.encryptAsString(s))), codec + " ecb string error");
                Assert.isTrue(sy.verify(s, sy.encryptAsString(s)), codec + " ecb verify error");
            }
        }
    }

    @Test
    public void paramSymmetricCodec() {
        SecretKey key = Keys.getSecretKey(aesKey, CipherAlgorithm.AES);
        for (CryptoCodec codec : CryptoCodec.values()) {
            ParamSymmetric sy = SymmetricBuilder.aes()
                    .workingMode(WorkingMode.CBC)
                    .secretKey(key)
                    .ivSpec(aesIv)
                    .codec(codec)
                    .buildParam();
            byte[] enc = sy.encrypt(Strings.bytes(s));
            byte[] dec = sy.decrypt(enc);
            Assert.isTrue(Arrays.equals(Strings.bytes(s), dec), codec + " cbc bytes error");
            if (codec != CryptoCodec.NONE) {
                // NONE 为原始字节 无法通过字符串 API 无损往返
                Assert.isTrue(s.equals(sy.decryptAsString(sy.encryptAsString(s))), codec + " cbc string error");
            }
        }
    }

    @Test
    public void builderCodec() {
        // hex
        String hexEnc = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(aesKey)
                .codec(CryptoCodec.HEX)
                .buildEcb()
                .encryptAsString(s);
        Assert.isTrue(hexEnc.matches("[0-9a-f]+"), "hex format error " + hexEnc);
        // base64
        String b64Enc = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(aesKey)
                .codec(CryptoCodec.BASE64)
                .buildEcb()
                .encryptAsString(s);
        Assert.isTrue(s.equals(AES.decrypt(b64Enc, aesKey)), "builder base64 error");
        // none 原始字节
        byte[] noneEnc = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(aesKey)
                .codec(CryptoCodec.NONE)
                .buildEcb()
                .encrypt(Strings.bytes(s));
        byte[] noneDec = SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(aesKey)
                .codec(CryptoCodec.NONE)
                .buildEcb()
                .decrypt(noneEnc);
        Assert.isTrue(Arrays.equals(Strings.bytes(s), noneDec), "builder none error");
    }

    @Test
    public void defaultCodecBackwardCompatible() {
        // 不设置 codec 默认为 BASE64 与旧版本行为保持一致
        SecretKey key = Keys.getSecretKey(aesKey, CipherAlgorithm.AES);
        EcbSymmetric defaultCodec = SymmetricBuilder.aes()
                .secretKey(key)
                .buildEcb();
        EcbSymmetric explicitBase64 = SymmetricBuilder.aes()
                .secretKey(key)
                .codec(CryptoCodec.BASE64)
                .buildEcb();
        byte[] enc = defaultCodec.encrypt(Strings.bytes(s));
        byte[] encNew = explicitBase64.encrypt(Strings.bytes(s));
        Assert.isTrue(Arrays.equals(enc, encNew), "backward compatible error");
        Assert.isTrue(Arrays.equals(Strings.bytes(s), explicitBase64.decrypt(enc)), "backward compatible error");
    }

    @Test
    public void rc4HexAndBase64() {
        RC4 rc4 = new RC4("orion-kit-rc4-key");
        String e1 = rc4.encryptHex(s);
        Assert.isTrue(e1.matches("[0-9a-f]+"), "rc4 hex format error");
        Assert.isTrue(s.equals(rc4.decryptHex(e1)), "rc4 hex error");
        String e2 = rc4.encryptBase64(s);
        Assert.isTrue(s.equals(rc4.decryptBase64(e2)), "rc4 base64 error");
        // 与旧入口互通
        Assert.isTrue(s.equals(rc4.decrypt(rc4.encrypt(s))), "rc4 default error");
    }

    @Test
    public void sm2HexAndBase64() {
        String[] keyPair = SM2.generateKeyPair();
        String publicKey = keyPair[0];
        String privateKey = keyPair[1];
        // hex
        String e1 = SM2.encrypt(s, publicKey);
        Assert.isTrue(e1 != null && e1.matches("[0-9a-f]+"), "sm2 hex format error");
        Assert.isTrue(s.equals(SM2.decrypt(e1, privateKey)), "sm2 hex error");
    }

}
