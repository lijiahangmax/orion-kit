package com.orion.utils;

/**
 * 字符编码转换
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/8 19:13
 */
public class CharConverts {

    private static final String UNICODE_PREFIX = "\\u";

    private static final String HTML_ENTITY_PREFIX = "&#";

    private static final String HTML_ENTITY_SUFFIX = ";";

    private CharConverts() {
    }

    /**
     * char -> html entity
     * xx -> &#xxx;
     *
     * @param str char
     * @return html entity
     */
    public static String toHtmlEntity(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            sb.append(HTML_ENTITY_PREFIX).append((int) c).append(HTML_ENTITY_SUFFIX);
        }
        return sb.toString();
    }

    /**
     * html entity -> char
     * &#xxx; -> xx
     *
     * @param value html entity
     * @return str
     */
    public static String fromHtmlEntity(String value) {
        StringBuilder sb = new StringBuilder();
        String[] chars = value.split(";");
        for (String un : chars) {
            sb.append((char) Integer.parseInt(un.trim().substring(2)));
        }
        return sb.toString();
    }

    public static String toUnicode(String str) {
        return toUnicode(str, false);
    }

    /**
     * char -> unicode
     * xx -> \\u
     *
     * @param str           str
     * @param convertNumber 是否转换字母和数字
     * @return unicode
     */
    public static String toUnicode(String str, boolean convertNumber) {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(toUnicodeChar(c, convertNumber));
        }
        return sb.toString();
    }

    /**
     * char -> unicode
     * xx -> \\u
     *
     * @param c             char
     * @param convertNumber 是否转换字母和数字
     * @return unicode
     */
    private static String toUnicodeChar(char c, boolean convertNumber) {
        if (convertNumber || c > 255) {
            StringBuilder sb = new StringBuilder();
            sb.append(UNICODE_PREFIX);
            int code = (c >> 8);
            String tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            code = (c & 0xFF);
            tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            return sb.toString();
        } else {
            return Character.toString(c);
        }
    }

    /**
     * unicode -> str
     * \\u -> xx
     *
     * @param str unicode
     * @return str
     */
    public static String fromUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        int begin = 0;
        int index = str.indexOf(UNICODE_PREFIX);
        while (index != -1) {
            sb.append(str, begin, index);
            sb.append(fromUnicodeChar(str.substring(index, index + 6)));
            begin = index + 6;
            index = str.indexOf(UNICODE_PREFIX, begin);
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    /**
     * unicode -> char
     * \\u -> xx
     *
     * @param str unicode
     * @return char
     */
    private static char fromUnicodeChar(String str) {
        if (str.length() != 6) {
            throw Exceptions.argument("ascii string of a native character must be 6 character.");
        }
        if (!UNICODE_PREFIX.equals(str.substring(0, 2))) {
            throw Exceptions.argument("ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        int code = Integer.parseInt(tmp, 16) << 8;
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16);
        return (char) code;
    }

    /**
     * str -> hex
     * xx -> %xx
     *
     * @param str str
     * @return hex
     */
    public static String toHex(String str) {
        return toHex(Strings.bytes(str));
    }

    /**
     * byte -> hex
     * xx -> %xx
     *
     * @param bs byte
     * @return hex
     */
    public static String toHex(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            sb.append(String.format("%%%02X", b));
        }
        return sb.toString();
    }

    /**
     * hex -> char
     * %xx -> xx
     *
     * @param str hex
     * @return char
     */
    public static String fromHex(String str) {
        return new String(fromHexByte(str));
    }

    /**
     * hex -> byte
     * %xx -> xx
     *
     * @param str hex
     * @return byte
     */
    public static byte[] fromHexByte(String str) {
        int l = str.length() / 3;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(str.substring(i * 3 + 1, i * 3 + 3), 16).byteValue();
        }
        return ret;
    }

}
