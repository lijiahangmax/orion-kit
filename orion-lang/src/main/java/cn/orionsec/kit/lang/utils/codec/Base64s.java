/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.codec;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;

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
     * base64 编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String encode(String s) {
        return new String(Base64.getEncoder().encode(Strings.bytes(s)));
    }

    /**
     * base64 编码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] encodeToBytes(String s) {
        return Base64.getEncoder().encode(Strings.bytes(s));
    }

    /**
     * base64 编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

    /**
     * base64 编码
     *
     * @param b ignore
     * @return ignore
     */
    public static String encodeToString(byte[] b) {
        return new String(Base64.getEncoder().encode(b));
    }

    /**
     * base64 解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String decode(String s) {
        return new String(Base64.getDecoder().decode(s));
    }

    /**
     * base64 解码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] decodeToBytes(String s) {
        return Base64.getDecoder().decode(s);
    }

    /**
     * base64 解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] decode(byte[] b) {
        return Base64.getDecoder().decode(b);
    }

    /**
     * base64 解码
     *
     * @param b ignore
     * @return ignore
     */
    public static String decodeToString(byte[] b) {
        return new String(Base64.getDecoder().decode(b));
    }

    // -------------------- mime type --------------------


    /**
     * mimeType base64 编码
     *
     * @param s        s
     * @param mimeType mimeType
     * @return base64
     */
    public static String mimeTypeEncode(String s, String mimeType) {
        return mimeTypeEncode(Strings.bytes(s), mimeType);
    }

    /**
     * mimeType base64 编码
     *
     * @param bs       bs
     * @param mimeType mimeType
     * @return base64
     */
    public static String mimeTypeEncode(byte[] bs, String mimeType) {
        return "data:" + mimeType + ";base64," + encodeToString(bs);
    }

    /**
     * mimeType base64 解码
     *
     * @param s base64
     * @return 图片
     */
    public static byte[] mimeTypeDecode(String s) {
        String[] b = s.split(Const.COMMA);
        return decode(Strings.bytes(Arrays1.last(b)));
    }

    /**
     * 获取 base64 mimeType
     *
     * @param s base64
     * @return 图片类型
     */
    public static String getMimeType(String s) {
        String[] b = s.split(Const.COMMA);
        if (b.length == 0) {
            return Strings.EMPTY;
        }
        return b[0].split(Const.SEMICOLON)[0];
    }

    /**
     * 获取 base64 mimeType 前半段
     *
     * @param s base64
     * @return 数据分类
     */
    public static String getMimeTypeFirst(String s) {
        return Arrays1.first(getMimeType(s).split(Const.SLASH));
    }

    /**
     * 获取 base64 mimeType 后半段
     *
     * @param s base64
     * @return 数据类型
     */
    public static String getMimeTypeLast(String s) {
        return Arrays1.last(getMimeType(s).split(Const.SLASH));
    }

    // -------------------- image --------------------

    /**
     * 图片 base64 编码
     *
     * @param bs bs
     * @return base64
     */
    public static String imgEncode(byte[] bs) {
        return imgEncode(bs, Const.SUFFIX_PNG);
    }

    /**
     * 图片 base64 编码
     *
     * @param bs   bs
     * @param type jpg, jpeg, png
     * @return base64
     */
    public static String imgEncode(byte[] bs, String type) {
        return mimeTypeEncode(bs, "image/" + type);
    }

    // -------------------- url --------------------

    /**
     * base64 url 编码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Encode(String s) {
        return new String(Base64.getUrlEncoder().encode(Strings.bytes(s)));
    }

    /**
     * base64 url 编码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] url64EncodeToBytes(String s) {
        return Base64.getUrlEncoder().encode(Strings.bytes(s));
    }

    /**
     * base64 url 编码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Encode(byte[] b) {
        return Base64.getUrlEncoder().encode(b);
    }

    /**
     * base64 url 编码
     *
     * @param b ignore
     * @return ignore
     */
    public static String url64EncodeToString(byte[] b) {
        return new String(Base64.getUrlEncoder().encode(b));
    }

    /**
     * base64 url 解码
     *
     * @param s ignore
     * @return ignore
     */
    public static String url64Decode(String s) {
        return new String(Base64.getUrlDecoder().decode(s));
    }

    /**
     * base64 url 解码
     *
     * @param s ignore
     * @return ignore
     */
    public static byte[] url64DecodeToBytes(String s) {
        return Base64.getUrlDecoder().decode(s);
    }

    /**
     * base64 url 解码
     *
     * @param b ignore
     * @return ignore
     */
    public static byte[] url64Decode(byte[] b) {
        return Base64.getUrlDecoder().decode(b);
    }

    /**
     * base64 url 解码
     *
     * @param b ignore
     * @return ignore
     */
    public static String url64DecodeToString(byte[] b) {
        return new String(Base64.getUrlDecoder().decode(b));
    }

}
