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
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to
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
package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.math.Hex;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * SM2 加密工具类 (C1C3C2 模式)
 * <p>
 * 密文格式: C1(X||Y 128位hex) + C3(32字节) + C2 与前端 sm-crypto 保持一致
 * 公钥格式: 04 || X || Y (130位hex) 私钥格式: D (64位hex)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/2 15:30
 */
public class SM2 {

    private static final ECDomainParameters DOMAIN_PARAMS;

    private SM2() {
    }

    static {
        DOMAIN_PARAMS = new ECDomainParameters(
                GMNamedCurves.getByName("sm2p256v1").getCurve(),
                GMNamedCurves.getByName("sm2p256v1").getG(),
                GMNamedCurves.getByName("sm2p256v1").getN(),
                GMNamedCurves.getByName("sm2p256v1").getH());
    }

    // -------------------- keypair --------------------

    /**
     * 生成 SM2 密钥对
     *
     * @return pair
     * [0] 公钥 hex (04||X||Y)
     * [1] 私钥 hex (D)
     */
    public static String[] generateKeyPair() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(new ECKeyGenerationParameters(DOMAIN_PARAMS, new SecureRandom()));
        AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
        ECPoint q = ((ECPublicKeyParameters) keyPair.getPublic()).getQ();
        BigInteger d = ((ECPrivateKeyParameters) keyPair.getPrivate()).getD();
        return new String[]{
                Hex.bytesToHex(q.getEncoded(false)),
                Strings.leftPad(d.toString(16), 64, "0")
        };
    }

    // -------------------- enc --------------------

    /**
     * SM2 加密
     *
     * @param s            明文
     * @param publicKeyHex 公钥 hex (04||X||Y)
     * @return 密文 hex (C1C3C2 无 04 前缀)
     */
    public static String encrypt(String s, String publicKeyHex) {
        byte[] bytes = encrypt(Strings.bytes(s), publicKeyHex);
        if (bytes != null) {
            return Hex.bytesToHex(bytes);
        }
        return null;
    }

    /**
     * SM2 加密
     *
     * @param bs           明文
     * @param publicKeyHex 公钥 hex (04||X||Y)
     * @return 密文 (C1C3C2 无 04 前缀)
     */
    public static byte[] encrypt(byte[] bs, String publicKeyHex) {
        try {
            ECPoint q = DOMAIN_PARAMS.getCurve().decodePoint(Hex.hexToBytes(publicKeyHex));
            SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            engine.init(true, new ParametersWithRandom(new ECPublicKeyParameters(q, DOMAIN_PARAMS), new SecureRandom()));
            byte[] out = engine.processBlock(bs, 0, bs.length);
            // 去掉 C1 的 04 前缀 与前端 sm-crypto 输出格式保持一致
            byte[] result = new byte[out.length - 1];
            System.arraycopy(out, 1, result, 0, result.length);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- dec --------------------

    /**
     * SM2 解密
     *
     * @param s             密文 hex (C1C3C2)
     * @param privateKeyHex 私钥 hex (D)
     * @return 明文
     */
    public static String decrypt(String s, String privateKeyHex) {
        byte[] bytes = decrypt(Hex.hexToBytes(s), privateKeyHex);
        if (bytes != null) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * SM2 解密
     *
     * @param bs            密文 (C1C3C2)
     * @param privateKeyHex 私钥 hex (D)
     * @return 明文
     */
    public static byte[] decrypt(byte[] bs, String privateKeyHex) {
        BigInteger d = new BigInteger(privateKeyHex, 16);
        // 尝试按原始格式解密 (C1 带 04 前缀)
        byte[] plain = doDecrypt(bs, d);
        if (plain != null) {
            return plain;
        }
        // 尝试按无 04 前缀格式解密 (前端 sm-crypto 输出)
        byte[] input = new byte[bs.length + 1];
        input[0] = 0x04;
        System.arraycopy(bs, 0, input, 1, bs.length);
        return doDecrypt(input, d);
    }

    private static byte[] doDecrypt(byte[] input, BigInteger d) {
        try {
            SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            engine.init(false, new ECPrivateKeyParameters(d, DOMAIN_PARAMS));
            return engine.processBlock(input, 0, input.length);
        } catch (Exception e) {
            return null;
        }
    }

}
