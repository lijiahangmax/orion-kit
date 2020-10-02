package com.orion.utils.crypto;

import java.util.Base64;

/**
 * Base64工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/29 17:12
 */
public class Base64s {

    private Base64s() {
    }

    /**
     * 图片base64编码
     *
     * @param bs bs
     * @return base64
     */
    public static String img64Encode(byte[] bs) {
        return img64Encode(bs, "png");
    }

    /**
     * 图片base64编码
     *
     * @param bs   bs
     * @param type jpg, jepg, png
     * @return base64
     */
    public static String img64Encode(byte[] bs, String type) {
        return "data:image/" + type + ";base64," + new String(base64Encode(bs));
    }

    /**
     * 图片base64解码
     *
     * @param s base64
     * @return 图片
     */
    public static byte[] img64Decode(String s) {
        // int i = s.indexOf("base64");
        // return base64Decode(s.substring(i + 7, s.length()).getBytes());
        String[] b = s.split(",");
        return base64Decode(b[b.length - 1].getBytes());
    }

    /**
     * base64编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String base64Encode(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()));
    }

    /**
     * base64编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] base64Encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

    /**
     * base64解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String base64Decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }

    /**
     * base64解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] base64Decode(byte[] b) {
        return Base64.getDecoder().decode(b);
    }

    /**
     * base64url编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Encode(String s) {
        return new String(Base64.getUrlEncoder().encode(s.getBytes()));
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
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Decode(byte[] b) {
        return Base64.getUrlDecoder().decode(b);
    }

}
