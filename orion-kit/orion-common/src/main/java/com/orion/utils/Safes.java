package com.orion.utils;

/**
 * 脱敏工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/10 9:45
 */
public class Safes {

    private static final String SAFE_CHAR = "*";

    private Safes() {
    }

    /**
     * 字符串脱敏
     *
     * @param s      原字符
     * @param start  脱敏开始下标
     * @param end    脱敏结束下标
     * @param safe   脱敏字符串
     * @param repeat 脱敏字符重复次数
     * @return 脱敏字符串
     */
    public static String mix(String s, int start, int end, String safe, int repeat) {
        int length = Strings.length(s);
        if (length == 0) {
            return "";
        }
        if (Strings.isBlank(safe)) {
            safe = SAFE_CHAR;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > length) {
            end = length;
        }
        char[] chars = s.toCharArray();
        char[] safeChars = Strings.repeatArr(safe, repeat);
        char[] res = new char[start + (length - end) + safeChars.length];
        System.arraycopy(chars, 0, res, 0, start);
        System.arraycopy(safeChars, 0, res, start, safeChars.length);
        System.arraycopy(chars, end, res, start + safeChars.length, length - end);
        return new String(res);
    }

    /**
     * 脱敏姓
     *
     * @param s 姓
     * @return 脱敏后的姓
     */
    public static String mixSurName(String s) {
        return mix(s, 0, 1, "*", 1);
    }

    /**
     * 脱敏名
     *
     * @param s 名
     * @return 脱敏后的名
     */
    public static String mixName(String s) {
        int length = Strings.length(s);
        return mix(s, 1, length, "*", length - 1);
    }

    /**
     * 脱敏手机号
     *
     * @param s 手机号
     * @return 脱敏后的手机号
     */
    public static String mixPhone(String s) {
        return mix(s, 3, 7, "*", 4);
    }

    /**
     * 脱敏身份证
     *
     * @param s 身份证
     * @return 脱敏后的身份证
     */
    public static String mixCardNum(String s) {
        return mix(s, 6, 14, "*", 8);
    }

}
