package com.orion.lang;

import com.orion.utils.*;

import java.io.Serializable;
import java.util.Date;

/**
 * String 拓展类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/13 14:11
 */
public class StringExt implements Serializable {

    private static final long serialVersionUID = 8675244107435484L;

    private String s;

    public StringExt() {
        s = "";
    }

    public StringExt(String s) {
        this.s = s;
    }

    public StringExt(String s, String def) {
        this.s = Objects1.det(s, def);
    }

    /**
     * 获取值
     *
     * @return 值
     */
    public String get() {
        return s;
    }

    /**
     * 获取值
     *
     * @param def 默认值
     * @return 值
     */
    public String get(String def) {
        return Objects1.det(s, def);
    }

    /**
     * 设置值
     *
     * @param s string
     * @return this
     */
    public StringExt set(String s) {
        this.s = s;
        return this;
    }

    /**
     * 设置值
     *
     * @param s   string
     * @param def 默认值
     * @return this
     */
    public StringExt set(String s, String def) {
        this.s = Objects1.det(s, def);
        return this;
    }

    /**
     * 是否为空白
     *
     * @return true 空白
     */
    public boolean isBlank() {
        return Strings.isBlank(s);
    }

    /**
     * 是否不为空白
     *
     * @return false 空白
     */
    public boolean isNotBlank() {
        return Strings.isNotBlank(s);
    }

    /**
     * 是否为null
     *
     * @return true null
     */
    public boolean isNull() {
        return s == null;
    }

    /**
     * 是否不为null
     *
     * @return false null
     */
    public boolean isNotNull() {
        return s != null;
    }

    /**
     * 是否为空
     *
     * @return true 空
     */
    public boolean isEmpty() {
        return Strings.isEmpty(s);
    }

    /**
     * 是否不为空
     *
     * @return false 空
     */
    public boolean isNotEmpty() {
        return Strings.isNotEmpty(s);
    }

    /**
     * 去除首尾空格
     *
     * @return this
     */
    public StringExt trim() {
        s = s.trim();
        return this;
    }

    /**
     * 连接字符
     *
     * @param ss 字符
     * @return this
     */
    public StringExt concat(String... ss) {
        StringBuilder sb = new StringBuilder(s);
        for (String s1 : ss) {
            sb.append(s1);
        }
        s = sb.toString();
        return this;
    }

    /**
     * 连接字符
     *
     * @param ss 字符
     * @return this
     */
    public StringExt concatBefore(String... ss) {
        StringBuilder sb = new StringBuilder();
        for (String s1 : ss) {
            sb.append(s1);
        }
        s = sb.append(s).toString();
        return this;
    }

    /**
     * 格式化字符串
     *
     * @param os 参数
     * @return this
     */
    public StringExt format(Object... os) {
        s = Strings.format(s, os);
        return this;
    }

    /**
     * 翻转字符串
     *
     * @return this
     */
    public StringExt reverse() {
        s = new StringBuilder(s).reverse().toString();
        return this;
    }

    /**
     * 截取字符串
     *
     * @param begin 开始下标
     * @param end   结束下标
     * @return this
     */
    public StringExt substring(int begin, int end) {
        s = s.substring(begin, end);
        return this;
    }

    /**
     * 截取字符串
     *
     * @param begin 开始下标
     * @return this
     */
    public StringExt substring(int begin) {
        s = s.substring(begin);
        return this;
    }

    /**
     * 是否包含字符串
     *
     * @param s 字符串
     * @return true包含
     */
    public boolean contains(String s) {
        return this.s.contains(s);
    }

