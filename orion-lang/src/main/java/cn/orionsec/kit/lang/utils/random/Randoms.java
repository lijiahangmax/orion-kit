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
package cn.orionsec.kit.lang.utils.random;

import cn.orionsec.kit.lang.utils.Strings;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/10/9 10:16
 */
public class Randoms {

    public static final Random RANDOM = new Random();

    private static final String ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String LETTER = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUMBER = "0123456789";

    private Randoms() {
    }

    public static Random getRandom() {
        return RANDOM;
    }

    public static Random getRandom(boolean threadLocal) {
        return threadLocal ? ThreadLocalRandom.current() : RANDOM;
    }

    /**
     * 随机一个字符
     *
     * @return ignore
     */
    public static char randomChineseChar() {
        return Strings.randomChar().charAt(0);
    }

    /**
     * 随机一个字符串
     *
     * @return ignore
     */
    public static String randomChineseString() {
        return Strings.randomChar();
    }

    /**
     * 随机一个字符串
     *
     * @param count 长度
     * @return ignore
     */
    public static String randomChineseString(int count) {
        return Strings.randomChars(count);
    }

    /**
     * 返回一个定长的随机字母+数字
     *
     * @param length 长度
     * @return ignore
     */
    public static String randomAscii(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL.charAt(random.nextInt(ALL.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串
     *
     * @param length 长度
     * @return ignore
     */
    public static String randomLetter(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTER.charAt(random.nextInt(LETTER.length())));
        }
        return sb.toString();
    }

    /**
     * 随机一个数字
     *
     * @return ignore
     */
    public static int randomInt() {
        return randomInt(0, Integer.MAX_VALUE);
    }

    /**
     * 随机一个数字
     *
     * @param max 最大值 不包含
     * @return ignore
     */
    public static int randomInt(int max) {
        return randomInt(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值 包含
     * @param max 最大值 不包含
     * @return ignore
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    /**
     * 随机一个数字
     *
     * @return ignore
     */
    public static double randomDouble() {
        return randomDouble(0, Double.MAX_VALUE);
    }

    /**
     * 随机一个数字
     *
     * @param max 最大值 不包含
     * @return ignore
     */
    public static double randomDouble(double max) {
        return randomDouble(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值 包含
     * @param max 最大值 不包含
     * @return ignore
     */
    public static double randomDouble(double min, double max) {
        return RANDOM.nextDouble() * (max - min) + min;
    }

    /**
     * 随机一个数字
     *
     * @return ignore
     */
    public static long randomLong() {
        return randomLong(0, Long.MAX_VALUE);
    }

    /**
     * 随机一个数字
     *
     * @param max 最大值 不包含
     * @return ignore
     */
    public static long randomLong(long max) {
        return randomLong(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值 包含
     * @param max 最大值 不包含
     * @return ignore
     */
    public static long randomLong(long min, long max) {
        return (long) (RANDOM.nextDouble() * (max - min)) + min;
    }

    /**
     * 返回一个随机的布尔值
     *
     * @return boolean
     */
    public static boolean randomBoolean() {
        return RANDOM.nextDouble() < 0.5;
    }

    /**
     * 返回一个随机的布尔值
     *
     * @param ratio 几率 1/ratio
     * @return boolean
     */
    public static boolean randomBoolean(int ratio) {
        return RANDOM.nextInt(ratio) == ratio - 1;
    }

    /**
     * 返回一个随机的布尔值 ratio%
     *
     * @param ratio ratio 0 ~ 1
     * @return boolean
     */
    public static boolean randomBoolean(double ratio) {
        return RANDOM.nextDouble() < ratio;
    }

    /**
     * 返回固定长度的数字
     *
     * @param length 长度
     * @return ignore
     */
    public static String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        }
        return sb.toString();
    }

    /**
     * 根据一个数组随机产生对象, 每个对象只会被取出一次, 当数组耗尽, 则一直返回 null
     *
     * @param array 数组
     * @param <E>   ignore
     * @return 随即元素
     */
    public static <E> ArrayRandom<E> randomArray(E[] array) {
        return new ArrayRandom<>(array);
    }

    /**
     * 根据一个集合随机产生对象, 每个对象只会被取出一次, 当数组耗尽, 则一直返回 null
     *
     * @param list 集合
     * @param <E>  ignore
     * @return 随即元素
     */
    public static <E> ListRandom<E> randomList(List<E> list) {
        return new ListRandom<>(list);
    }

    public static class ArrayRandom<E> {

        private final E[] array;
        private Integer len;
        private final Random r = new Random();
        private final Object lock = new Object();

        private ArrayRandom(E[] array) {
            this.array = array;
            len = array.length;
        }

        public E next() {
            synchronized (lock) {
                if (len <= 0) {
                    return null;
                }
                if (len == 1) {
                    return array[--len];
                }
                int index = r.nextInt(len);
                if (index == len - 1) {
                    return array[--len];
                }
                E c = array[index];
                array[index] = array[--len];
                return c;
            }
        }
    }

    public static class ListRandom<E> {

        private final List<E> list;
        private Integer len;
        private final Random r = new Random();
        private final Object lock = new Object();

        private ListRandom(List<E> list) {
            this.list = list;
            len = list.size();
        }

        public E next() {
            synchronized (lock) {
                if (len <= 0) {
                    return null;
                }
                if (len == 1) {
                    return list.get(--len);
                }
                int index = r.nextInt(len);
                if (index == len - 1) {
                    return list.get(--len);
                }
                E c = list.get(index);
                list.set(index, list.get(--len));
                return c;
            }
        }
    }

}
