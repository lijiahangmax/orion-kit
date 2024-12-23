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
package cn.orionsec.kit.lang.utils.hash;

import cn.orionsec.kit.lang.utils.Strings;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Murmur3 Hash 算法
 * <p>
 * Murmur3 32bit
 * Murmur3 64bit
 * Murmur3 128bit
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 16:05
 */
public class MurmurHash {

    private static final int C1_32 = 0xCC9E2D51;
    private static final int C2_32 = 0x1B873593;
    private static final int R1_32 = 15;
    private static final int R2_32 = 13;
    private static final int M_32 = 5;
    private static final int N_32 = 0xE6546B64;

    private static final long C1 = 0x87C37B91114253D5L;
    private static final long C2 = 0x4CF5AD432745937FL;
    private static final int R1 = 31;
    private static final int R2 = 27;
    private static final int R3 = 33;
    private static final int M = 5;
    private static final int N1 = 0x52DCE729;
    private static final int N2 = 0x38495AB5;

    private static final int DEFAULT_SEED = 0;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private MurmurHash() {
    }

    /**
     * Murmur3 32-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值
     */
    public static int hash32(String data) {
        return hash32(Strings.bytes(data, DEFAULT_CHARSET));
    }

    /**
     * Murmur3 32-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值
     */
    public static int hash32(byte[] data) {
        return hash32(data, data.length, DEFAULT_SEED);
    }

    /**
     * Murmur3 32-bit Hash值计算
     *
     * @param data   数据
     * @param length 长度
     * @param seed   种子, 默认0
     * @return Hash值
     */
    public static int hash32(byte[] data, int length, int seed) {
        int hash = seed;
        int nblocks = length >> 2;

        // body
        for (int i = 0; i < nblocks; i++) {
            int i4 = i << 2;
            int k = (data[i4] & 0xFF)
                    | ((data[i4 + 1] & 0xFF) << 8)
                    | ((data[i4 + 2] & 0xFF) << 16)
                    | ((data[i4 + 3] & 0xFF) << 24);

            // mix functions
            k *= C1_32;
            k = Integer.rotateLeft(k, R1_32);
            k *= C2_32;
            hash ^= k;
            hash = Integer.rotateLeft(hash, R2_32) * M_32 + N_32;
        }

        // tail
        int idx = nblocks << 2;
        int k1 = 0;
        switch (length - idx) {
            case 3:
                k1 ^= data[idx + 2] << 16;
            case 2:
                k1 ^= data[idx + 1] << 8;
            case 1:
                k1 ^= data[idx];
                // mix functions
                k1 *= C1_32;
                k1 = Integer.rotateLeft(k1, R1_32);
                k1 *= C2_32;
                hash ^= k1;
        }

        // finalization
        hash ^= length;
        hash ^= (hash >>> 16);
        hash *= 0x85EBCA6B;
        hash ^= (hash >>> 13);
        hash *= 0xC2B2AE35;
        hash ^= (hash >>> 16);

        return hash;
    }

    /**
     * Murmur3 64-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值
     */
    public static long hash64(String data) {
        return hash64(Strings.bytes(data, DEFAULT_CHARSET));
    }

    /**
     * Murmur3 64-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值
     */
    public static long hash64(byte[] data) {
        return hash64(data, data.length, DEFAULT_SEED);
    }

    /**
     * Murmur3 64-bit Hash值计算
     *
     * @param data   数据
     * @param length 长度
     * @param seed   种子, 默认0
     * @return Hash值
     */
    public static long hash64(byte[] data, int length, int seed) {
        long hash = seed;
        int nblocks = length >> 3;

        // body
        for (int i = 0; i < nblocks; i++) {
            final int i8 = i << 3;
            long k = ((long) data[i8] & 0xFF)
                    | (((long) data[i8 + 1] & 0xFF) << 8)
                    | (((long) data[i8 + 2] & 0xFF) << 16)
                    | (((long) data[i8 + 3] & 0xFF) << 24)
                    | (((long) data[i8 + 4] & 0xFF) << 32)
                    | (((long) data[i8 + 5] & 0xFF) << 40)
                    | (((long) data[i8 + 6] & 0xFF) << 48)
                    | (((long) data[i8 + 7] & 0xFF) << 56);

            // mix functions
            k *= C1;
            k = Long.rotateLeft(k, R1);
            k *= C2;
            hash ^= k;
            hash = Long.rotateLeft(hash, R2) * M + N1;
        }

        // tail
        long k1 = 0;
        int tailStart = nblocks << 3;
        switch (length - tailStart) {
            case 7:
                k1 ^= ((long) data[tailStart + 6] & 0xFF) << 48;
            case 6:
                k1 ^= ((long) data[tailStart + 5] & 0xFF) << 40;
            case 5:
                k1 ^= ((long) data[tailStart + 4] & 0xFF) << 32;
            case 4:
                k1 ^= ((long) data[tailStart + 3] & 0xFF) << 24;
            case 3:
                k1 ^= ((long) data[tailStart + 2] & 0xFF) << 16;
            case 2:
                k1 ^= ((long) data[tailStart + 1] & 0xFF) << 8;
            case 1:
                k1 ^= ((long) data[tailStart] & 0xFF);
                k1 *= C1;
                k1 = Long.rotateLeft(k1, R1);
                k1 *= C2;
                hash ^= k1;
        }

        // finalization
        hash ^= length;
        hash = fmix64(hash);

        return hash;
    }

