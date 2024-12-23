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

import cn.orionsec.kit.lang.define.number.Number128;

import java.util.Arrays;

/**
 * Google CityHash64
 * Google CityHash128
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 16:02
 */
public class CityHash {

    // Some primes between 2^63 and 2^64 for various uses.
    private static final long K_0 = 0xC3A5C85C97CB3127L;
    private static final long K_1 = 0xB492B66FBE98F273L;
    private static final long K_2 = 0x9AE16A3B2F90404FL;
    private static final long K_MUL = 0x9DDFEA08EB382D69L;

    // Magic numbers for 32-bit hashing.  Copied from Murmur3.
    private static final int C_1 = 0xCC9E2D51;
    private static final int C_2 = 0x1B873593;

    private CityHash() {
    }

    /**
     * 计算32位City Hash值
     *
     * @param data 数据
     * @return hash值
     */
    public static int hash32(byte[] data) {
        int len = data.length;
        if (len <= 24) {
            return len <= 12 ? (len <= 4 ? hash32Len0to4(data) : hash32Len5to12(data)) : hash32Len13to24(data);
        }

        // len > 24
        int h = len, g = C_1 * len, f = g;
        int a0 = rotate32(fetch32(data, len - 4) * C_1, 17) * C_2;
        int a1 = rotate32(fetch32(data, len - 8) * C_1, 17) * C_2;
        int a2 = rotate32(fetch32(data, len - 16) * C_1, 17) * C_2;
        int a3 = rotate32(fetch32(data, len - 12) * C_1, 17) * C_2;
        int a4 = rotate32(fetch32(data, len - 20) * C_1, 17) * C_2;
        h ^= a0;
        h = rotate32(h, 19);
        h = h * 5 + 0xE6546B64;
        h ^= a2;
        h = rotate32(h, 19);
        h = h * 5 + 0xE6546B64;
        g ^= a1;
        g = rotate32(g, 19);
        g = g * 5 + 0xE6546B64;
        g ^= a3;
        g = rotate32(g, 19);
        g = g * 5 + 0xE6546B64;
        f += a4;
        f = rotate32(f, 19);
        f = f * 5 + 0xE6546B64;
        int iters = (len - 1) / 20;

        int pos = 0;
        do {
            a0 = rotate32(fetch32(data, pos) * C_1, 17) * C_2;
            a1 = fetch32(data, pos + 4);
            a2 = rotate32(fetch32(data, pos + 8) * C_1, 17) * C_2;
            a3 = rotate32(fetch32(data, pos + 12) * C_1, 17) * C_2;
            a4 = fetch32(data, pos + 16);
            h ^= a0;
            h = rotate32(h, 18);
            h = h * 5 + 0xE6546B64;
            f += a1;
            f = rotate32(f, 19);
            f = f * C_1;
            g += a2;
            g = rotate32(g, 18);
            g = g * 5 + 0xE6546B64;
            h ^= a3 + a1;
            h = rotate32(h, 19);
            h = h * 5 + 0xE6546B64;
            g ^= a4;
            g = Integer.reverseBytes(g) * 5;
            h += a4 * 5;
            h = Integer.reverseBytes(h);
            f += a0;
            int swapValue = f;
            f = g;
            g = h;
            h = swapValue;

            pos += 20;
        } while (--iters != 0);

        g = rotate32(g, 11) * C_1;
        g = rotate32(g, 17) * C_1;
        f = rotate32(f, 11) * C_1;
        f = rotate32(f, 17) * C_1;
        h = rotate32(h + g, 19);
        h = h * 5 + 0xE6546B64;
        h = rotate32(h, 17) * C_1;
        h = rotate32(h + f, 19);
        h = h * 5 + 0xE6546B64;
        h = rotate32(h, 17) * C_1;
        return h;
    }

