package com.orion.utils.crypto.symmetric;

import com.orion.utils.Strings;
import com.orion.utils.crypto.Keys;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.PaddingMode;
import com.orion.utils.crypto.enums.WorkingMode;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 非对称加密构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/30 1:12
 */
public class SymmetricBuilder {

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

    /**
     * 参数规格
     */
    protected AlgorithmParameterSpec paramSpec;

    /**
     * aad
     */
    private byte[] aad;

    public SymmetricBuilder() {
        this.workingMode = WorkingMode.ECB;
        this.paddingMode = PaddingMode.PKCS5_PADDING;
    }

    /**
     * 创建构造器
     *
     * @return 构造器
     */
    public static SymmetricBuilder create() {
        return new SymmetricBuilder();
    }

    /**
     * 创建 AES 构造器
     *
     * @return 构造器
     */
    public static SymmetricBuilder aes() {
        return new SymmetricBuilder().algorithm(CipherAlgorithm.AES);
    }

    /**
     * 创建 DES 构造器
     *
     * @return 构造器
     */
    public static SymmetricBuilder des() {
        return new SymmetricBuilder().algorithm(CipherAlgorithm.DES);
    }

    /**
     * 创建 DES3 构造器
     *
     * @return 构造器
     */
    public static SymmetricBuilder des3() {
        return new SymmetricBuilder().algorithm(CipherAlgorithm.DES3);
    }

    /**
     * 创建 SM4 构造器
     *
     * @return 构造器
     */
    public static SymmetricBuilder sm4() {
        return new SymmetricBuilder().algorithm(CipherAlgorithm.SM4);
    }

