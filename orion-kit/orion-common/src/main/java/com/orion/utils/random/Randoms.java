package com.orion.utils.random;

import com.orion.utils.Strings;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/10/9 10:16
 */
public class Randoms {

    private Randoms() {
    }

    public static final Random RANDOM = new Random();

    private static final String ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTER = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER = "0123456789";

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
     * @param max 最大值
     * @return ignore
     */
    public static int randomInt(int max) {
        return randomInt(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值
     * @param max 最大值
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
     * @param max 最大值
     * @return ignore
     */
    public static double randomDouble(double max) {
        return randomDouble(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值
     * @param max 最大值
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
     * @param max 最大值
     * @return ignore
     */
    public static long randomLong(long max) {
        return randomLong(0, max);
    }

    /**
     * 随机一个数字
     *
     * @param min 最小值
     * @param max 最大值
     * @return ignore
     */
    public static long randomLong(long min, long max) {
        return (long) (RANDOM.nextDouble() * (max - min)) + min;
    }

    /**
     * 返回一个随机的布尔值
     *
     * @param i 几率 1/i
     * @return boolean
     */
    public static boolean randomBoolean(int i) {
        return RANDOM.nextInt(i) == i - 1;
    }

    /**
     * 返回固定长度的数字
     *
     * @param length 长度
     * @return ignor
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

        private E[] array;
        private Integer len;
        private Random r = new Random();
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

        private List<E> list;
        private Integer len;
        private Random r = new Random();
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