    /**
     * 计算64位City Hash值
     *
     * @param data 数据
     * @return hash值
     */
    public static long hash64(byte[] data) {
        int len = data.length;
        if (len <= 32) {
            if (len <= 16) {
                return hashLen0to16(data);
            } else {
                return hashLen17to32(data);
            }
        } else if (len <= 64) {
            return hashLen33to64(data);
        }

        // For strings over 64 bytes we hash the end first, and then as we
        // loop we keep 56 bytes of state: v, w, x, y, and z.
        long x = fetch64(data, len - 40);
        long y = fetch64(data, len - 16) + fetch64(data, len - 56);
        long z = hashLen16(fetch64(data, len - 48) + len, fetch64(data, len - 24));
        Number128 v = weakHashLen32WithSeeds(data, len - 64, len, z);
        Number128 w = weakHashLen32WithSeeds(data, len - 32, y + K_1, x);
        x = x * K_1 + fetch64(data, 0);

        // Decrease len to the nearest multiple of 64, and operate on 64-byte chunks.
        len = (len - 1) & ~63;
        int pos = 0;
        do {
            x = rotate(x + y + v.getLowValue() + fetch64(data, pos + 8), 37) * K_1;
            y = rotate(y + v.getHighValue() + fetch64(data, pos + 48), 42) * K_1;
            x ^= w.getHighValue();
            y += v.getLowValue() + fetch64(data, pos + 40);
            z = rotate(z + w.getLowValue(), 33) * K_1;
            v = weakHashLen32WithSeeds(data, pos, v.getHighValue() * K_1, x + w.getLowValue());
            w = weakHashLen32WithSeeds(data, pos + 32, z + w.getHighValue(), y + fetch64(data, pos + 16));
            // swap z,x value
            long swapValue = x;
            x = z;
            z = swapValue;
            pos += 64;
            len -= 64;
        } while (len != 0);
        return hashLen16(hashLen16(v.getLowValue(), w.getLowValue()) + shiftMix(y) * K_1 + z,
                hashLen16(v.getHighValue(), w.getHighValue()) + x);
    }

    /**
     * 计算64位City Hash值
     *
     * @param data  数据
     * @param seed0 种子1
     * @param seed1 种子2
     * @return hash值
     */
    public static long hash64(byte[] data, long seed0, long seed1) {
        return hashLen16(hash64(data) - seed0, seed1);
    }

    /**
     * 计算64位City Hash值, 种子1使用默认的{@link #K_2}
     *
     * @param data 数据
     * @param seed 种子2
     * @return hash值
     */
    public static long hash64(byte[] data, long seed) {
        return hash64(data, K_2, seed);
    }

    /**
     * 计算128位City Hash值
     *
     * @param data 数据
     * @return hash值
     */
    public static Number128 hash128(byte[] data) {
        int len = data.length;
        return len >= 16 ?
                hash128(data, 16, new Number128(fetch64(data, 0), fetch64(data, 8) + K_0)) :
                hash128(data, 0, new Number128(K_0, K_1));
    }

    /**
     * 计算128位City Hash值
     *
     * @param data 数据
     * @param seed 种子
     * @return hash值
     */
    public static Number128 hash128(byte[] data, Number128 seed) {
        return hash128(data, 0, seed);
    }

