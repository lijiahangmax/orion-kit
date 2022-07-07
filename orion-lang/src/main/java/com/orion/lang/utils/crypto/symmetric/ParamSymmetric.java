package com.orion.lang.utils.crypto.symmetric;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.crypto.enums.CipherAlgorithm;
import com.orion.lang.utils.crypto.enums.PaddingMode;
import com.orion.lang.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

import static com.orion.lang.utils.codec.Base64s.decode;
import static com.orion.lang.utils.codec.Base64s.encode;

/**
 * CBC CFB OFB FTP GCM 模式非对称加密 AES DES 3DES
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/3 14:45
 */
public class ParamSymmetric extends BaseSymmetric {

    /**
     * 参数规格
     */
    private final AlgorithmParameterSpec paramSpec;

    /**
     * aad
     */
    private byte[] aad;

    public ParamSymmetric(CipherAlgorithm cipherAlgorithm, WorkingMode workingMode, SecretKey secretKey, AlgorithmParameterSpec paramSpec) {
        this(cipherAlgorithm, workingMode, PaddingMode.PKCS5_PADDING, secretKey, paramSpec);
    }

    public ParamSymmetric(CipherAlgorithm cipherAlgorithm, WorkingMode workingMode, PaddingMode paddingMode, SecretKey secretKey, AlgorithmParameterSpec paramSpec) {
        super(cipherAlgorithm, workingMode, paddingMode, secretKey);
        this.paramSpec = Valid.notNull(paramSpec, "paramSpec is null");
    }

    public void setAad(String aad) {
        this.aad = Strings.bytes(aad);
    }

    public void setAad(byte[] aad) {
        this.aad = aad;
    }

    @Override
    public byte[] encrypt(byte[] plain) {
        try {
            Cipher cipher = super.getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            if (aad != null) {
                cipher.updateAAD(aad);
            }
            return encode(cipher.doFinal(this.zeroPadding(plain, cipher.getBlockSize())));
        } catch (Exception e) {
            throw Exceptions.encrypt("encrypt data error", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] text) {
        try {
            Cipher cipher = super.getCipher();
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            if (aad != null) {
                cipher.updateAAD(aad);
            }
            return this.clearZeroPadding(cipher.doFinal(decode(text)));
        } catch (Exception e) {
            throw Exceptions.encrypt("decrypt data error", e);
        }
    }

}
