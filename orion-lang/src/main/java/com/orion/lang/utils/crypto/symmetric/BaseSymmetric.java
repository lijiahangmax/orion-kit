package com.orion.lang.utils.crypto.symmetric;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.crypto.enums.CipherAlgorithm;
import com.orion.lang.utils.crypto.enums.PaddingMode;
import com.orion.lang.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Arrays;

/**
 * 非对称加密父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/2 18:27
 */
public abstract class BaseSymmetric {

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
     * 秘钥
     */
    protected SecretKey secretKey;

    protected BaseSymmetric(CipherAlgorithm cipherAlgorithm, WorkingMode workingMode, PaddingMode paddingMode, SecretKey secretKey) {
        this.algorithm = Valid.notNull(cipherAlgorithm, "cipherAlgorithm is null");
        this.workingMode = Valid.notNull(workingMode, "workingMode is null");
        this.paddingMode = Valid.notNull(paddingMode, "paddingMode is null");
        this.secretKey = Valid.notNull(secretKey, "secretKey is null");
    }

    public byte[] encrypt(String plain) {
        return this.encrypt(Strings.bytes(plain));
    }

    public String encryptAsString(String plain) {
        return new String(this.encrypt(Strings.bytes(plain)));
    }

    public String encryptAsString(byte[] plain) {
        return new String(this.encrypt(plain));
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    public abstract byte[] encrypt(byte[] plain);

    public byte[] decrypt(String text) {
        return this.decrypt(Strings.bytes(text));
    }

    public String decryptAsString(String text) {
        return new String(this.decrypt(Strings.bytes(text)));
    }

    public String decryptAsString(byte[] text) {
        return new String(this.decrypt(text));
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    public abstract byte[] decrypt(byte[] text);

    public boolean verify(String plain, String text) {
        try {
            return plain.equals(this.decryptAsString(text));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证加密结果
     *
     * @param plain 明文
     * @param text  密文
     * @return 是否成功
     */
    public boolean verify(byte[] plain, byte[] text) {
        try {
            return Arrays.equals(plain, this.decrypt(text));
        } catch (Exception e) {
            return false;
        }
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
