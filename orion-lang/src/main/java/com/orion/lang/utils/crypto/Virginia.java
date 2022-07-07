package com.orion.lang.utils.crypto;

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
