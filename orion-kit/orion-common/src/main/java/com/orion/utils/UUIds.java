package com.orion.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * uuid工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/3 11:26
 */
public class UUIds {

    private UUIds() {
    }

    private final static String STR_BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static char[] DIGITS = STR_BASE.toCharArray();
    private final static Map<Character, Integer> DIGIT_MAP = new HashMap<>();

    static {
        for (int i = 0; i < DIGITS.length; i++) {
            DIGIT_MAP.put(DIGITS[i], i);
        }
    }

    /**
     * 支持的最小进制数
     */
    private static final int MIN_RADIX = 2;

    /**
     * 支持的最大进制数
     */
    private static final int MAX_RADIX = DIGITS.length;

    /**
     * 获取36位UUID
     */
    public static String random() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取32位UUID
     */
    public static String random32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取19位的UUID
     */
    public static String random19() {
        // 产生UUID
        UUID uuid = UUID.randomUUID();
        // 分区转换
        return digits(uuid.getMostSignificantBits() >> 32, 8) +
                digits(uuid.getMostSignificantBits() >> 16, 4) +
                digits(uuid.getMostSignificantBits(), 4) +
                digits(uuid.getLeastSignificantBits() >> 48, 4) +
                digits(uuid.getLeastSignificantBits(), 12);
    }

    /**
     * 获取15位的UUID (精度有所损失)
     */
    public static String random15() {
        return UUIDMaker.generate();
    }

    /**
     * 获取15位的Long型UUID (精度有所损失)
     */
    public static long random15Long() {
        return toNumber(random15(), 10);
    }

    /**
     * 获取36位UUID进行编码
     */
    public static String randomBase64() {
        UUID uuid = UUID.randomUUID();
        byte[] byUuid = new byte[16];
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();
        long2bytes(most, byUuid, 0);
        long2bytes(least, byUuid, 8);
        return Base64.getEncoder().encodeToString(byUuid);
    }

    private static void long2bytes(long value, byte[] bytes, int offset) {
        for (int i = 7; i > -1; i--) {
            bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
        }
    }

    /**
     * 将字符串转换为长整型数字
     *
     * @param s     数字字符串
     * @param radix 进制数
     */
    private static long toNumber(String s, int radix) {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Numbers.MIN_RADIX");
        }
        if (radix > MAX_RADIX) {
            throw new NumberFormatException("radix " + radix + " greater than Numbers.MAX_RADIX");
        }
        boolean negative = false;
        Integer digit, i = 0, len = s.length();
        long result = 0, limit = -Long.MAX_VALUE, multmin;
        if (len <= 0) {
            throw swap(s);
        }
        char firstChar = s.charAt(0);
        if (firstChar < '0') {
            if (firstChar == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
            } else if (firstChar != '+') {
                throw swap(s);
            }
            if (len == 1) {
                throw swap(s);
            }
            i++;
        }
        multmin = limit / radix;
        while (i < len) {
            digit = DIGIT_MAP.get(s.charAt(i++));
            if (digit == null || digit < 0 || result < multmin) {
                throw swap(s);
            }
            result *= radix;
            if (result < limit + digit) {
                throw swap(s);
            }
            result -= digit;
        }
        return negative ? result : -result;
    }

    /**
     * 将长整型数值转换为指定的进制数 (最大支持62进制，字母数字已经用尽)
     *
     * @param num   num
     * @param radix 进制
     * @return string value
     */
    private static String toString(long num, int radix) {
        if (radix < MIN_RADIX || radix > MAX_RADIX) {
            radix = 10;
        }
        if (radix == 10) {
            return Long.toString(num);
        }
        final int size = 65;
        int charPos = 64;
        char[] buf = new char[size];
        boolean negative = (num < 0);
        if (!negative) {
            num = -num;
        }
        while (num <= -radix) {
            buf[charPos--] = DIGITS[(int) (-(num % radix))];
            num = num / radix;
        }
        buf[charPos] = DIGITS[(int) (-num)];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (size - charPos));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return toString(hi | (val & (hi - 1)), MAX_RADIX).substring(1);
    }

    private static NumberFormatException swap(String s) {
        return new NumberFormatException("For input string: " + s);
    }

    private static class UUIDMaker {

        private final static String STR = "0123456789abcdefghijklmnopqrstuvwxyz";
        private final static int PIX_LEN = STR.length();
        private static volatile int pixOne = 0;
        private static volatile int pixTwo = 0;
        private static volatile int pixThree = 0;
        private static volatile int pixFour = 0;

        /**
         * 生成短时间内不会重复的长度为15位的字符串, 主要用于模块数据库主键生成使用
         * 生成策略为获取自1970年1月1日零时零分零秒至当前时间的毫秒数的16进制字符串值, 该字符串值为11位
         * 并追加四位"0-z"的自增字符串
         * 如果系统时间设置为大于 2304-6-27 7:00:26 的时间将会报错
         * 由于系统返回的毫秒数与操作系统关系很大, 所以本方法并不准确
         * 本方法可以保证在系统返回的一个毫秒数内生成36的4次方个 (1679616) ID不重复
         */
        private synchronized static String generate() {
            String hexString = Long.toHexString(System.currentTimeMillis());
            pixFour++;
            if (pixFour == PIX_LEN) {
                pixFour = 0;
                pixThree++;
                if (pixThree == PIX_LEN) {
                    pixThree = 0;
                    pixTwo++;
                    if (pixTwo == PIX_LEN) {
                        pixTwo = 0;
                        pixOne++;
                        if (pixOne == PIX_LEN) {
                            pixOne = 0;
                        }
                    }
                }
            }
            return hexString + STR.charAt(pixOne) + STR.charAt(pixTwo) + STR.charAt(pixThree) + STR.charAt(pixFour);
        }
    }

}
