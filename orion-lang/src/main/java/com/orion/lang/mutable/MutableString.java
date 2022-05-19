package com.orion.lang.mutable;

import com.orion.able.Mutable;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import com.orion.utils.Xsses;
import com.orion.utils.codec.Base64s;
import com.orion.utils.convert.Converts;
import com.orion.utils.crypto.Signatures;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 可变 String
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/13 14:11
 */
public class MutableString implements Mutable<String>, CharSequence, Serializable {

    private static final long serialVersionUID = 8675244107435484L;

    private StringBuilder builder;

    public MutableString() {
        this.builder = new StringBuilder();
    }

    public MutableString(String s) {
        Valid.notNull(s);
        this.builder = new StringBuilder(s);
    }

    public MutableString(StringBuilder builder) {
        Valid.notNull(builder);
        this.builder = builder;
    }

    public MutableString(Object o) {
        Valid.notNull(o);
        this.builder = new StringBuilder(Strings.str(o));
    }

    public MutableString of(String s) {
        return new MutableString(s);
    }

    @Override
    public String get() {
        return builder.toString();
    }

    @Override
    public void set(String s) {
        Valid.notNull(s);
        this.builder = new StringBuilder(s);
    }

    /**
     * 是否为空白
     *
     * @return true 空白
     */
    public boolean isBlank() {
        return Strings.isBlank(builder.toString());
    }

    /**
     * 是否不为空白
     *
     * @return false 空白
     */
    public boolean isNotBlank() {
        return Strings.isNotBlank(builder.toString());
    }

    /**
     * 是否为空
     *
     * @return true 空
     */
    public boolean isEmpty() {
        return Strings.isEmpty(builder);
    }

    /**
     * 是否不为空
     *
     * @return false 空
     */
    public boolean isNotEmpty() {
        return Strings.isNotEmpty(builder);
    }

    /**
     * 去除首尾空格
     *
     * @return this
     */
    public MutableString trim() {
        this.builder = new StringBuilder(builder.toString().trim());
        return this;
    }

    /**
     * 去除特殊符号
     *
     * @return this
     */
    public MutableString trimPunct() {
        this.builder = new StringBuilder(Strings.trimPunct(builder.toString()));
        return this;
    }

    /**
     * 连接字符
     *
     * @param ss 字符
     * @return this
     */
    public MutableString concat(String... ss) {
        for (String s : ss) {
            builder.append(s);
        }
        return this;
    }

