package com.orion.lang.utils.crypto;

/**
 * 凯撒密码实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/4 1:08
 */
public class Caesars {

    public static final String TABLE = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

    private final int keys;

    public Caesars() {
        this(3);
    }

    public Caesars(int keys) {
        this.keys = keys;
    }

    /**
     * 加密
     *
     * @param s 明文
     * @return 密文
     */
    public String encrypt(String s) {
        char[] plain = s.toCharArray();
        for (int i = 0; i < plain.length; i++) {
            if (!Character.isLetter(plain[i])) {
                continue;
            }
            plain[i] = cipher(plain[i]);
        }
        return new String(plain);
    }

    /**
     * 解密
     *
     * @param s 密文
     * @return 明文
     */
    public String decrypt(String s) {
        char[] plain = s.toCharArray();
        for (int i = 0; i < plain.length; i++) {
            if (!Character.isLetter(plain[i])) {
                continue;
            }
            plain[i] = decCipher(plain[i]);
        }
        return new String(plain);
    }

    /**
     * 加密轮盘
     *
     * @param str str
     * @return 密文
     */
    private char cipher(char str) {
        int position = (TABLE.indexOf(str) + keys) % 52;
        return TABLE.charAt(position);
    }

    /**
     * 解密轮盘
     *
     * @param str str
     * @return 明文
     */
    private char decCipher(char str) {
        int position = (TABLE.indexOf(str) - keys) % 52;
        position = position < 0 ? 52 + position : position;
        return TABLE.charAt(position);
    }

}
