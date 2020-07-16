package com.orion.utils;

import com.orion.lang.wrapper.Args;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 加密工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/10 9:46
 */
@SuppressWarnings("ALL")
public class Encrypts {

    private static final String AES_ALGORITHM = "SHA1PRNG";
    private static final String DES_ALGORITHM = "desede/CBC/PKCS5Padding";
    private static final String DES_SECRET = "desede";
    private static final String HEX_CHARS = "0123456789ABCDEF";
    private static final char[] HEX_CHAR_ARRAY = HEX_CHARS.toCharArray();

    private Encrypts() {
    }

    // ------------------ 散列加密 ------------------

    /**
     * MD5加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String md5(String s) {
        return hashEncrypt(s, "MD5");
    }

    /**
     * MD5加密
     *
     * @param s    明文
     * @param salt 盐
     * @return 密文
     */
    public static String md5(String s, String salt) {
        return md5(s, salt, 1);
    }

    /**
     * MD5加密
     *
     * @param s     明文
     * @param salt  盐
     * @param times 加密次数
     * @return 密文
     */
    public static String md5(String s, String salt, int times) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            for (int i = 0; i < times; i++) {
                md.update(salt.getBytes());
            }
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * SHA1加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String sha1(String s) {
        return hashEncrypt(s, "SHA-1");
    }

    /**
     * SHA224加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String sha224(String s) {
        return hashEncrypt(s, "SHA-224");
    }

    /**
     * SHA256加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String sha256(String s) {
        return hashEncrypt(s, "SHA-256");
    }

    /**
     * SHA384加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String sha384(String s) {
        return hashEncrypt(s, "SHA-384");
    }

    /**
     * SHA512加密
     *
     * @param s 明文
     * @return 密文
     */
    public static String sha512(String s) {
        return hashEncrypt(s, "SHA-512");
    }

    /**
     * hash签名
     *
     * @param s    明文
     * @param type 加密类型
     * @return 密文
     */
    public static String sign(String s, String type) {
        return hashEncrypt(s, type);
    }

