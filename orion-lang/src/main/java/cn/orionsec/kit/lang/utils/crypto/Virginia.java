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

/**
 * 维吉尼亚密码实现
 * <p>
 * 不支持中文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 23:04
 */
public class Virginia {

    private Virginia() {
    }

    /**
     * 加密
     *
     * @param data      数据
     * @param cipherKey 密钥
     * @return 密文
     */
    public static String encrypt(String data, String cipherKey) {
        int dataLen = data.length();
        int cipherKeyLen = cipherKey.length();
        char[] cipherArray = new char[dataLen];
        for (int i = 0; i < dataLen / cipherKeyLen + 1; i++) {
            for (int t = 0; t < cipherKeyLen; t++) {
                if (t + i * cipherKeyLen < dataLen) {
                    char dataChar = data.charAt(t + i * cipherKeyLen);
                    char cipherKeyChar = cipherKey.charAt(t);
                    cipherArray[t + i * cipherKeyLen] = (char) ((dataChar + cipherKeyChar - 64) % 95 + 32);
                }
            }
        }
        return new String(cipherArray);
    }

    /**
     * 解密
     *
     * @param data      密文
     * @param cipherKey 密钥
     * @return 明文
     */
    public static String decrypt(String data, String cipherKey) {
        int dataLen = data.length();
        int cipherKeyLen = cipherKey.length();
        char[] clearArray = new char[dataLen];
        for (int i = 0; i < dataLen; i++) {
            for (int t = 0; t < cipherKeyLen; t++) {
                if (t + i * cipherKeyLen < dataLen) {
                    char dataChar = data.charAt(t + i * cipherKeyLen);
                    char cipherKeyChar = cipherKey.charAt(t);
                    if (dataChar - cipherKeyChar >= 0) {
                        clearArray[t + i * cipherKeyLen] = (char) ((dataChar - cipherKeyChar) % 95 + 32);
                    } else {
                        clearArray[t + i * cipherKeyLen] = (char) ((dataChar - cipherKeyChar + 95) % 95 + 32);
                    }
                }
            }
        }
        return new String(clearArray);
    }

}