    private static Number128 hash128(byte[] byteArray, int start, Number128 seed) {
        int len = byteArray.length - start;

        if (len < 128) {
            return cityMurmur(Arrays.copyOfRange(byteArray, start, byteArray.length), seed);
        }

        // We expect len >= 128 to be the common case.  Keep 56 bytes of state:
        // v, w, x, y, and z.
        Number128 v = new Number128(0L, 0L);
        Number128 w = new Number128(0L, 0L);
        long x = seed.getLowValue();
        long y = seed.getHighValue();
        long z = len * K_1;
        v.setLowValue(rotate(y ^ K_1, 49) * K_1 + fetch64(byteArray, start));
        v.setHighValue(rotate(v.getLowValue(), 42) * K_1 + fetch64(byteArray, start + 8));
        w.setLowValue(rotate(y + z, 35) * K_1 + x);
        w.setHighValue(rotate(x + fetch64(byteArray, start + 88), 53) * K_1);

        // This is the same inner loop as CityHash64(), manually unrolled.
        int pos = start;
        do {
            x = rotate(x + y + v.getLowValue() + fetch64(byteArray, pos + 8), 37) * K_1;
            y = rotate(y + v.getHighValue() + fetch64(byteArray, pos + 48), 42) * K_1;
            x ^= w.getHighValue();
            y += v.getLowValue() + fetch64(byteArray, pos + 40);
            z = rotate(z + w.getLowValue(), 33) * K_1;
            v = weakHashLen32WithSeeds(byteArray, pos, v.getHighValue() * K_1, x + w.getLowValue());
            w = weakHashLen32WithSeeds(byteArray, pos + 32, z + w.getHighValue(), y + fetch64(byteArray, pos + 16));

            long swapValue = x;
            x = z;
            z = swapValue;
            pos += 64;
            x = rotate(x + y + v.getLowValue() + fetch64(byteArray, pos + 8), 37) * K_1;
            y = rotate(y + v.getHighValue() + fetch64(byteArray, pos + 48), 42) * K_1;
            x ^= w.getHighValue();
            y += v.getLowValue() + fetch64(byteArray, pos + 40);
            z = rotate(z + w.getLowValue(), 33) * K_1;
            v = weakHashLen32WithSeeds(byteArray, pos, v.getHighValue() * K_1, x + w.getLowValue());
            w = weakHashLen32WithSeeds(byteArray, pos + 32, z + w.getHighValue(), y + fetch64(byteArray, pos + 16));
            swapValue = x;
            x = z;
            z = swapValue;
            pos += 64;
            len -= 128;
        } while (len >= 128);
        x += rotate(v.getLowValue() + z, 49) * K_0;
        y = y * K_0 + rotate(w.getHighValue(), 37);
        z = z * K_0 + rotate(w.getLowValue(), 27);
        w.setLowValue(w.getLowValue() * 9);
        v.setLowValue(v.getLowValue() * K_0);

        // If 0 < len < 128, hash up to 4 chunks of 32 bytes each from the end of s.
        for (int tailDone = 0; tailDone < len; ) {
            tailDone += 32;
            y = rotate(x + y, 42) * K_0 + v.getHighValue();
            w.setLowValue(w.getLowValue() + fetch64(byteArray, pos + len - tailDone + 16));
            x = x * K_0 + w.getLowValue();
            z += w.getHighValue() + fetch64(byteArray, pos + len - tailDone);
            w.setHighValue(w.getHighValue() + v.getLowValue());
            v = weakHashLen32WithSeeds(byteArray, pos + len - tailDone, v.getLowValue() + z, v.getHighValue());
            v.setLowValue(v.getLowValue() * K_0);
        }
        // At this point our 56 bytes of state should contain more than
        // enough information for a strong 128-bit hash.  We use two
        // different 56-byte-to-8-byte hashes to get a 16-byte final result.
        x = hashLen16(x, v.getLowValue());
        y = hashLen16(y + z, w.getLowValue());
        return new Number128(hashLen16(x + v.getHighValue(), w.getHighValue()) + y,
                hashLen16(x + w.getHighValue(), y + v.getHighValue()));

    }

    private static int hash32Len0to4(byte[] byteArray) {
        int b = 0;
        int c = 9;
        int len = byteArray.length;
        for (int v : byteArray) {
            b = b * C_1 + v;
            c ^= b;
        }
        return fmix(mur(b, mur(len, c)));
    }

    private static int hash32Len5to12(byte[] byteArray) {
        int len = byteArray.length;
        int a = len, b = len * 5, c = 9, d = b;
        a += fetch32(byteArray, 0);
        b += fetch32(byteArray, len - 4);
        c += fetch32(byteArray, ((len >>> 1) & 4));
        return fmix(mur(c, mur(b, mur(a, d))));
    }