    /**
     * 散列加密的方法
     *
     * @param s    明文
     * @param type 加密类型
     * @return 密文
     */
    private static String hashEncrypt(String s, String type) {
        try {
            return new BigInteger(1, MessageDigest.getInstance(type).digest(s.getBytes())).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ Base64 ------------------

    /**
     * 图片base64编码
     *
     * @param bs bs
     * @return base64
     */
    public static String img64Encode(byte[] bs) {
        return img64Encode(bs, "jpg");
    }

    /**
     * 图片base64编码
     *
     * @param bs   bs
     * @param type jpg, jepg, png
     * @return base64
     */
    public static String img64Encode(byte[] bs, String type) {
        return "data:image/" + type + ";base64," + new String(base64Encode(bs));
    }

    /**
     * 图片base64解码
     *
     * @param s base64
     * @return 图片
     */
    public static byte[] img64Decode(String s) {
        int i = s.indexOf("base64");
        return s.substring(i + 7, s.length()).getBytes();
    }

    /**
     * base64编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String base64Encode(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()));
    }

    /**
     * base64编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] base64Encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

    /**
     * base64解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String base64Decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }

    /**
     * base64解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] base64Decode(byte[] b) {
        return Base64.getDecoder().decode(b);
    }

    /**
     * base64url编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Encode(String s) {
        return new String(Base64.getUrlEncoder().encode(s.getBytes()));
    }

    /**
     * base64url编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Encode(byte[] b) {
        return Base64.getUrlEncoder().encode(b);
    }

    /**
     * base64url解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Decode(String s) {
        return new String(Base64.getUrlDecoder().decode(s));
    }

    /**
     * base64url解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Decode(byte[] b) {
        return Base64.getUrlDecoder().decode(b);
    }

    // ------------------ HEX ------------------

    /**
     * 比特安全编码
     *
     * @param str ignore
     * @return ignore
     */
    public static String encodeHex(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (byte b : bs) {
            sb.append(HEX_CHAR_ARRAY[(b & 0x0f0) >> 4]).append(HEX_CHAR_ARRAY[b & 0x0f]);
        }
        return sb.toString().trim();
    }

    /**
     * 比特安全解码
     *
     * @param hexStr ignore
     * @return ignore
     */
    public static String decodeHex(String hexStr) {
        char[] hex = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = HEX_CHARS.indexOf(hex[2 * i]) * 16;
            n += HEX_CHARS.indexOf(hex[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    // ------------------ AES ------------------

    /**
     * AES加密
     *
     * @param s   明文
     * @param key key
     * @return 密文
     */
    public static String aesEncrypt(String s, String key) {
        byte[] bs = aesEncrypt(s.getBytes(), null, key, false);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param bs  明文
     * @param key key
     * @return 密文
     */
    public static byte[] aesEncrypt(byte[] bs, String key) {
        return aesEncrypt(bs, null, key, false);
    }

    /**
     * AES加密
     *
     * @param s         明文
     * @param key       key
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 密文
     */
    public static String aesEncrypt(String s, String key, boolean generator) {
        byte[] bs = aesEncrypt(s.getBytes(), null, key, generator);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param bs        明文
     * @param key       key
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 密文
     */
    public static byte[] aesEncrypt(byte[] bs, String key, boolean generator) {
        return aesEncrypt(bs, null, key, generator);
    }

    /**
     * AES加密
     *
     * @param s   明文
     * @param key key
     * @return 密文
     */
    public static String aesEncrypt(String s, SecretKey key) {
        byte[] bs = aesEncrypt(s.getBytes(), key, null, false);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param bs  明文
     * @param key key
     * @return 密文
     */
    public static byte[] aesEncrypt(byte[] bs, SecretKey key) {
        return aesEncrypt(bs, key, null, false);
    }

    /**
     * AES加密
     *
     * @param bs        明文
     * @param key       key
     * @param s         StringKey
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 密文
     */
    private static byte[] aesEncrypt(byte[] bs, SecretKey key, String s, boolean generator) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            if (key == null) {
                if (generator) {
                    cipher.init(Cipher.ENCRYPT_MODE, generatorAESKey(s));
                } else {
                    cipher.init(Cipher.ENCRYPT_MODE, getAESKey(s));
                }
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            return base64Encode(cipher.doFinal(bs));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param s   密文
     * @param key key
     * @return 明文
     */
    public static String aesDecrypt(String s, String key) {
        byte[] bs = aesEncrypt(s.getBytes(), null, key, false);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param bs  密文
     * @param key key
     * @return 明文
     */
    public static byte[] aesDecrypt(byte[] bs, String key) {
        return aesDecrypt(bs, null, key, false);
    }

    /**
     * AES解密
     *
     * @param s         密文
     * @param key       key
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 明文
     */
    public static String aesDecrypt(String s, String key, boolean generator) {
        byte[] bs = aesDecrypt(s.getBytes(), null, key, generator);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param bs        密文
     * @param key       key
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 明文
     */
    public static byte[] aesDecrypt(byte[] bs, String key, boolean generator) {
        return aesDecrypt(bs, null, key, generator);
    }

    /**
     * AES解密
     *
     * @param s   密文
     * @param key key
     * @return 明文
     */
    public static String aesDecrypt(String s, SecretKey key) {
        byte[] bs = aesDecrypt(s.getBytes(), key, null, false);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param bs  密文
     * @param key key
     * @return 明文
     */
    public static byte[] aesDecrypt(byte[] bs, SecretKey key) {
        return aesDecrypt(bs, key, null, false);
    }

    /**
     * AES解密
     *
     * @param bs        密文
     * @param key       key
     * @param s         StringKey
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @return 明文
     */
    private static byte[] aesDecrypt(byte[] bs, SecretKey key, String s, boolean generator) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            if (key == null) {
                if (generator) {
                    cipher.init(Cipher.DECRYPT_MODE, generatorAESKey(s));
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, getAESKey(s));
                }
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            return cipher.doFinal(base64Decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ DES ------------------

    /**
     * DES加密
     *
     * @param s   明文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 密文
     */
    public static String desEncrypt(String s, String key, String iv) {
        byte[] bytes = desEncrypt(s.getBytes(), null, key, false, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES加密
     *
     * @param bs  明文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 密文
     */
    public static byte[] desEncrypt(byte[] bs, String key, byte[] iv) {
        return desEncrypt(bs, null, key, false, iv);
    }

    /**
     * DES加密
     *
     * @param s         明文
     * @param key       key 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 密文
     */
    public static String desEncrypt(String s, String key, boolean generator, String iv) {
        byte[] bytes = desEncrypt(s.getBytes(), null, key, generator, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES加密
     *
     * @param bs        明文
     * @param key       key 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 密文
     */
    public static byte[] desEncrypt(byte[] bs, String key, boolean generator, byte[] iv) {
        return desEncrypt(bs, null, key, generator, iv);
    }

    /**
     * DES加密
     *
     * @param s   明文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 密文
     */
    public static String desEncrypt(String s, SecretKey key, String iv) {
        byte[] bytes = desEncrypt(s.getBytes(), key, null, false, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES加密
     *
     * @param bs  明文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 密文
     */
    public static byte[] desEncrypt(byte[] bs, SecretKey key, byte[] iv) {
        return desEncrypt(bs, key, null, false, iv);
    }

    /**
     * DES加密
     *
     * @param bs        明文
     * @param key       key
     * @param s         StringKey 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 密文
     */
    private static byte[] desEncrypt(byte[] bs, SecretKey key, String s, boolean generator, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            if (key == null) {
                if (generator) {
                    key = generatorDESKey(s);
                } else {
                    key = getDESKey(s);
                }
            }
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return base64Encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * DES解密
     *
     * @param s   密文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 明文
     */
    public static String desDecrypt(String s, String key, String iv) {
        byte[] bytes = desDecrypt(s.getBytes(), null, key, false, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param bs  密文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 明文
     */
    public static byte[] desDecrypt(byte[] bs, String key, byte[] iv) {
        return desDecrypt(bs, null, key, false, iv);
    }

    /**
     * DES解密
     *
     * @param s         密文
     * @param key       key 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 明文
     */
    public static String desDecrypt(String s, String key, boolean generator, String iv) {
        byte[] bytes = desDecrypt(s.getBytes(), null, key, generator, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param bs        密文
     * @param key       key 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 明文
     */
    public static byte[] desDecrypt(byte[] bs, String key, boolean generator, byte[] iv) {
        return desDecrypt(bs, null, key, generator, iv);
    }

    /**
     * DES解密
     *
     * @param s   密文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 明文
     */
    public static String desDecrypt(String s, SecretKey key, String iv) {
        byte[] bytes = desDecrypt(s.getBytes(), key, null, false, iv.getBytes());
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param bs  密文
     * @param key key
     * @param iv  向量 必须为8位
     * @return 明文
     */
    public static byte[] desDecrypt(byte[] bs, SecretKey key, byte[] iv) {
        return desDecrypt(bs, key, null, false, iv);
    }

    /**
     * DES解密
     *
     * @param bs        密文
     * @param key       key
     * @param s         StringKey 如果generator为true 必须为24位
     * @param generator true通过StringKey生成SecretKey, false 通过StringKey转化SecretKey
     * @param iv        向量 必须为8位
     * @return 明文
     */
    private static byte[] desDecrypt(byte[] bs, SecretKey key, String s, boolean generator, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            if (key == null) {
                if (generator) {
                    key = generatorDESKey(s);
                } else {
                    key = getDESKey(s);
                }
            }
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(base64Decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ RSA ------------------

    /**
     * RSA公钥加密
     *
     * @param s         明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String rsaEncrypt(String s, String publicKey) {
        byte[] bytes = rsaEncrypt(s.getBytes(), getPublicRSAKey(publicKey));
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
    public static String rsaEncrypt(String s, PublicKey publicKey) {
        byte[] bytes = rsaEncrypt(s.getBytes(), publicKey);
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
    public static byte[] rsaEncrypt(byte[] bs, String publicKey) {
        return rsaEncrypt(bs, getPublicRSAKey(publicKey));
    }

    /**
     * RSA公钥加密
     *
     * @param bs        bytes
     * @param publicKey 公钥
     * @return 密文
     */
    public static byte[] rsaEncrypt(byte[] bs, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return base64Encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA私钥解密
     *
     * @param s          密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String rsaDecrypt(String s, String privateKey) {
        byte[] bytes = rsaDecrypt(s.getBytes(), getPrivateRSAKey(privateKey));
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
    public static String rsaDecrypt(String s, PrivateKey privateKey) {
        byte[] bytes = rsaDecrypt(s.getBytes(), privateKey);
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
    public static byte[] rsaDecrypt(byte[] bs, String privateKey) {
        return rsaDecrypt(bs, getPrivateRSAKey(privateKey));
    }

    /**
     * RSA私钥解密
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 明文
     */
    public static byte[] rsaDecrypt(byte[] bs, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(base64Decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA私钥签名
     *
     * @param s          明文
     * @param privateKey 私钥
     * @return 签名
     */
    public static String rsaSign(String s, String privateKey) {
        byte[] bytes = rsaSign(s.getBytes(), getPrivateRSAKey(privateKey));
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
    public static String rsaSign(String s, PrivateKey privateKey) {
        byte[] bytes = rsaSign(s.getBytes(), privateKey);
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
    public static byte[] rsaSign(byte[] bs, String privateKey) {
        return rsaSign(bs, getPrivateRSAKey(privateKey));
    }

    /**
     * RSA私钥签名
     *
     * @param bs         bytes
     * @param privateKey 私钥
     * @return 签名
     */
    public static byte[] rsaSign(byte[] bs, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(bs);
            return base64Encode(signature.sign());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean rsaVerify(String s, String publicKey, String sign) {
        return rsaVerify(s.getBytes(), getPublicRSAKey(publicKey), sign.getBytes());
    }

    /**
     * RSA公钥验签
     *
     * @param s         明文
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean rsaVerify(String s, PublicKey publicKey, String sign) {
        return rsaVerify(s.getBytes(), publicKey, sign.getBytes());
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean rsaVerify(byte[] bs, String publicKey, byte[] sign) {
        return rsaVerify(bs, getPublicRSAKey(publicKey), sign);
    }

    /**
     * RSA公钥验签
     *
     * @param bs        明文bytes
     * @param publicKey 公钥
     * @param sign      签名
     * @return true验证成功
     */
    public static boolean rsaVerify(byte[] bs, PublicKey publicKey, byte[] sign) {
        try {
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(bs);
            return signature.verify(base64Decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    // ------------------ Key ------------------

    /**
     * String -> AESKey
     *
     * @param key String
     * @return AESKey
     */
    public static SecretKey generatorAESKey(String key) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance(AES_ALGORITHM);
            random.setSeed(key.getBytes());
            keyGenerator.init(128, random);
            return new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * StringKey -> AESKey
     *
     * @param key StringKey
     * @return AESKey
     */
    public static SecretKey getAESKey(String key) {
        return new SecretKeySpec(base64Decode(key.getBytes()), "AES");
    }

    /**
     * AESKey -> StringKey
     *
     * @param key AESKey
     * @return StringKey
     */
    public static String getAESKey(SecretKey key) {
        return new String(base64Encode(key.getEncoded()));
    }

    /**
     * String -> DESKey
     *
     * @param key 必须为24位
     * @return DESKey
     */
    public static SecretKey generatorDESKey(String key) {
        try {
            return SecretKeyFactory.getInstance(DES_SECRET).generateSecret(new DESedeKeySpec(key.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * StringKey -> AESKey
     *
     * @param key StringKey
     * @return AESKey
     */
    public static SecretKey getDESKey(String key) {
        return new SecretKeySpec(base64Decode(key.getBytes()), "DES");
    }

    /**
     * AESKey -> StringKey
     *
     * @param key AESKey
     * @return StringKey
     */
    public static String getDESKey(SecretKey key) {
        return new String(base64Encode(key.getEncoded()));
    }

    /**
     * StringKey -> PublicKey
     *
     * @param key StringKey
     * @return PublicKey
     */
    public static PublicKey getPublicRSAKey(String key) {
        try {
            byte[] keyBytes = base64Decode(key.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * PublicKey -> StringKey
     *
     * @param key PublicKey
     * @return StringKey
     */
    public static String getPublicRSAKey(PublicKey key) {
        return new String(base64Encode(key.getEncoded()));
    }

    /**
     * StringKey -> PrivateKey
     *
     * @param key StringKey
     * @return PrivateKey
     */
    public static PrivateKey getPrivateRSAKey(String key) {
        try {
            byte[] keyBytes = base64Decode(key.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * PrivateKey -> StringKey
     *
     * @param key PrivateKey
     * @return StringKey
     */
    public static String getPrivateRSAKey(PrivateKey key) {
        return new String(base64Encode(key.getEncoded()));
    }

    /**
     * 随机生成 公私钥对
     *
     * @return PublicKey PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> generatorRSAKey() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 96 ~ 1024
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return Args.of(keyPair.getPublic(), keyPair.getPrivate());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取秘钥文件 如PEM公钥, 私钥
     *
     * @param file 秘钥文件
     * @return Key
     */
    public static String getKey(File file) {
        try {
            return getKey(new InputStreamReader(Files1.openInputStream(file)), true);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取秘钥文件 如PEM公钥, 私钥
     *
     * @param in in
     * @return Key
     */
    public static String getKey(InputStream in) {
        return getKey(new InputStreamReader(in), false);
    }

    /**
     * 获取秘钥文件 如PEM公钥, 私钥
     *
     * @param reader reader
     * @return Key
     */
    public static String getKey(Reader reader) {
        return getKey(reader, false);
    }

    /**
     * 获取秘钥文件 如PEM公钥, 私钥
     *
     * @param reader reader
     * @param close  是否关闭流
     * @return Key
     */
    private static String getKey(Reader reader, boolean close) {
        StringBuilder key = new StringBuilder();
        int c = 0;
        BufferedReader r = new BufferedReader(reader);
        try {
            String s = r.readLine();
            while (s != null) {
                if (s.contains("--")) {
                    if (++c == 2) {
                        break;
                    }
                } else {
                    key.append(s);
                }
                s = r.readLine();
            }
            return key.toString().replaceAll("\n", "").replaceAll("\r", "");
        } catch (IOException e) {
            return null;
        } finally {
            if (close) {
                Streams.closeQuietly(reader);
            }
        }
    }

    /**
     * 读取CER公钥文件
     *
     * @param file 文件
     * @return StringKey
     */
    public static String getCERPublicKey(File file) {
        try {
            return getCERPublicKey(Files1.openInputStream(file), true);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取CER公钥文件
     *
     * @param in 流
     * @return StringKey
     */
    public static String getCERPublicKey(InputStream in) {
        return getCERPublicKey(in, false);
    }

    /**
     * 读取CER公钥文件
     *
     * @param in    流
     * @param close 是否关闭流
     * @return StringKey
     */
    private static String getCERPublicKey(InputStream in, boolean close) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
            PublicKey publicKey = cert.getPublicKey();
            return new String(base64Encode(publicKey.getEncoded()));
        } catch (Exception e) {
            return null;
        } finally {
            if (close) {
                Streams.closeQuietly(in);
            }
        }
    }

    /**
     * 读取PFX私钥文件
     *
     * @param file     私钥文件
     * @param password 密码
     * @return PublicKey, PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> getPFXPrivateKey(File file, String password) {
        try {
            return getPFXPrivateKey(Files1.openInputStream(file), password, true);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取PFX私钥文件
     *
     * @param in       in
     * @param password 密码
     * @return PublicKey, PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> getPFXPrivateKey(InputStream in, String password) {
        return getPFXPrivateKey(in, password, false);
    }

    /**
     * 读取PFX私钥文件
     *
     * @param in       in
     * @param password 密码
     * @param close    是否关闭流
     * @return PublicKey, PrivateKey
     */
    private static Args.Two<PublicKey, PrivateKey> getPFXPrivateKey(InputStream in, String password, boolean close) {
        try {
            char[] ps = password == null ? null : password.toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(in, ps);
            Enumeration aliases = ks.aliases();
            String keyAlias = null;
            if (aliases.hasMoreElements()) {
                keyAlias = (String) aliases.nextElement();
            }
            Certificate cert = ks.getCertificate(keyAlias);
            return Args.of(cert.getPublicKey(), (PrivateKey) ks.getKey(keyAlias, ps));
        } catch (Exception e) {
            return null;
        } finally {
            if (close) {
                Streams.closeQuietly(in);
            }
        }
    }

}
