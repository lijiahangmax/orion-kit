package com.orion.utils.crypto;

import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.SecretKeySpecMode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

import static com.orion.utils.crypto.Base64s.base64Decode;
import static com.orion.utils.crypto.Base64s.base64Encode;

/**
 * AES 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/27 18:54
 */
public class AES {

    private static final String AES = "AES";

    /**
     * 默认工作模式
     */
    private static final String DEFAULT_WORKING_MODE = "ECB";

    /**
     * 使用向量 工作模式
     */
    private static String ivWorkingMode = "CBC";

    /**
     * 默认填充方式
     */
    private static String paddingMode = "PKCS5Padding";

    private AES() {
    }

    // ------------------ ENC ------------------

    public static String encrypt(String s, String key) {
        byte[] bs = encrypt(getBytes(s), null, getBytes(key), null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static String encrypt(String s, String key, String iv) {
        byte[] bs = encrypt(getBytes(s), null, getBytes(key), getBytes(iv));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static byte[] encrypt(byte[] bs, byte[] key) {
        return encrypt(bs, null, key, null);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, byte[] iv) {
        return encrypt(bs, null, key, iv);
    }

    public static String encrypt(String s, SecretKey key) {
        byte[] bs = encrypt(getBytes(s), key, null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static String encrypt(String s, SecretKey key, String iv) {
        byte[] bs = encrypt(getBytes(s), key, null, getBytes(iv));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static byte[] encrypt(byte[] bs, SecretKey key) {
        return encrypt(bs, key, null, null);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, byte[] iv) {
        return encrypt(bs, key, null, iv);
    }

    /**
     * AES加密
     *
     * @param bs  明文
     * @param key key
     * @param k   StringKey
     * @param iv  向量 16位置 不足用0填充
     * @return 密文
     */
    private static byte[] encrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv) {
        try {
            if (key == null) {
                key = generatorKey(k);
            }
            Cipher cipher;
            if (iv == null) {
                cipher = CipherAlgorithm.AES.getCipher(DEFAULT_WORKING_MODE, paddingMode);
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher = CipherAlgorithm.AES.getCipher(ivWorkingMode, paddingMode);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Arrays1.resize(iv, 16)));
            }
            return base64Encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ DEC ------------------

    public static String decrypt(String s, String key) {
        byte[] bs = decrypt(getBytes(s), null, getBytes(key), null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static String decrypt(String s, String key, String iv) {
        byte[] bs = decrypt(getBytes(s), null, getBytes(key), getBytes(iv));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static byte[] decrypt(byte[] bs, byte[] key) {
        return decrypt(bs, null, key, null);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, byte[] iv) {
        return decrypt(bs, null, key, iv);
    }

    public static String decrypt(String s, SecretKey key) {
        byte[] bs = decrypt(getBytes(s), key, null, null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static String decrypt(String s, SecretKey key, String iv) {
        byte[] bs = decrypt(getBytes(s), key, null, getBytes(iv));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public static byte[] decrypt(byte[] bs, SecretKey key) {
        return decrypt(bs, key, null, null);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, byte[] iv) {
        return decrypt(bs, key, null, iv);
    }

    /**
     * AES解密
     *
     * @param bs  密文
     * @param key key
     * @param k   StringKey
     * @param iv  向量 16位 不足用0填充
     * @return 明文
     */
    private static byte[] decrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv) {
        try {
            if (key == null) {
                key = generatorKey(k);
            }
            Cipher cipher;
            if (iv == null) {
                cipher = CipherAlgorithm.AES.getCipher(DEFAULT_WORKING_MODE, paddingMode);
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher = CipherAlgorithm.AES.getCipher(ivWorkingMode, paddingMode);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Arrays1.resize(iv, 16)));
            }
            return cipher.doFinal(base64Decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ KEY ------------------

    /**
     * String -> SecretKey
     *
     * @param key String
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom random = new SecureRandom(getBytes(key));
            keyGenerator.init(128, random);
            return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * String -> SecretKey
     *
     * @param size size  128 192 256
     * @param key  String
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key, int size) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom random = new SecureRandom(getBytes(key));
            keyGenerator.init(size, random);
            return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * String -> SecretKey
     *
     * @param key String
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] key) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom random = new SecureRandom(key);
            keyGenerator.init(128, random);
            return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * String -> SecretKey
     *
     * @param size size  128 192 256
     * @param key  String
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] key, int size) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom random = new SecureRandom(key);
            keyGenerator.init(size, random);
            return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key StringKey
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String key) {
        return SecretKeySpecMode.AES.getSecretKeySpec(base64Decode(getBytes(key)));
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key StringKey
     * @return SecretKey
     */
    public static SecretKey getSecretKey(byte[] key) {
        return SecretKeySpecMode.AES.getSecretKeySpec(base64Decode(key));
    }

    // ------------------ SETTING ------------------

    private static byte[] getBytes(String s) {
        return s == null ? null : s.getBytes();
    }

    public static void setIvWorkingMode(String mode) {
        ivWorkingMode = mode;
    }

    public static void setPaddingMode(String mode) {
        paddingMode = mode;
    }

}
