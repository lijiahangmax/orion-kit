/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.ext.location.region.core;

/**
 * @author Jiahang Li
 */
public class RegionSupport {

    /**
     * write specfield bytes to a byte array start from offset
     */
    public static void write(byte[] b, int offset, long v, int bytes) {
        for (int i = 0; i < bytes; i++) {
            b[offset++] = (byte) ((v >>> (8 * i)) & 0xFF);
        }
    }

    /**
     * write a int to a byte array
     */
    public static void writeIntLong(byte[] b, int offset, long v) {
        b[offset++] = (byte) ((v) & 0xFF);
        b[offset++] = (byte) ((v >> 8) & 0xFF);
        b[offset++] = (byte) ((v >> 16) & 0xFF);
        b[offset] = (byte) ((v >> 24) & 0xFF);
    }

    /**
     * get a int from a byte array start from the specifiled offset
     */
    public static long getIntLong(byte[] b, int offset) {
        return (
                ((b[offset++] & 0x000000FFL)) |
                        ((b[offset++] << 8) & 0x0000FF00L) |
                        ((b[offset++] << 16) & 0x00FF0000L) |
                        ((b[offset] << 24) & 0xFF000000L)
        );
    }

    /**
     * get a int from a byte array start from the specifield offset
     */
    public static int getInt3(byte[] b, int offset) {
        return (
                (b[offset++] & 0x000000FF) |
                        (b[offset++] & 0x0000FF00) |
                        (b[offset] & 0x00FF0000)
        );
    }

    public static int getInt2(byte[] b, int offset) {
        return (
                (b[offset++] & 0x000000FF) |
                        (b[offset] & 0x0000FF00)
        );
    }

    public static int getInt1(byte[] b, int offset) {
        return (
                (b[offset] & 0x000000FF)
        );
    }

    /**
     * string ip to long ip
     */
    public static long ip2long(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0;
        }
        int p1 = ((Integer.parseInt(p[0]) << 24) & 0xFF000000);
        int p2 = ((Integer.parseInt(p[1]) << 16) & 0x00FF0000);
        int p3 = ((Integer.parseInt(p[2]) << 8) & 0x0000FF00);
        int p4 = ((Integer.parseInt(p[3])) & 0x000000FF);
        return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
    }

    /**
     * int to ip string
     */
    public static String long2ip(long ip) {
        return new StringBuilder()
                .append((ip >> 24) & 0xFF).append('.')
                .append((ip >> 16) & 0xFF).append('.')
                .append((ip >> 8) & 0xFF).append('.')
                .append((ip) & 0xFF).toString();
    }

}