    /**
     * Murmur3 128-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值 (2 longs)
     */
    public static long[] hash128(String data) {
        return hash128(Strings.bytes(data, DEFAULT_CHARSET));
    }

    /**
     * Murmur3 128-bit Hash值计算
     *
     * @param data 数据
     * @return Hash值 (2 longs)
     */
    public static long[] hash128(byte[] data) {
        return hash128(data, data.length, DEFAULT_SEED);
    }

    /**
     * Murmur3 128-bit Hash值计算
     *
     * @param data   数据
     * @param length 长度
     * @param seed   种子, 默认0
     * @return Hash值(2 longs)
     */
    public static long[] hash128(byte[] data, int length, int seed) {
        long h1 = seed;
        long h2 = seed;
        final int nblocks = length >> 4;

        // body
        for (int i = 0; i < nblocks; i++) {
            final int i16 = i << 4;
            long k1 = ((long) data[i16] & 0xFF)
                    | (((long) data[i16 + 1] & 0xFF) << 8)
                    | (((long) data[i16 + 2] & 0xFF) << 16)
                    | (((long) data[i16 + 3] & 0xFF) << 24)
                    | (((long) data[i16 + 4] & 0xFF) << 32)
                    | (((long) data[i16 + 5] & 0xFF) << 40)
                    | (((long) data[i16 + 6] & 0xFF) << 48)
                    | (((long) data[i16 + 7] & 0xFF) << 56);

            long k2 = ((long) data[i16 + 8] & 0xFF)
                    | (((long) data[i16 + 9] & 0xFF) << 8)
                    | (((long) data[i16 + 10] & 0xFF) << 16)
                    | (((long) data[i16 + 11] & 0xFF) << 24)
                    | (((long) data[i16 + 12] & 0xFF) << 32)
                    | (((long) data[i16 + 13] & 0xFF) << 40)
                    | (((long) data[i16 + 14] & 0xFF) << 48)
                    | (((long) data[i16 + 15] & 0xFF) << 56);

            // mix functions for k1
            k1 *= C1;
            k1 = Long.rotateLeft(k1, R1);
            k1 *= C2;
            h1 ^= k1;
            h1 = Long.rotateLeft(h1, R2);
            h1 += h2;
            h1 = h1 * M + N1;

            // mix functions for k2
            k2 *= C2;
            k2 = Long.rotateLeft(k2, R3);
            k2 *= C1;
            h2 ^= k2;
            h2 = Long.rotateLeft(h2, R1);
            h2 += h1;
            h2 = h2 * M + N2;
        }

        // tail
        long k1 = 0;
        long k2 = 0;
        int tailStart = nblocks << 4;
        switch (length - tailStart) {
            case 15:
                k2 ^= (long) (data[tailStart + 14] & 0xFF) << 48;
            case 14:
                k2 ^= (long) (data[tailStart + 13] & 0xFF) << 40;
            case 13:
                k2 ^= (long) (data[tailStart + 12] & 0xFF) << 32;
            case 12:
                k2 ^= (long) (data[tailStart + 11] & 0xFF) << 24;
            case 11:
                k2 ^= (long) (data[tailStart + 10] & 0xFF) << 16;
            case 10:
                k2 ^= (long) (data[tailStart + 9] & 0xFF) << 8;
            case 9:
                k2 ^= data[tailStart + 8] & 0xFF;
                k2 *= C2;
                k2 = Long.rotateLeft(k2, R3);
                k2 *= C1;
                h2 ^= k2;
            case 8:
                k1 ^= (long) (data[tailStart + 7] & 0xFF) << 56;
            case 7:
                k1 ^= (long) (data[tailStart + 6] & 0xFF) << 48;
            case 6:
                k1 ^= (long) (data[tailStart + 5] & 0xFF) << 40;
            case 5:
                k1 ^= (long) (data[tailStart + 4] & 0xFF) << 32;
            case 4:
                k1 ^= (long) (data[tailStart + 3] & 0xFF) << 24;
            case 3:
                k1 ^= (long) (data[tailStart + 2] & 0xFF) << 16;
            case 2:
                k1 ^= (long) (data[tailStart + 1] & 0xFF) << 8;
            case 1:
                k1 ^= data[tailStart] & 0xFF;
                k1 *= C1;
                k1 = Long.rotateLeft(k1, R1);
                k1 *= C2;
                h1 ^= k1;
        }

        // finalization
        h1 ^= length;
        h2 ^= length;

        h1 += h2;
        h2 += h1;

        h1 = fmix64(h1);
        h2 = fmix64(h2);

        h1 += h2;
        h2 += h1;
        return new long[]{h1, h2};
    }

    private static long fmix64(long h) {
        h ^= (h >>> 33);
        h *= 0xFF51AFD7ED558CCDL;
        h ^= (h >>> 33);
        h *= 0xC4CEB9FE1A85EC53L;
        h ^= (h >>> 33);
        return h;
    }

}
