package com.orion.utils.crypto.symmetric;

import com.orion.utils.Arrays1;
import com.orion.utils.crypto.Keys;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.PaddingMode;
import com.orion.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 非对称加密父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/2 18:27
 */
public abstract class BaseSymmetric {

    protected CipherAlgorithm algorithm;

    protected WorkingMode workingMode;

    protected PaddingMode paddingMode;

    /**
     * 生成gcm规范参数 长度
     * 128, 120, 112, 104, 96
     */
    protected int gcmSpecLen = 128;

    /**
     * AES key 长度
     * 128 192 256
     */
    protected int aesKeyLen = 128;

    /**
     * DES Key 长度
     * 8的倍数
     */
    protected int desKeyLen = 8;

    /**
     * 3DES Key 长度
     * >=24 8的倍数
     */
    protected int des3KeyLen = 24;

    /**
     * AES IV 长度
     * >=16
     */
    protected int aesIvLen = 16;

    /**
     * DES IV 长度
     * >=8
     */
    protected int desIvLen = 8;

    /**
     * 3DES IV 长度
     * >=8
     */
    protected int des3IvLen = 8;

    protected BaseSymmetric(CipherAlgorithm algorithm, WorkingMode workingMode, PaddingMode paddingMode) {
        this.algorithm = algorithm;
        this.workingMode = workingMode;
        this.paddingMode = paddingMode;
    }

    protected Cipher getCipher() {
        return algorithm.getCipher(workingMode, paddingMode);
    }

    protected byte[] clearDecryptZero(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int f = bytes.length;
        for (int i = 0; i < f; i++) {
            if (bytes[i] == 0) {
                f = i;
                break;
            }
        }
        byte[] res = new byte[f];
        System.arraycopy(bytes, 0, res, 0, f);
        return res;
    }

    /**
     * 0填充数据
     *
     * @param bs        数据
     * @param blockSize 块大小
     * @return 0填充块数据
     */
    protected byte[] zeroPadding(byte[] bs, int blockSize) {
        if (bs.length % blockSize == 0) {
            return bs;
        }
        int newSize = ((bs.length / blockSize) + 1) * blockSize;
        byte[] bytes = new byte[newSize];
        System.arraycopy(bs, 0, bytes, 0, bs.length);
        return bytes;
    }

    /**
     * 获取向量
     *
     * @param bs 向量
     * @return 填充后的向量
     */
    protected IvParameterSpec getIvSpec(byte[] bs) {
        switch (algorithm) {
            case AES:
                return new IvParameterSpec(Arrays1.resize(bs, aesIvLen));
            case DES:
                return new IvParameterSpec(Arrays1.resize(bs, desIvLen));
            case DES3:
                return new IvParameterSpec(Arrays1.resize(bs, des3IvLen));
            default:
                return null;
        }
    }

    /**
     * 生成key
     *
     * @param bs key
     * @return SecretKey
     */
    protected SecretKey generatorKey(byte[] bs) {
        switch (algorithm) {
            case AES:
                return Keys.generatorKey(bs, aesKeyLen, algorithm);
            case DES:
                return Keys.generatorKey(bs, desKeyLen, algorithm);
            case DES3:
                return Keys.generatorKey(bs, des3KeyLen, algorithm);
            default:
                return null;
        }
    }

    /**
     * 生成gcm规范参数
     *
     * @param iv iv
     * @return GCMParameterSpec
     */
    protected GCMParameterSpec getGcmSpec(byte[] iv) {
        return new GCMParameterSpec(this.gcmSpecLen, iv);
    }

    public BaseSymmetric setGcmSpecLen(int gcmSpecLen) {
        this.gcmSpecLen = gcmSpecLen;
        return this;
    }

    public BaseSymmetric setAesKeyLen(int aesKeyLen) {
        this.aesKeyLen = aesKeyLen;
        return this;
    }

    public BaseSymmetric setDesKeyLen(int desKeyLen) {
        this.desKeyLen = desKeyLen;
        return this;
    }

    public BaseSymmetric setDes3KeyLen(int des3KeyLen) {
        this.des3KeyLen = des3KeyLen;
        return this;
    }

    public BaseSymmetric setAesIvLen(int aesIvLen) {
        this.aesIvLen = aesIvLen;
        return this;
    }

    public BaseSymmetric setDesIvLen(int desIvLen) {
        this.desIvLen = desIvLen;
        return this;
    }

    public BaseSymmetric setDes3IvLen(int des3IvLen) {
        this.des3IvLen = des3IvLen;
        return this;
    }

}
