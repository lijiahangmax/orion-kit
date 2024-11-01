/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.codec.Base64s;

/**
 * RC4 实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 22:35
 */
public class RC4 {

    private static final int BOX_LENGTH = 256;

    private static final int KEY_MIN_LENGTH = 5;

    private int[] box;

    public RC4(String key) {
        this(Strings.bytes(key));
    }

    public RC4(byte[] key) {
        this.initKey(key);
    }

    public String encrypt(String s) {
        return Base64s.encodeToString(this.encryptOrDecrypt(Strings.bytes(s)));
    }

    /**
     * 加密
     *
     * @param bs 明文
     * @return 密文
     */
    public byte[] encrypt(byte[] bs) {
        return this.encryptOrDecrypt(bs);
    }

    public String decrypt(String s) {
        return new String(this.encryptOrDecrypt(Base64s.decodeToBytes(s)));
    }

    /**
     * 解密
     *
     * @param bs 密文
     * @return 明文
     */
    public byte[] decrypt(byte[] bs) {
        return this.encryptOrDecrypt(bs);
    }

    /**
     * 加密或解密
     *
     * @param bs 要加密或解密的值
     * @return 加密或解密后的值
     */
    public byte[] encryptOrDecrypt(byte[] bs) {
        byte[] code;
        int[] box = this.box.clone();
        code = new byte[bs.length];
        int i = 0;
        int j = 0;
        for (int n = 0; n < bs.length; n++) {
            i = (i + 1) % BOX_LENGTH;
            j = (j + box[i]) % BOX_LENGTH;
            Arrays1.swap(box, i, j);
            int rand = box[(box[i] + box[j]) % BOX_LENGTH];
            code[n] = (byte) (rand ^ bs[n]);
        }
        return code;
    }

    public void initKey(String key) {
        this.initKey(Strings.bytes(key));
    }

    /**
     * 初始化密钥
     *
     * @param key 密钥
     */
    public void initKey(byte[] key) {
        int length = key.length;
        if (length < KEY_MIN_LENGTH || length >= BOX_LENGTH) {
            throw Exceptions.init(Strings.format("key length has to be between {} and {}", KEY_MIN_LENGTH, (BOX_LENGTH - 1)));
        }
        this.box = initBox(key);
    }

    private static int[] initBox(byte[] key) {
        int[] box = new int[BOX_LENGTH];
        int j = 0;
        for (int i = 0; i < BOX_LENGTH; i++) {
            box[i] = i;
        }
        for (int i = 0; i < BOX_LENGTH; i++) {
            j = (j + box[i] + (key[i % key.length]) & 0xFF) % BOX_LENGTH;
            Arrays1.swap(box, i, j);
        }
        return box;
    }

}