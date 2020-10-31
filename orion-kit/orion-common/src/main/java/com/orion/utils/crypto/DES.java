package com.orion.utils.crypto;

import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.SecretKeySpecMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.orion.utils.codec.Base64s.decode;
import static com.orion.utils.codec.Base64s.encode;

/**
 * DES 3DES 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/27 18:55
 */
public class DES {

    private static final String DES = "DES";

    private static final String DES3 = "DESEDE";

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

    private DES() {
    }

    // ------------------ ENC ------------------

    public static String encrypt(String s, String key) {
        byte[] bytes = encrypt(getBytes(s), null, getBytes(key), null, CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, String key, String iv) {
        byte[] bytes = encrypt(getBytes(s), null, getBytes(key), getBytes(iv), CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, String key, CipherAlgorithm mode) {
        byte[] bytes = encrypt(getBytes(s), null, getBytes(key), null, mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, String key, String iv, CipherAlgorithm mode) {
        byte[] bytes = encrypt(getBytes(s), null, getBytes(key), getBytes(iv), mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] encrypt(byte[] bs, byte[] key) {
        return encrypt(bs, null, key, null, CipherAlgorithm.DES);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, byte[] iv) {
        return encrypt(bs, null, key, iv, CipherAlgorithm.DES);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, CipherAlgorithm mode) {
        return encrypt(bs, null, key, null, mode);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, byte[] iv, CipherAlgorithm mode) {
        return encrypt(bs, null, key, iv, mode);
    }

    public static String encrypt(String s, SecretKey key) {
        byte[] bytes = encrypt(getBytes(s), key, null, null, CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, SecretKey key, String iv) {
        byte[] bytes = encrypt(getBytes(s), key, null, getBytes(iv), CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, SecretKey key, CipherAlgorithm mode) {
        byte[] bytes = encrypt(getBytes(s), key, null, null, mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String encrypt(String s, SecretKey key, String iv, CipherAlgorithm mode) {
        byte[] bytes = encrypt(getBytes(s), key, null, getBytes(iv), mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] encrypt(byte[] bs, SecretKey key) {
        return encrypt(bs, key, null, null, CipherAlgorithm.DES);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, byte[] iv) {
        return encrypt(bs, key, null, iv, CipherAlgorithm.DES);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, CipherAlgorithm mode) {
        return encrypt(bs, key, null, null, mode);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, byte[] iv, CipherAlgorithm mode) {
        return encrypt(bs, key, null, iv, mode);
    }

    /**
     * DES加密
     *
     * @param bs   明文
     * @param key  key
     * @param k    StringKey  必须为24位
     * @param iv   向量 必须为8位 不足用0填充
     * @param mode CipherAlgorithm 加密类型 DES / DES3
     * @return 密文
     */
    private static byte[] encrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv, CipherAlgorithm mode) {
        try {
            if (key == null) {
                key = generatorKey(k, mode);
            }
            Cipher cipher;
            if (iv == null) {
                cipher = mode.getCipher(DEFAULT_WORKING_MODE, paddingMode);
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher = mode.getCipher(ivWorkingMode, paddingMode);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Arrays1.resize(iv, 8)));
            }
            return encode(cipher.doFinal(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ DEC ------------------

    public static String decrypt(String s, String key) {
        byte[] bytes = decrypt(getBytes(s), null, getBytes(key), null, CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, String key, String iv) {
        byte[] bytes = decrypt(getBytes(s), null, getBytes(key), getBytes(iv), CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, String key, CipherAlgorithm mode) {
        byte[] bytes = decrypt(getBytes(s), null, getBytes(key), null, mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, String key, String iv, CipherAlgorithm mode) {
        byte[] bytes = decrypt(getBytes(s), null, getBytes(key), getBytes(iv), mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] decrypt(byte[] bs, byte[] key) {
        return decrypt(bs, null, key, null, CipherAlgorithm.DES);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, byte[] iv) {
        return decrypt(bs, null, key, iv, CipherAlgorithm.DES);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, CipherAlgorithm mode) {
        return decrypt(bs, null, key, null, mode);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, byte[] iv, CipherAlgorithm mode) {
        return decrypt(bs, null, key, iv, mode);
    }

    public static String decrypt(String s, SecretKey key) {
        byte[] bytes = decrypt(getBytes(s), key, null, null, CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, SecretKey key, String iv) {
        byte[] bytes = decrypt(getBytes(s), key, null, getBytes(iv), CipherAlgorithm.DES);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, SecretKey key, CipherAlgorithm mode) {
        byte[] bytes = decrypt(getBytes(s), key, null, null, mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static String decrypt(String s, SecretKey key, String iv, CipherAlgorithm mode) {
        byte[] bytes = decrypt(getBytes(s), key, null, getBytes(iv), mode);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] decrypt(byte[] bs, SecretKey key) {
        return decrypt(bs, key, null, null, CipherAlgorithm.DES);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, byte[] iv) {
        return decrypt(bs, key, null, iv, CipherAlgorithm.DES);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, CipherAlgorithm mode) {
        return decrypt(bs, key, null, null, mode);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, byte[] iv, CipherAlgorithm mode) {
        return decrypt(bs, key, null, iv, mode);
    }

    /**
     * DES解密
     *
     * @param bs   密文
     * @param key  key
     * @param k    StringKey  必须为24位 不足用0填充
     * @param iv   向量 必须为8位 不足用0填充
     * @param mode 加密类型 DES / DES3
     * @return 明文
     */
    private static byte[] decrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv, CipherAlgorithm mode) {
        try {
            if (key == null) {
                key = generatorKey(k, mode);
            }
            Cipher cipher;
            if (iv == null) {
                cipher = mode.getCipher(DEFAULT_WORKING_MODE, paddingMode);
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher = mode.getCipher(ivWorkingMode, paddingMode);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Arrays1.resize(iv, 8)));
            }
            return cipher.doFinal(decode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    // ------------------ KEY ------------------

    /**
     * String -> SecretKey
     *
     * @param bs 24位 不足24位用0填充
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] bs) {
        try {
            bs = Arrays1.resize(bs, 24);
            return SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(bs));
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * String -> SecretKey
     *
     * @param bs   24位 不足24位用0填充
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] bs, CipherAlgorithm mode) {
        try {
            bs = Arrays1.resize(bs, 24);
            if (DES.equalsIgnoreCase(mode.getMode())) {
                return SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(bs));
            } else {
                return SecretKeyFactory.getInstance(DES3).generateSecret(new DESedeKeySpec(bs));
            }
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * String -> SecretKey
     *
     * @param key 24位 不足24位用0填充
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key) {
        return generatorKey(getBytes(key));
    }

    /**
     * String -> SecretKey
     *
     * @param key  24位 不足24位用0填充
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key, CipherAlgorithm mode) {
        return generatorKey(getBytes(key), mode);
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key StringKey
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String key) {
        return SecretKeySpecMode.DES.getSecretKeySpec(decode(getBytes(key)));
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key  StringKey
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String key, CipherAlgorithm mode) {
        return new SecretKeySpec(decode(getBytes(key)), mode.getMode());
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key StringKey
     * @return SecretKey
     */
    public static SecretKey getSecretKey(byte[] key) {
        return SecretKeySpecMode.DES.getSecretKeySpec(decode(key));
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key  StringKey
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey getSecretKey(byte[] key, CipherAlgorithm mode) {
        return new SecretKeySpec(decode(key), mode.getMode());
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
