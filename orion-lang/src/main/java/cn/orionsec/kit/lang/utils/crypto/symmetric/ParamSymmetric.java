/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.crypto.symmetric;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.crypto.enums.PaddingMode;
import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

import static cn.orionsec.kit.lang.utils.codec.Base64s.decode;
import static cn.orionsec.kit.lang.utils.codec.Base64s.encode;

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
            throw Exceptions.decrypt("decrypt data error", e);
        }
    }

}
