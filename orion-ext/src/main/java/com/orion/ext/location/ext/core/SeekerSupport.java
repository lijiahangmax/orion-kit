package com.orion.ext.location.ext.core;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;

import java.io.UnsupportedEncodingException;

/**
 * 查询工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 19:28
 */
public class SeekerSupport {

    private SeekerSupport() {
    }

    /**
     * 从 ip 的字符串形式得到字节数组形式
     *
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            Exceptions.printStacks(e);
        }
        return ret;
    }

    /**
     * 对原始字符串进行编码转换, 如果失败, 返回原始的字符串
     *
     * @param s            原始字符串
     * @param srcEncoding  源编码方式
     * @param destEncoding 目标编码方式
     * @return 转换编码后的字符串, 失败返回原始字符串
     */
    public static String getString(String s, String srcEncoding, String destEncoding) {
        try {
            return new String(Strings.bytes(s, srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * 根据某种编码方式将字节数组转换成字符串
     *
     * @param b        字节数组
     * @param encoding 编码方式
     * @return 如果encoding不支持, 返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }

    /**
     * 根据某种编码方式将字节数组转换成字符串
     *
     * @param b        字节数组
     * @param offset   要转换的起始位置
     * @param len      要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持, 返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }

    /**
     * @param ip ip的字节数组形式
     * @return 字符串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
        return new StringBuilder()
                .append(ip[0] & 0xFF)
                .append('.')
                .append(ip[1] & 0xFF)
                .append('.')
                .append(ip[2] & 0xFF)
                .append('.')
                .append(ip[3] & 0xFF)
                .toString();
    }

}
