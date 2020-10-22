package com.orion.utils.crypto;

import com.orion.lang.wrapper.Args;
import com.orion.utils.Exceptions;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.RSASignature;

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

import static com.orion.utils.crypto.Base64s.base64Decode;
import static com.orion.utils.crypto.Base64s.base64Encode;
import static com.orion.utils.crypto.Keys.getKey;

/**
 * RSA 加密类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/27 16:45
 */
public class RSA {

    private RSA() {
    }

    public static final KeyFactory RSA_KEY_FACTORY;

    static {
        KeyFactory rsaKeyFactory = null;
        try {
            rsaKeyFactory = KeyFactory.getInstance("RSA");
        } catch (Exception e) {
            // ignore
        }
        RSA_KEY_FACTORY = rsaKeyFactory;

    }

    // ------------------ ENC ------------------

    /**
     * RSA公钥加密
     *
     * @param s         明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String s, String publicKey) {
        byte[] bytes = encrypt(s.getBytes(), getPublicKey(publicKey));
        if (bytes != null) {
            return new String(bytes);
        } else {
            return null;
        }
    }

    /**
     * RSA公钥加密
     *
     * @param s         明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String s, PublicKey publicKey) {
        byte[] bytes = encrypt(s.getBytes(), publicKey);
        if (bytes != null) {
            return new String(bytes);
        } else {
            return null;
        }
    }

    /**
     * RSA公钥加密
     *
     * @param bs        bytes
     * @param publicKey 公钥
     * @return 密文
     */
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
            return base64Encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ DEC ------------------

    /**
     * RSA私钥解密
     *
     * @param s          密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decrypt(String s, String privateKey) {
        byte[] bytes = decrypt(s.getBytes(), getPrivateKey(privateKey));
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥解密
     *
     * @param s          密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decrypt(String s, PrivateKey privateKey) {
        byte[] bytes = decrypt(s.getBytes(), privateKey);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥解密
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 明文
     */
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
            return cipher.doFinal(base64Decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ SIGN ------------------

    /**
     * RSA私钥签名
     *
     * @param s          明文
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String s, String privateKey) {
        byte[] bytes = sign(s.getBytes(), getPrivateKey(privateKey), RSASignature.MD5);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥签名
     *
     * @param s          明文
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String s, PrivateKey privateKey) {
        byte[] bytes = sign(s.getBytes(), privateKey, RSASignature.MD5);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 签名
     */
    public static byte[] sign(byte[] bs, String privateKey) {
        return sign(bs, getPrivateKey(privateKey), RSASignature.MD5);
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 签名
     */
    public static byte[] sign(byte[] bs, PrivateKey privateKey) {
        return sign(bs, privateKey, RSASignature.MD5);
    }

    /**
     * RSA私钥签名
     *
     * @param s          明文
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String s, String privateKey, RSASignature signModel) {
        byte[] bytes = sign(s.getBytes(), getPrivateKey(privateKey), signModel);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥签名
     *
     * @param s          明文
     * @param privateKey 私钥
     * @param signModel  com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return 签名
     */
    public static String sign(String s, PrivateKey privateKey, RSASignature signModel) {
        byte[] bytes = sign(s.getBytes(), privateKey, signModel);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @param signModel  com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return 签名
     */
    public static byte[] sign(byte[] bs, String privateKey, RSASignature signModel) {
        return sign(bs, getPrivateKey(privateKey), signModel);
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @param signModel  com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return 签名
     */
    public static byte[] sign(byte[] bs, PrivateKey privateKey, RSASignature signModel) {
        try {
            Signature signature = signModel.getSignature();
            signature.initSign(privateKey);
            signature.update(bs);
            return base64Encode(signature.sign());
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ VERIFY ------------------

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean verify(String s, String publicKey, String sign) {
        return verify(s.getBytes(), getPublicKey(publicKey), sign.getBytes(), RSASignature.MD5);
    }

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean verify(String s, PublicKey publicKey, String sign) {
        return verify(s.getBytes(), publicKey, sign.getBytes(), RSASignature.MD5);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean verify(byte[] bs, String publicKey, byte[] sign) {
        return verify(bs, getPublicKey(publicKey), sign, RSASignature.MD5);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean verify(byte[] bs, PublicKey publicKey, byte[] sign) {
        return verify(bs, publicKey, sign, RSASignature.MD5);
    }

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @param signModel com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return true验证成功
     */
    public static boolean verify(String s, String publicKey, String sign, RSASignature signModel) {
        return verify(s.getBytes(), getPublicKey(publicKey), sign.getBytes(), signModel);
    }

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @param signModel com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return true验证成功
     */
    public static boolean verify(String s, PublicKey publicKey, String sign, RSASignature signModel) {
        return verify(s.getBytes(), publicKey, sign.getBytes(), signModel);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @param signModel com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return true验证成功
     */
    public static boolean verify(byte[] bs, String publicKey, byte[] sign, RSASignature signModel) {
        return verify(bs, getPublicKey(publicKey), sign, signModel);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @param signModel com.orion.utils.encrypt.EncryptConst.RSASignature
     * @return true验证成功
     */
    public static boolean verify(byte[] bs, PublicKey publicKey, byte[] sign, RSASignature signModel) {
        try {
            Signature signature = signModel.getSignature();
            signature.initVerify(publicKey);
            signature.update(bs);
            return signature.verify(base64Decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    // ------------------ KEY ------------------

    /**
     * RSA pkcs8 私钥文件 -> RSAPrivateKey
     *
     * @param file 私钥文件
     * @return RSAPrivateKey
     */
    public static RSAPrivateKey getPrivateKey(File file) {
        try {
            byte[] bytes = getKey(file).getBytes(StandardCharsets.ISO_8859_1);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(base64Decode(bytes));
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA pkcs8 私钥 -> RSAPrivateKey
     *
     * @param key 私钥
     * @return RSAPrivateKey
     */
    public static RSAPrivateKey getPrivateKey(String key) {
        try {
            byte[] bytes = key.getBytes();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(base64Decode(bytes));
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA pkcs8 私钥 -> RSAPrivateKey
     *
     * @param key 私钥
     * @return RSAPrivateKey
     */
    public static RSAPrivateKey getPrivateKey(byte[] key) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(base64Decode(key));
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA 公钥文件 -> getRSAPublicKey
     *
     * @param file 公钥文件
     * @return getRSAPublicKey
     */
    public static RSAPublicKey getPublicKey(File file) {
        try {
            byte[] bytes = getKey(file).getBytes(StandardCharsets.ISO_8859_1);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(base64Decode(bytes));
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA 公钥文件 -> getRSAPublicKey
     *
     * @param key 公钥
     * @return getRSAPublicKey
     */
    public static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] bytes = key.getBytes(StandardCharsets.ISO_8859_1);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(base64Decode(bytes));
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(spec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA 公钥文件 -> getRSAPublicKey
     *
     * @param key 公钥
     * @return getRSAPublicKey
     */
    public static RSAPublicKey getPublicKey(byte[] key) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(base64Decode(key));
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
    public static Args.Two<RSAPublicKey, RSAPrivateKey> generatorKeys() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return Args.of(((RSAPublicKey) keyPair.getPublic()), ((RSAPrivateKey) keyPair.getPrivate()));
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 随机生成 公私钥对
     *
     * @param size 512 ~ 16384
     * @return PublicKey PrivateKey
     */
    public static Args.Two<RSAPublicKey, RSAPrivateKey> generatorKeys(int size) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(size, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return Args.of(((RSAPublicKey) keyPair.getPublic()), ((RSAPrivateKey) keyPair.getPrivate()));
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

}