    private static int hash32Len13to24(byte[] byteArray) {
        int len = byteArray.length;
        int a = fetch32(byteArray, (len >>> 1) - 4);
        int b = fetch32(byteArray, 4);
        int c = fetch32(byteArray, len - 8);
        int d = fetch32(byteArray, (len >>> 1));
        int e = fetch32(byteArray, 0);
        int f = fetch32(byteArray, len - 4);
        @SuppressWarnings("UnnecessaryLocalVariable")
        int h = len;

        return fmix(mur(f, mur(e, mur(d, mur(c, mur(b, mur(a, h)))))));
    }

    private static long hashLen0to16(byte[] byteArray) {
        int len = byteArray.length;
        if (len >= 8) {
            long mul = K_2 + len * 2;
            long a = fetch64(byteArray, 0) + K_2;
            long b = fetch64(byteArray, len - 8);
            long c = rotate(b, 37) * mul + a;
            long d = (rotate(a, 25) + b) * mul;
            return hashLen16(c, d, mul);
        }
        if (len >= 4) {
            long mul = K_2 + len * 2;
            long a = fetch32(byteArray, 0) & 0xFFFFFFFFL;
            return hashLen16(len + (a << 3), fetch32(byteArray, len - 4) & 0xFFFFFFFFL, mul);
        }
        if (len > 0) {
            int a = byteArray[0] & 0xFF;
            int b = byteArray[len >>> 1] & 0xFF;
            int c = byteArray[len - 1] & 0xFF;
            int y = a + (b << 8);
            int z = len + (c << 2);
            return shiftMix(y * K_2 ^ z * K_0) * K_2;
        }
        return K_2;
    }

    private static long hashLen17to32(byte[] byteArray) {
        int len = byteArray.length;
        long mul = K_2 + len * 2;
        long a = fetch64(byteArray, 0) * K_1;
        long b = fetch64(byteArray, 8);
        long c = fetch64(byteArray, len - 8) * mul;
        long d = fetch64(byteArray, len - 16) * K_2;
        return hashLen16(rotate(a + b, 43) + rotate(c, 30) + d,
                a + rotate(b + K_2, 18) + c, mul);
    }

    private static long hashLen33to64(byte[] byteArray) {
        int len = byteArray.length;
        long mul = K_2 + len * 2;
        long a = fetch64(byteArray, 0) * K_2;
        long b = fetch64(byteArray, 8);
        long c = fetch64(byteArray, len - 24);
        long d = fetch64(byteArray, len - 32);
        long e = fetch64(byteArray, 16) * K_2;
        long f = fetch64(byteArray, 24) * 9;
        long g = fetch64(byteArray, len - 8);
        long h = fetch64(byteArray, len - 16) * mul;
        long u = rotate(a + g, 43) + (rotate(b, 30) + c) * 9;
        long v = ((a + g) ^ d) + f + 1;
        long w = Long.reverseBytes((u + v) * mul) + h;
        long x = rotate(e + f, 42) + c;
        long y = (Long.reverseBytes((v + w) * mul) + g) * mul;
        long z = e + f + c;
        a = Long.reverseBytes((x + z) * mul + y) + b;
        b = shiftMix((z + a) * mul + d + h) * mul;
        return b + x;
    }

    private static long loadUnaligned64(byte[] byteArray, int start) {
        long result = 0;
        OrderIter orderIter = new OrderIter(8);
        while (orderIter.hasNext()) {
            int next = orderIter.next();
            long value = (byteArray[next + start] & 0xFFL) << (next * 8);
            result |= value;
        }
        return result;
    }

    private static int loadUnaligned32(byte[] byteArray, int start) {
        int result = 0;
        OrderIter orderIter = new OrderIter(4);
        while (orderIter.hasNext()) {
            int next = orderIter.next();
            int value = (byteArray[next + start] & 0xFF) << (next * 8);
            result |= value;
        }
        return result;
    }

    private static long fetch64(byte[] byteArray, int start) {
        return loadUnaligned64(byteArray, start);
    }

    private static int fetch32(byte[] byteArray, int start) {
        return loadUnaligned32(byteArray, start);
    }