    /**
     * 转为 byte
     *
     * @return byte
     */
    public Byte toByte() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toByte(s);
    }

    public Byte toByte(Byte def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toByte(s);
    }

    /**
     * 转为 byte
     *
     * @return byte
     */
    public byte toByteValue() {
        if (isEmpty()) {
            return 0;
        }
        return Converts.toByte(s);
    }

    public byte toByteValue(byte def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toByte(s);
    }

    /**
     * 转为 short
     *
     * @return s
     */
    public Short toShort() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toShort(s);
    }

    public Short toShort(Short def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toShort(s);
    }

    /**
     * 转为 short
     *
     * @return s
     */
    public short toShortValue() {
        if (isEmpty()) {
            return 0;
        }
        return Converts.toShort(s);
    }

    public short toShortValue(short def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toShort(s);
    }

    /**
     * 转为 Integer
     *
     * @return Integer
     */
    public Integer toInt() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toInt(s);
    }

    public Integer toInt(Integer def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toInt(s);
    }

    /**
     * 转为 int
     *
     * @return int
     */
    public int toIntValue() {
        if (isEmpty()) {
            return 0;
        }
        return Converts.toInt(s);
    }

    public int toIntValue(int def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toInt(s);
    }

    /**
     * 转为 long
     *
     * @return long
     */
    public Long toLong() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toLong(s);
    }

    public Long toLong(Long def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toLong(s);
    }

    /**
     * 转为 long
     *
     * @return long
     */
    public long toLongValue() {
        if (isEmpty()) {
            return 0L;
        }
        return Converts.toLong(s);
    }

    public long toLongValue(long def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toLong(s);
    }

    /**
     * 转为 float
     *
     * @return float
     */
    public Float toFloat() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toFloat(s);
    }

    public Float toFloat(Float def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toFloat(s);
    }

    /**
     * 转为 float
     *
     * @return float
     */
    public float toFloatValue() {
        if (isEmpty()) {
            return 0f;
        }
        return Converts.toFloat(s);
    }

    public float toFloatValue(float def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toFloat(s);
    }

    /**
     * 转为 double
     *
     * @return double
     */
    public Double toDouble() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toDouble(s);
    }

    public Double toDouble(Double def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toDouble(s);
    }

    /**
     * 转为 double
     *
     * @return double
     */
    public double toDoubleValue() {
        if (isEmpty()) {
            return 0.0;
        }
        return Converts.toDouble(s);
    }

    public double toDoubleValue(double def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toDouble(s);
    }

    /**
     * 转为 boolean
     *
     * @return boolean
     */
    public Boolean toBoolean() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toBoolean(s);
    }

    public Boolean toBoolean(Boolean def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toBoolean(s);
    }

    /**
     * 转为 boolean
     *
     * @return boolean
     */
    public boolean toBooleanValue() {
        if (isEmpty()) {
            return false;
        }
        return Converts.toBoolean(s);
    }

    public boolean toBooleanValue(boolean def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toBoolean(s);
    }

    /**
     * 转为 char
     *
     * @return char
     */
    public Character toChar() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toChar(s);
    }

    public Character toChar(Character def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toChar(s);
    }

    /**
     * 转为 char
     *
     * @return char
     */
    public char toCharValue() {
        if (isEmpty()) {
            return 0;
        }
        return Converts.toChar(s);
    }

    public char toCharValue(char def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toChar(s);
    }

    /**
     * 转为 date
     *
     * @return date
     */
    public Date toDate() {
        if (isEmpty()) {
            return null;
        }
        return Converts.toDate(s);
    }

    public Date toDate(Date def) {
        if (isEmpty()) {
            return def;
        }
        return Converts.toDate(s);
    }

    /**
     * 转为 stringBuilder
     *
     * @return stringBuilder
     */
    public StringBuilder toStringBuilder() {
        if (isEmpty()) {
            return new StringBuilder();
        }
        return new StringBuilder(s);
    }

    /**
     * 转为 stringBuffer
     *
     * @return stringBuffer
     */
    public StringBuffer toStringBuffer() {
        if (isEmpty()) {
            return new StringBuffer();
        }
        return new StringBuffer(s);
    }

    /**
     * 转为 byte[]
     *
     * @return byte[]
     */
    public byte[] toBytes() {
        return s.getBytes();
    }

    /**
     * 转为  byte[]
     *
     * @param charset 编码格式
     * @return byte[]
     */
    public byte[] toBytes(String charset) {
        try {
            return s.getBytes(charset);
        } catch (Exception e) {
            throw Exceptions.unEnding(e);
        }
    }

    /**
     * 转为 char[]
     *
     * @return char[]
     */
    public char[] toChars() {
        return s.toCharArray();
    }

    /**
     * 转为string
     *
     * @return string
     */
    @Override
    public String toString() {
        return s;
    }

    /**
     * 转为小写String
     *
     * @return string
     */
    public String toLowerString() {
        return s.toLowerCase();
    }

    /**
     * 转为大写String
     *
     * @return string
     */
    public String toUpperString() {
        return s.toUpperCase();
    }

    /**
     * url编码
     *
     * @return string
     */
    public String urlEncode() {
        return Urls.encode(s);
    }

    /**
     * url编码
     *
     * @param charset 编码格式
     * @return string
     */
    public String urlEncode(String charset) {
        return Urls.encode(s, charset);
    }

    /**
     * url解码
     *
     * @return string
     */
    public String urlDecode() {
        return Urls.decode(s);
    }

    /**
     * url解码
     *
     * @param charset 编码格式
     * @return string
     */
    public String urlDecode(String charset) {
        return Urls.decode(s, charset);
    }

    /**
     * md5签名
     *
     * @return 签名
     */
    public String md5() {
        return Encrypts.md5(s);
    }

    /**
     * 签名
     *
     * @param sign 签名方式
     * @return 签名
     */
    public String sign(String sign) {
        return Encrypts.sign(s, sign);
    }

    /**
     * base64编码
     *
     * @return base64
     */
    public String encodeBase64() {
        return Encrypts.base64Encode(s);
    }

    /**
     * base64转码
     *
     * @return string
     */
    public String decodeBase64() {
        return Encrypts.base64Decode(s);
    }

    /**
     * xss过滤
     *
     * @return string
     */
    public String cleanXSS() {
        return Xsses.clean(s);
    }

    /**
     * xss编码
     *
     * @return string
     */
    public String recodeXSS() {
        return Xsses.recode(s);
    }

}