    /**
     * 设置加密算法
     *
     * @param algorithm algorithm
     * @return this
     */
    public SymmetricBuilder algorithm(CipherAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * 设置工作模式
     *
     * @param workingMode 工作模式
     * @return this
     */
    public SymmetricBuilder workingMode(WorkingMode workingMode) {
        this.workingMode = workingMode;
        return this;
    }

    /**
     * 设置填充模式
     *
     * @param paddingMode 填充模式
     * @return this
     */
    public SymmetricBuilder paddingMode(PaddingMode paddingMode) {
        this.paddingMode = paddingMode;
        return this;
    }

    /**
     * 设置秘钥
     *
     * @param secretKey secretKey
     * @return this
     */
    public SymmetricBuilder secretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * 设置秘钥
     *
     * @param secretKey secretKey
     * @return this
     */
    public SymmetricBuilder secretKey(byte[] secretKey) {
        this.secretKey = Keys.getSecretKey(secretKey, algorithm);
        return this;
    }

    /**
     * 生成秘钥
     *
     * @param secretKey secretKey
     * @return this
     */
    public SymmetricBuilder generatorSecretKey(byte[] secretKey) {
        this.secretKey = Keys.generatorKey(secretKey, algorithm);
        return this;
    }

    /**
     * 生成秘钥
     *
     * @param secretKey secretKey
     * @return this
     */
    public SymmetricBuilder generatorSecretKey(String secretKey) {
        this.secretKey = Keys.generatorKey(secretKey, algorithm);
        return this;
    }

    /**
     * 生成秘钥
     *
     * @param secretKey secretKey
     * @param keySize   keySize
     * @return this
     */
    public SymmetricBuilder generatorSecretKey(byte[] secretKey, int keySize) {
        this.secretKey = Keys.generatorKey(secretKey, keySize, algorithm);
        return this;
    }

    /**
     * 生成秘钥
     *
     * @param secretKey secretKey
     * @param keySize   keySize
     * @return this
     */
    public SymmetricBuilder generatorSecretKey(String secretKey, int keySize) {
        this.secretKey = Keys.generatorKey(secretKey, keySize, algorithm);
        return this;
    }

    /**
     * 设置向量
     *
     * @param ivSpec ivSpec
     * @return this
     */
    public SymmetricBuilder ivSpec(IvParameterSpec ivSpec) {
        this.paramSpec = ivSpec;
        return this;
    }

    /**
     * 设置向量
     *
     * @param iv iv
     * @return this
     */
    public SymmetricBuilder ivSpec(String iv) {
        return this.ivSpec(Strings.bytes(iv), false);
    }

    /**
     * 设置向量
     *
     * @param iv iv
     * @return this
     */
    public SymmetricBuilder ivSpec(byte[] iv) {
        return this.ivSpec(iv, false);
    }

    /**
     * 设置向量
     *
     * @param iv      iv
     * @param specLen specLen
     * @return this
     */
    public SymmetricBuilder ivSpec(String iv, int specLen) {
        return this.ivSpec(Strings.bytes(iv), specLen);
    }

    /**
     * 设置向量
     *
     * @param iv      iv
     * @param specLen specLen
     * @return this
     */
    public SymmetricBuilder ivSpec(byte[] iv, int specLen) {
        this.paramSpec = Keys.getIvSpec(iv, specLen);
        return this;
    }

    /**
     * 设置向量
     *
     * @param iv   iv
     * @param fill 是否填充长度
     * @return this
     */
    public SymmetricBuilder ivSpec(String iv, boolean fill) {
        return this.ivSpec(Strings.bytes(iv), fill);
    }

    /**
     * 设置向量
     *
     * @param iv   iv
     * @param fill 是否填充长度
     * @return this
     */
    public SymmetricBuilder ivSpec(byte[] iv, boolean fill) {
        if (fill) {
            this.paramSpec = Keys.getIvSpec(algorithm, iv);
        } else {
            this.paramSpec = Keys.getIvSpec(iv);
        }
        return this;
    }

    /**
     * 设置 gcm规格
     *
     * @param gcmSpec gcm规格
     * @return this
     */
    public SymmetricBuilder gcmSpec(GCMParameterSpec gcmSpec) {
        this.paramSpec = gcmSpec;
        return this;
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm gcm
     * @return this
     */
    public SymmetricBuilder gcmSpec(String gcm) {
        return this.gcmSpec(Strings.bytes(gcm), false);
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm gcm
     * @return this
     */
    public SymmetricBuilder gcmSpec(byte[] gcm) {
        return this.gcmSpec(gcm, false);
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm     gcm
     * @param specLen specLen
     * @return this
     */
    public SymmetricBuilder gcmSpec(String gcm, int specLen) {
        return this.gcmSpec(Strings.bytes(gcm), specLen);
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm     gcm
     * @param specLen specLen
     * @return this
     */
    public SymmetricBuilder gcmSpec(byte[] gcm, int specLen) {
        this.paramSpec = Keys.getGcmSpec(gcm, specLen);
        return this;
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm  gcm
     * @param fill 是否填充长度
     * @return this
     */
    public SymmetricBuilder gcmSpec(String gcm, boolean fill) {
        return this.gcmSpec(Strings.bytes(gcm), fill);
    }

    /**
     * 设置 GCM 规格
     *
     * @param gcm  gcm
     * @param fill 是否填充长度
     * @return this
     */
    public SymmetricBuilder gcmSpec(byte[] gcm, boolean fill) {
        if (fill) {
            this.paramSpec = Keys.getGcmSpec(algorithm, gcm);
        } else {
            this.paramSpec = Keys.getGcmSpec(gcm);
        }
        return this;
    }

    /**
     * 设置参数规格
     *
     * @param paramSpec paramSpec
     * @return this
     */
    public SymmetricBuilder paramSpec(AlgorithmParameterSpec paramSpec) {
        this.paramSpec = paramSpec;
        return this;
    }

    /**
     * 设置 aad
     *
     * @param aad aad
     * @return this
     */
    public SymmetricBuilder aad(String aad) {
        return this.aad(Strings.bytes(aad));
    }

    /**
     * 设置 aad
     *
     * @param aad aad
     * @return this
     */
    public SymmetricBuilder aad(byte[] aad) {
        this.aad = aad;
        return this;
    }

    /**
     * 构建 ECB
     *
     * @return EcbSymmetric
     */
    public EcbSymmetric buildEcb() {
        return new EcbSymmetric(algorithm, paddingMode, secretKey);
    }

    /**
     * 构建 PARAM
     *
     * @return ParamSymmetric
     */
    public ParamSymmetric buildParam() {
        ParamSymmetric symmetric = new ParamSymmetric(algorithm, workingMode, paddingMode, secretKey, paramSpec);
        symmetric.setAad(aad);
        return symmetric;
    }

}