    private static long rotate(long val, int shift) {
        // Avoid shifting by 64: doing so yields an undefined result.
        return shift == 0 ? val : ((val >>> shift) | (val << (64 - shift)));
    }

    private static int rotate32(int val, int shift) {
        // Avoid shifting by 32: doing so yields an undefined result.
        return shift == 0 ? val : ((val >>> shift) | (val << (32 - shift)));
    }

    private static long hashLen16(long u, long v, long mul) {
        // Murmur-inspired hashing.
        long a = (u ^ v) * mul;
        a ^= (a >>> 47);
        long b = (v ^ a) * mul;
        b ^= (b >>> 47);
        b *= mul;
        return b;
    }

    private static long hashLen16(long u, long v) {
        return hash128to64(new Number128(u, v));
    }

    private static long hash128to64(Number128 number128) {
        // Murmur-inspired hashing.
        long a = (number128.getLowValue() ^ number128.getHighValue()) * K_MUL;
        a ^= (a >>> 47);
        long b = (number128.getHighValue() ^ a) * K_MUL;
        b ^= (b >>> 47);
        b *= K_MUL;
        return b;
    }

    private static long shiftMix(long val) {
        return val ^ (val >>> 47);
    }

    private static int fmix(int h) {
        h ^= h >>> 16;
        h *= 0x85EBCA6B;
        h ^= h >>> 13;
        h *= 0xC2B2AE35;
        h ^= h >>> 16;
        return h;
    }

    private static int mur(int a, int h) {
        // Helper from Murmur3 for combining two 32-bit values.
        a *= C_1;
        a = rotate32(a, 17);
        a *= C_2;
        h ^= a;
        h = rotate32(h, 19);
        return h * 5 + 0xE6546B64;
    }

    private static Number128 weakHashLen32WithSeeds(long w, long x, long y, long z, long a, long b) {
        a += w;
        b = rotate(b + a + z, 21);
        long c = a;
        a += x;
        a += y;
        b += rotate(a, 44);
        return new Number128(a + z, b + c);
    }

    // Return a 16-byte hash for s[0] ... s[31], a, and b.  Quick and dirty.
    private static Number128 weakHashLen32WithSeeds(byte[] byteArray, int start, long a, long b) {
        return weakHashLen32WithSeeds(fetch64(byteArray, start),
                fetch64(byteArray, start + 8),
                fetch64(byteArray, start + 16),
                fetch64(byteArray, start + 24),
                a, b);
    }

    private static Number128 cityMurmur(final byte[] byteArray, Number128 seed) {
        int len = byteArray.length;
        long a = seed.getLowValue();
        long b = seed.getHighValue();
        long c;
        long d;
        int l = len - 16;
        if (l <= 0) {  // len <= 16
            a = shiftMix(a * K_1) * K_1;
            c = b * K_1 + hashLen0to16(byteArray);
            d = shiftMix(a + (len >= 8 ? fetch64(byteArray, 0) : c));
        } else {  // len > 16
            c = hashLen16(fetch64(byteArray, len - 8) + K_1, a);
            d = hashLen16(b + len, c + fetch64(byteArray, len - 16));
            a += d;
            int pos = 0;
            do {
                a ^= shiftMix(fetch64(byteArray, pos) * K_1) * K_1;
                a *= K_1;
                b ^= a;
                c ^= shiftMix(fetch64(byteArray, pos + 8) * K_1) * K_1;
                c *= K_1;
                d ^= c;
                pos += 16;
                l -= 16;
            } while (l > 0);
        }
        a = hashLen16(a, c);
        b = hashLen16(d, b);
        return new Number128(a ^ b, hashLen16(b, a));
    }

    private static class OrderIter {

        private static final boolean IS_LITTLE_ENDIAN = "little".equals(System.getProperty("sun.cpu.endian"));

        private final int size;
        private int index;

        OrderIter(int size) {
            this.size = size;
        }

        boolean hasNext() {
            return index < size;
        }

        int next() {
            return IS_LITTLE_ENDIAN ? index++ : (size - 1 - index++);
        }

    }

}
