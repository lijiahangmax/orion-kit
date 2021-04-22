package com.orion.utils.crypto.symmetric;

import com.orion.utils.Strings;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.PaddingMode;
import com.orion.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static com.orion.utils.codec.Base64s.decode;
import static com.orion.utils.codec.Base64s.encode;

/**
 * GCM 非对称加密模式 AES
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/3 14:45
 */
public class GcmSymmetric extends BaseSymmetric {

    public GcmSymmetric() {
        super(CipherAlgorithm.AES, WorkingMode.GCM, PaddingMode.NO_PADDING);
    }

    public GcmSymmetric(PaddingMode paddingMode) {
        super(CipherAlgorithm.AES, WorkingMode.GCM, paddingMode);
    }

    // -------------------- ENC --------------------

    public String encrypt(String bs, String key, String iv, String aad) {
        byte[] encrypt = this.encrypt(Strings.bytes(bs), null, Strings.bytes(key), Strings.bytes(iv), Strings.bytes(aad));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public String encrypt(String bs, SecretKey key, String iv, String aad) {
        byte[] encrypt = this.encrypt(Strings.bytes(bs), key, null, Strings.bytes(iv), Strings.bytes(aad));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public byte[] encrypt(byte[] bs, byte[] k, byte[] iv, byte[] aad) {
        return this.encrypt(bs, null, k, iv, aad);
    }

    public byte[] encrypt(byte[] bs, SecretKey key, byte[] iv, byte[] aad) {
        return this.encrypt(bs, key, null, iv, aad);
    }

    /**
     * 加密
     *
     * @param bs  明文
     * @param key key
     * @param k   StringKey
     * @param iv  向量
     * @param aad aad
     * @return 密文
     */
    private byte[] encrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv, byte[] aad) {
        try {
            if (key == null) {
                key = this.generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            GCMParameterSpec gcmSpec = super.getGcmSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
            cipher.updateAAD(aad);
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

    public String decrypt(String bs, String key, String iv, String aad) {
        byte[] encrypt = this.decrypt(Strings.bytes(bs), null, Strings.bytes(key), Strings.bytes(iv), Strings.bytes(aad));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public String decrypt(String bs, SecretKey key, String iv, String aad) {
        byte[] encrypt = this.decrypt(Strings.bytes(bs), key, null, Strings.bytes(iv), Strings.bytes(aad));
        if (encrypt != null) {
            return new String(encrypt);
        }
        return null;
    }

    public byte[] decrypt(byte[] bs, byte[] k, byte[] iv, byte[] aad) {
        return this.decrypt(bs, null, k, iv, aad);
    }

    public byte[] decrypt(byte[] bs, SecretKey key, byte[] iv, byte[] aad) {
        return this.decrypt(bs, key, null, iv, aad);
    }

    /**
     * 解密
     *
     * @param bs  密文
     * @param key key
     * @param k   StringKey
     * @param iv  向量
     * @param aad aad
     * @return 明文
     */
    private byte[] decrypt(byte[] bs, SecretKey key, byte[] k, byte[] iv, byte[] aad) {
        try {
            if (key == null) {
                key = this.generatorKey(k);
            }
            Cipher cipher = super.getCipher();
            GCMParameterSpec gcmSpec = super.getGcmSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            cipher.updateAAD(aad);
            return this.clearDecryptZero(cipher.doFinal(decode(bs)));
        } catch (Exception e) {
            return null;
        }
    }

}