    /**
     * 连接字符
     *
     * @param ss 字符
     * @return this
     */
    public MutableString concatBefore(String... ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(s);
        }
        this.builder = sb.append(builder);
        return this;
    }

    /**
     * 格式化字符串
     *
     * @param os 参数
     * @return this
     */
    public MutableString format(Object... os) {
        this.builder = new StringBuilder(Strings.format(builder.toString(), os));
        return this;
    }

    /**
     * 翻转字符串
     *
     * @return this
     */
    public MutableString reverse() {
        builder.reverse();
        return this;
    }

    /**
     * 截取字符串
     *
     * @param begin 开始下标
     * @param end   结束下标
     * @return this
     */
    public MutableString substring(int begin, int end) {
        this.builder = new StringBuilder(builder.substring(begin, end));
        return this;
    }

    /**
     * 截取字符串
     *
     * @param begin 开始下标
     * @return this
     */
    public MutableString substring(int begin) {
        this.builder = new StringBuilder(builder.substring(begin));
        return this;
    }

    /**
     * 是否包含字符串
     *
     * @param s 字符串
     * @return true包含
     */
    public boolean contains(String s) {
        return builder.indexOf(s) > -1;
    }

    /**
     * 转为 byte
     *
     * @return byte
     */
    public Byte toByte() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toByte(builder.toString());
    }

    /**
     * 转为 short
     *
     * @return s
     */
    public Short toShort() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toShort(builder.toString());
    }

    /**
     * 转为 integer
     *
     * @return Integer
     */
    public Integer toInteger() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toInt(builder.toString());
    }

    /**
     * 转为 long
     *
     * @return long
     */
    public Long toLong() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toLong(builder.toString());
    }

    /**
     * 转为 float
     *
     * @return float
     */
    public Float toFloat() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toFloat(builder.toString());
    }

    /**
     * 转为 double
     *
     * @return double
     */
    public Double toDouble() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toDouble(builder.toString());
    }

    /**
     * 转为 boolean
     *
     * @return boolean
     */
    public Boolean toBoolean() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toBoolean(builder.toString());
    }

    /**
     * 转为 char
     *
     * @return char
     */
    public Character toCharacter() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toChar(builder.toString());
    }

    /**
     * 转为 date
     *
     * @return date
     */
    public Date toDate() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toDate(builder.toString());
    }

    /**
     * 转为 localDateTime
     *
     * @return LocalDateTime
     */
    public LocalDateTime toLocalDateTime() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toLocalDateTime(builder.toString());
    }

    /**
     * 转为 localDate
     *
     * @return date
     */
    public LocalDate toLocalDate() {
        if (this.isEmpty()) {
            return null;
        }
        return Converts.toLocalDate(builder.toString());
    }

    /**
     * 转为 stringBuilder
     *
     * @return stringBuilder
     */
    public StringBuilder toStringBuilder() {
        return new StringBuilder(builder);
    }

    /**
     * 转为 stringBuffer
     *
     * @return stringBuffer
     */
    public StringBuffer toStringBuffer() {
        return new StringBuffer(builder);
    }

    /**
     * 转为 byte[]
     *
     * @return byte[]
     */
    public byte[] toBytes() {
        return Strings.bytes(builder.toString());
    }

    /**
     * 转为  byte[]
     *
     * @param charset 编码格式
     * @return byte[]
     */
    public byte[] toBytes(String charset) {
        return Strings.bytes(builder.toString(), charset);
    }

    /**
     * 转为 char[]
     *
     * @return char[]
     */
    public char[] toChars() {
        return builder.toString().toCharArray();
    }

    /**
     * 转为小写String
     *
     * @return string
     */
    public String toLowerString() {
        return builder.toString().toLowerCase();
    }

    /**
     * 转为大写String
     *
     * @return string
     */
    public String toUpperString() {
        return builder.toString().toUpperCase();
    }

    /**
     * url编码
     *
     * @return string
     */
    public String urlEncode() {
        return Urls.encode(builder.toString());
    }

    /**
     * url编码
     *
     * @param charset 编码格式
     * @return string
     */
    public String urlEncode(String charset) {
        return Urls.encode(builder.toString(), charset);
    }

    /**
     * url解码
     *
     * @return string
     */
    public String urlDecode() {
        return Urls.decode(builder.toString());
    }

    /**
     * url解码
     *
     * @param charset 编码格式
     * @return string
     */
    public String urlDecode(String charset) {
        return Urls.decode(builder.toString(), charset);
    }

    /**
     * md5签名
     *
     * @return 签名
     */
    public String md5() {
        return Signatures.md5(builder.toString());
    }

    /**
     * 签名
     *
     * @param sign 签名方式
     * @return 签名
     */
    public String sign(String sign) {
        return Signatures.sign(builder.toString(), sign);
    }

    /**
     * base64编码
     *
     * @return base64
     */
    public String encodeBase64() {
        return Base64s.encode(builder.toString());
    }

    /**
     * base64转码
     *
     * @return string
     */
    public String decodeBase64() {
        return Base64s.decode(builder.toString());
    }

    /**
     * xss过滤
     *
     * @return string
     */
    public String cleanXss() {
        return Xsses.clean(builder.toString());
    }

    /**
     * xss编码
     *
     * @return string
     */
    public String recodeXss() {
        return Xsses.recode(builder.toString());
    }

    @Override
    public int length() {
        return builder.length();
    }

    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableString that = (MutableString) o;
        return Objects.equals(builder, that.builder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builder);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

}
