/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.crypto.enums.RSASignature;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static cn.orionsec.kit.lang.utils.codec.Base64s.decode;
import static cn.orionsec.kit.lang.utils.codec.Base64s.encode;

/**
 * RSA 加密类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/27 16:45
 */
public class RSA {

    public static final KeyFactory RSA_KEY_FACTORY;

    private RSA() {
    }

    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance(CryptoConst.RSA);
        } catch (Exception e) {
            throw Exceptions.unsupported("unsupported RSA algorithm", e);
        }
    }

    // -------------------- ENC --------------------

    public static String encrypt(String s, String publicKey) {
        byte[] bytes = encrypt(Strings.bytes(s), getPublicKey(publicKey));
        if (bytes != null) {
            return new String(bytes);
        } else {
            return null;
        }
    }

    public static String encrypt(String s, PublicKey publicKey) {
        byte[] bytes = encrypt(Strings.bytes(s), publicKey);
        if (bytes != null) {
            return new String(bytes);
        } else {
            return null;
        }
    }

    public static byte[] encrypt(byte[] bs, String publicKey) {
        return encrypt(bs, getPublicKey(publicKey));
    }

    /**
     * RSA公钥加密
     *
     * @param bs        bytes
     * @param publicKey 公钥
     * @return 密文
     */
    public static byte[] encrypt(byte[] bs, PublicKey publicKey) {
        try {
            Cipher cipher = CipherAlgorithm.RSA.getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- DEC --------------------

    public static String decrypt(String s, String privateKey) {
        byte[] bytes = decrypt(Strings.bytes(s), getPrivateKey(privateKey));
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, PrivateKey privateKey) {
        byte[] bytes = decrypt(Strings.bytes(s), privateKey);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] decrypt(byte[] bs, String privateKey) {
        return decrypt(bs, getPrivateKey(privateKey));
    }

    /**
     * RSA私钥解密
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 明文
     */
    public static byte[] decrypt(byte[] bs, PrivateKey privateKey) {
        try {
            Cipher cipher = CipherAlgorithm.RSA.getCipher();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- SIGN --------------------

    public static String sign(String s, String privateKey) {
        byte[] bytes = sign(Strings.bytes(s), getPrivateKey(privateKey), RSASignature.MD5);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String sign(String s, PrivateKey privateKey) {
        byte[] bytes = sign(Strings.bytes(s), privateKey, RSASignature.MD5);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] sign(byte[] bs, String privateKey) {
        return sign(bs, getPrivateKey(privateKey), RSASignature.MD5);
    }

    public static byte[] sign(byte[] bs, PrivateKey privateKey) {
        return sign(bs, privateKey, RSASignature.MD5);
    }

    public static String sign(String s, String privateKey, RSASignature signModel) {
        byte[] bytes = sign(Strings.bytes(s), getPrivateKey(privateKey), signModel);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String sign(String s, PrivateKey privateKey, RSASignature signModel) {
        byte[] bytes = sign(Strings.bytes(s), privateKey, signModel);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] sign(byte[] bs, String privateKey, RSASignature signModel) {
        return sign(bs, getPrivateKey(privateKey), signModel);
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @param signModel  签名类型
     * @return 签名
     * @see RSASignature
     */
    public static byte[] sign(byte[] bs, PrivateKey privateKey, RSASignature signModel) {
        try {
            Signature signature = signModel.getSignature();
            signature.initSign(privateKey);
            signature.update(bs);
            return encode(signature.sign());
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- VERIFY --------------------

    public static boolean verify(String s, String publicKey, String sign) {
        return verify(Strings.bytes(s), getPublicKey(publicKey), Strings.bytes(sign), RSASignature.MD5);
    }

    public static boolean verify(String s, PublicKey publicKey, String sign) {
        return verify(Strings.bytes(s), publicKey, Strings.bytes(sign), RSASignature.MD5);
    }

    public static boolean verify(byte[] bs, String publicKey, byte[] sign) {
        return verify(bs, getPublicKey(publicKey), sign, RSASignature.MD5);
    }

    public static boolean verify(byte[] bs, PublicKey publicKey, byte[] sign) {
        return verify(bs, publicKey, sign, RSASignature.MD5);
    }

    public static boolean verify(String s, String publicKey, String sign, RSASignature signModel) {
        return verify(Strings.bytes(s), getPublicKey(publicKey), Strings.bytes(sign), signModel);
    }

    public static boolean verify(String s, PublicKey publicKey, String sign, RSASignature signModel) {
        return verify(Strings.bytes(s), publicKey, Strings.bytes(sign), signModel);
    }

    public static boolean verify(byte[] bs, String publicKey, byte[] sign, RSASignature signModel) {
        return verify(bs, getPublicKey(publicKey), sign, signModel);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @param signModel 签名类型
     * @return true验证成功
     * @see RSASignature
     */
    public static boolean verify(byte[] bs, PublicKey publicKey, byte[] sign, RSASignature signModel) {
        try {
            Signature signature = signModel.getSignature();
            signature.initVerify(publicKey);
            signature.update(bs);
            return signature.verify(decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------- KEY --------------------

    public static RSAPrivateKey getPrivateKey(File file) {
        byte[] bytes = Strings.bytes(Keys.getKey(file), StandardCharsets.ISO_8859_1);
        return getPrivateKey(bytes);
    }

    public static RSAPrivateKey getPrivateKey(String key) {
        byte[] bytes = Strings.bytes(key, StandardCharsets.ISO_8859_1);
        return getPrivateKey(bytes);
    }

    /**
     * RSA pkcs8 私钥 -> RSAPrivateKey
     *
     * @param key 私钥
     * @return RSAPrivateKey
     */
    public static RSAPrivateKey getPrivateKey(byte[] key) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode(key));
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(spec);
        } catch (Exception e) {
            return null;
        }
    }

    public static RSAPublicKey getPublicKey(File file) {
        byte[] bytes = Strings.bytes(Keys.getKey(file), StandardCharsets.ISO_8859_1);
        return getPublicKey(bytes);
    }

    public static RSAPublicKey getPublicKey(String key) {
        byte[] bytes = Strings.bytes(key, StandardCharsets.ISO_8859_1);
        return getPublicKey(bytes);
    }

    /**
     * RSA 公钥文件 -> getRSAPublicKey
     *
     * @param key 公钥
     * @return getRSAPublicKey
     */
    public static RSAPublicKey getPublicKey(byte[] key) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decode(key));
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA 私钥文件 -> RSA 公钥文件
     *
     * @param key 私钥
     * @return 公钥
     */
    public static RSAPublicKey getPublicKey(RSAPrivateKey key) {
        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(key.getModulus(), ((RSAPrivateCrtKey) key).getPublicExponent());
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(publicKeySpec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 随机生成 公私钥对
     *
     * @return PublicKey PrivateKey
     */
    public static Pair<RSAPublicKey, RSAPrivateKey> generatorKeys() {
        return generatorKeys(CryptoConst.RSA_KEY_LENGTH);
    }

    /**
     * 随机生成 公私钥对
     *
     * @param length 512 ~ 16384
     * @return PublicKey PrivateKey
     */
    public static Pair<RSAPublicKey, RSAPrivateKey> generatorKeys(int length) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(CryptoConst.RSA);
            keyPairGen.initialize(length, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return Pair.of(((RSAPublicKey) keyPair.getPublic()), ((RSAPrivateKey) keyPair.getPrivate()));
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

}
