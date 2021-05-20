package com.orion.utils.crypto.symmetric;

import com.orion.utils.Strings;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.PaddingMode;
import com.orion.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static com.orion.utils.codec.Base64s.decode;
import static com.orion.utils.codec.Base64s.encode;

/**
 * CBC CFB OFB FTP GCM 模式非对称加密 AES DES 3DES
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/3 16:22
 */
public class IvSymmetric extends BaseSymmetric {

    public IvSymmetric(CipherAlgorithm algorithm, WorkingMode workingMode) {
        super(algorithm, workingMode, PaddingMode.PKCS5_PADDING);
    }

    public IvSymmetric(CipherAlgorithm algorithm, WorkingMode workingMode, PaddingMode paddingMode) {
        super(algorithm, workingMode, paddingMode);
    }

    // -------------------- ENC --------------------

    public String encrypt(String bs, String key, String iv) {
        byte[] encrypt = this.encrypt(Strings.bytes(bs), null, Strings.bytes(key), Strings.bytes(iv));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public String encrypt(String bs, SecretKey key, String iv) {
        byte[] encrypt = this.encrypt(Strings.bytes(bs), key, null, Strings.bytes(iv));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public byte[] encrypt(byte[] bs, byte[] k, byte[] iv) {
        return this.encrypt(bs, null, k, iv);
    }

    public byte[] encrypt(byte[] bs, SecretKey key, byte[] iv) {
        return this.encrypt(bs, key, null, iv);
    }

    /**
     * 加密
     *
     * @param bs  明文
     * @param key key
     * @param k   StringKey
     * @param iv  向量
     * @return 密文
     */
    private byte[] encrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv) {
        try {
            if (key == null) {
                key = this.generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            IvParameterSpec ivSpec = super.getIvSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            if (paddingMode.equals(PaddingMode.ZERO_PADDING)) {
                return encode(cipher.doFinal(zeroPadding(bs, cipher.getBlockSize())));
            } else {
                return encode(cipher.doFinal(bs));
            }
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------- DEC --------------------

    public String decrypt(String bs, String key, String iv) {
        byte[] encrypt = this.decrypt(Strings.bytes(bs), null, Strings.bytes(key), Strings.bytes(iv));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public String decrypt(String bs, SecretKey key, String iv) {
        byte[] encrypt = this.decrypt(Strings.bytes(bs), key, null, Strings.bytes(iv));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public byte[] decrypt(byte[] bs, byte[] k, byte[] iv) {
        return this.decrypt(bs, null, k, iv);
    }

    public byte[] decrypt(byte[] bs, SecretKey key, byte[] iv) {
        return this.decrypt(bs, key, null, iv);
    }

    /**
     * 解密
     *
     * @param bs  密文
     * @param key key
     * @param k   StringKey
     * @param iv  向量
     * @return 明文
     */
    private byte[] decrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv) {
        try {
            if (key == null) {
                key = this.generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            IvParameterSpec ivSpec = super.getIvSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            return this.clearDecryptZero(cipher.doFinal(decode(bs)));
        } catch (Exception e) {
            return null;
        }
    }

}
