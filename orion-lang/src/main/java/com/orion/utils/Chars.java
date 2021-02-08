package com.orion.utils;

import com.orion.constant.Letters;

/**
 * Char 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 10:28
 */
public class Chars {

    private Chars() {
    }

    /**
     * 是否为ASCII字符 0~127
     *
     * @param c c
     * @return ignore
     */
    public static boolean isAscii(char c) {
        return c < 128;
    }

    /**
     * 是否为可见ASCII字符 32~126
     *
     * @param c c
     * @return ignore
     */
    public static boolean isAsciiPrintable(char c) {
        return c >= 32 && c < 127;
    }

    /**
     * 是否为控制符ASCII 0~31 | 127
     *
     * @param c c
     * @return ignore
     */
    public static boolean isAsciiControl(char c) {
        return c < 32 || c == 127;
    }

    /**
     * 判断是否为字母
     *
     * @param c c
     * @return ignore
     */
    public static boolean isLetter(char c) {
        return isLetterUpper(c) || isLetterLower(c);
    }

    /**
     * 判断是否为大写字母
     *
     * @param c c
     * @return ignore
     */
    public static boolean isLetterUpper(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     * 判断是否为小写字母
     *
     * @param c c
     * @return ignore
     */
    public static boolean isLetterLower(char c) {
        return c >= 'a' && c <= 'z';
    }

    /**
     * 检查是否为数字字符
     *
     * @param c c
     * @return ignore
     */
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * 是否为16进制规范的字符
     *
     * @param c c
     * @return ignore
     */
    public static boolean isHexChar(char c) {
        return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * toString
     *
     * @param c c
     * @return ignore
     */
    public static String toString(char c) {
        return String.valueOf(c);
    }

    /**
     * 是否为空白符
     *
     * @param c c
     * @return ignore
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否为空白符
     *
     * @param c c
     * @return ignore
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }

    /**
     * 是否为emoji
     *
     * @param c c
     * @return ignore
     */
    public static boolean isEmoji(char c) {
        return !(c == 0x0 || c == 0x9 || c == 0xA || c == 0xD || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD);
    }

    /**
     * 是否为文件分隔符
     *
     * @param c c
     * @return ignore
     */
    public static boolean isFileSeparator(char c) {
        return Letters.SLASH == c || Letters.BACKSLASH == c;
    }

    /**
     * 比较两个字符是否相同
     *
     * @param c1         c1
     * @param c2         c2
     * @param ignoreCase 是否忽略大小写
     * @return ignore
     */
    public static boolean equals(char c1, char c2, boolean ignoreCase) {
        if (ignoreCase) {
            return Character.toLowerCase(c1) == Character.toLowerCase(c2);
        }
        return c1 == c2;
    }

    /**
     * 获取字符类型
     *
     * @param c c
     * @return 字符类型
     */
    public static int getType(int c) {
        return Character.getType(c);
    }

    /**
     * 获取字符的16进制数值
     *
     * @param b 字符
     * @return 16进制字符
     */
    public static int digit16(char b) {
        return Character.digit(b, 16);
    }

}
