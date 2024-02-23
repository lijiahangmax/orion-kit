package com.orion.lang.utils.codec;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.util.Base64;

/**
 * Base64 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 17:12
 */
public class Base64s {

    private Base64s() {
    }

    // -------------------- data --------------------

    /**
     * base64编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String encode(String s) {
        return new String(Base64.getEncoder().encode(Strings.bytes(s)));
    }

    /**
     * base64编码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] encodeToBytes(String s) {
        return Base64.getEncoder().encode(Strings.bytes(s));
    }

    /**
     * base64编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

    /**
     * base64编码
     *
     * @param b ignore
     * @return ignore
     */
    public static String encodeToString(byte[] b) {
        return new String(Base64.getEncoder().encode(b));
    }

    /**
     * base64解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }

    /**
     * base64解码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] decodeToBytes(String s) {
        return Base64.getDecoder().decode(s);
    }

    /**
     * base64解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] decode(byte[] b) {
        return Base64.getDecoder().decode(b);
    }

    /**
     * base64解码
     *
     * @param b ignore
     * @return ignore
     */
    public static String decodeToString(byte[] b) {
        return new String(Base64.getDecoder().decode(b));
    }

    // -------------------- image --------------------

    /**
     * 图片base64编码
     *
     * @param bs bs
     * @return base64
     */
    public static String img64Encode(byte[] bs) {
        return img64Encode(bs, Const.SUFFIX_PNG);
    }

    /**
     * 图片base64编码
     *
     * @param bs   bs
     * @param type jpg, jpeg, png
     * @return base64
     */
    public static String img64Encode(byte[] bs, String type) {
        return "data:image/" + type + ";base64," + new String(encode(bs));
    }

    /**
     * 图片base64解码
     *
     * @param s base64
     * @return 图片
     */
    public static byte[] img64Decode(String s) {
        // int i = s.indexOf("base64");
        // return decode(Strings.bytes(s.substring(i + 7)));
        String[] b = s.split(Const.COMMA);
        return decode(Strings.bytes(b[b.length - 1]));
    }

    /**
     * 获取base64图片类型
     *
     * @param s base64
     * @return 图片类型
     */
    public static String img64Type(String s) {
        String[] b = s.split(Const.COMMA);
        if (b.length == 0) {
            return Strings.EMPTY;
        }
        String dataImage = b[0].split(";")[0];
        String[] dataType = dataImage.split("/");
        if (dataType.length == 0) {
            return Strings.EMPTY;
        }
        return dataType[1];
    }

    // -------------------- url --------------------

    /**
     * base64url编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Encode(String s) {
        return new String(Base64.getUrlEncoder().encode(Strings.bytes(s)));
    }

    /**
     * base64url编码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] url64EncodeToBytes(String s) {
        return Base64.getUrlEncoder().encode(Strings.bytes(s));
    }

    /**
     * base64url编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Encode(byte[] b) {
        return Base64.getUrlEncoder().encode(b);
    }

    /**
     * base64url编码
     *
     * @param b ignore
     * @return ignore
     */
    public static String url64EncodeToString(byte[] b) {
        return new String(Base64.getUrlEncoder().encode(b));
    }

    /**
     * base64url解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Decode(String s) {
        return new String(Base64.getUrlDecoder().decode(s));
    }

    /**
     * base64url解码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] url64DecodeToBytes(String s) {
        return Base64.getUrlDecoder().decode(s);
    }

    /**
     * base64url解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Decode(byte[] b) {
        return Base64.getUrlDecoder().decode(b);
    }

    /**
     * base64url解码
     *
     * @param b ignore
     * @return ignore
     */
    public static String url64DecodeToString(byte[] b) {
        return new String(Base64.getUrlDecoder().decode(b));
    }

}
