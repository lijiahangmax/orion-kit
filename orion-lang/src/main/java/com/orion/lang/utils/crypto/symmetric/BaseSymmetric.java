package com.orion.lang.utils.crypto.symmetric;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.crypto.enums.CipherAlgorithm;
import com.orion.lang.utils.crypto.enums.PaddingMode;
import com.orion.lang.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * 非对称加密父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/2 18:27
 */
public abstract class BaseSymmetric implements SymmetricCrypto {

    /**
     * 加密算法
     */
    protected CipherAlgorithm algorithm;

    /**
     * 工作模式
     */
    protected WorkingMode workingMode;

    /**
     * 填充模式
     */
    protected PaddingMode paddingMode;

    /**
     * 密钥
     */
    protected SecretKey secretKey;

    protected BaseSymmetric(CipherAlgorithm cipherAlgorithm, WorkingMode workingMode, PaddingMode paddingMode, SecretKey secretKey) {
        this.algorithm = Valid.notNull(cipherAlgorithm, "cipherAlgorithm is null");
        this.workingMode = Valid.notNull(workingMode, "workingMode is null");
        this.paddingMode = Valid.notNull(paddingMode, "paddingMode is null");
        this.secretKey = Valid.notNull(secretKey, "secretKey is null");
    }

    /**
     * 获取 cipher
     *
     * @return cipher
     */
    protected Cipher getCipher() {
        return algorithm.getCipher(workingMode, paddingMode);
    }

    /**
     * ZeroPadding 去除解密后的0位数
     *
     * @param bytes bytes
     * @return bytes
     */
    protected byte[] clearZeroPadding(byte[] bytes) {
        // 如果是0填充的话则需要去除0 否则解密结果的byte[]最后都是0 与明文比对会不匹配
        if (!PaddingMode.ZERO_PADDING.equals(paddingMode)) {
            return bytes;
        }
        int f = bytes.length;
        for (int i = 0; i < f; i++) {
            if (bytes[i] == 0) {
                f = i;
                break;
            }
        }
        if (f == bytes.length) {
            return bytes;
        }
        byte[] res = new byte[f];
        System.arraycopy(bytes, 0, res, 0, f);
        return res;
    }

    /**
     * ZeroPadding 0填充数据
     *
     * @param bytes     数据
     * @param blockSize 块大小
     * @return 0填充块数据
     */
    protected byte[] zeroPadding(byte[] bytes, int blockSize) {
        // 如果是0填充的话则需要补全数据块的0
        if (!PaddingMode.ZERO_PADDING.equals(paddingMode)) {
            return bytes;
        }
        if (bytes.length % blockSize == 0) {
            return bytes;
        }
        int newSize = ((bytes.length / blockSize) + 1) * blockSize;
        byte[] res = new byte[newSize];
        System.arraycopy(bytes, 0, res, 0, bytes.length);
        return res;
    }

    public CipherAlgorithm getAlgorithm() {
        return algorithm;
    }

    public WorkingMode getWorkingMode() {
        return workingMode;
    }

    public PaddingMode getPaddingMode() {
        return paddingMode;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

}
