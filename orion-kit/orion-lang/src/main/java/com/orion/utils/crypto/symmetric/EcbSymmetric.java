package com.orion.utils.crypto.symmetric;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.PaddingMode;
import com.orion.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static com.orion.utils.codec.Base64s.decode;
import static com.orion.utils.codec.Base64s.encode;

/**
 * ECB 模式非对称加密 AES DES 3DES
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/3 14:07
 */
public class EcbSymmetric extends BaseSymmetric {

    public EcbSymmetric(CipherAlgorithm algorithm) {
        super(algorithm, WorkingMode.ECB, PaddingMode.PKCS5_PADDING);
    }

    public EcbSymmetric(CipherAlgorithm algorithm, PaddingMode paddingMode) {
        super(algorithm, WorkingMode.ECB, paddingMode);
    }

    // ------------------ ENC ------------------

    public String encrypt(String s, String key) {
        byte[] bs = encrypt(Strings.bytes(s), null, Strings.bytes(key));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public byte[] encrypt(byte[] bs, byte[] key) {
        return encrypt(bs, null, key);
    }

    public String encrypt(String s, SecretKey key) {
        byte[] bs = encrypt(Strings.bytes(s), key, null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public byte[] encrypt(byte[] bs, SecretKey key) {
        return encrypt(bs, key, null);
    }

    /**
     * 加密
     *
     * @param bs  明文
     * @param key key
     * @param k   StringKey
     * @return 密文
     */
    private byte[] encrypt(byte[] bs, SecretKey key, byte[] k) {
        try {
            if (key == null) {
                key = generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            if (paddingMode.equals(PaddingMode.ZERO_PADDING)) {
                return encode(cipher.doFinal(zeroPadding(bs, cipher.getBlockSize())));
            } else {
                return encode(cipher.doFinal(bs));
            }
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    // ------------------ DEC ------------------

    public String decrypt(String s, String key) {
        byte[] bs = decrypt(Strings.bytes(s), null, Strings.bytes(key));
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public byte[] decrypt(byte[] bs, byte[] key) {
        return decrypt(bs, null, key);
    }

    public String decrypt(String s, SecretKey key) {
        byte[] bs = decrypt(Strings.bytes(s), key, null);
        if (bs != null) {
            return new String(bs);
        }
        return null;
    }

    public byte[] decrypt(byte[] bs, SecretKey key) {
        return decrypt(bs, key, null);
    }

    /**
     * 解密
     *
     * @param bs  密文
     * @param key key
     * @param k   StringKey
     * @return 明文
     */
    private byte[] decrypt(byte[] bs, SecretKey key, byte[] k) {
        try {
            if (key == null) {
                key = generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(decode(bs));
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

}

