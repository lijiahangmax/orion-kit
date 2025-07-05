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
package cn.orionsec.kit.lang.utils.collect;

import cn.orionsec.kit.lang.define.collect.*;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * List 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/29 16:58
 */
@SuppressWarnings("ALL")
public class Lists extends Collections {

    private Lists() {
    }

    // -------------------- new --------------------

    public static <E> List<E> newList() {
        return new ArrayList<>();
    }

    public static <E> List<E> newList(int capacity) {
        return new ArrayList<>(capacity);
    }

    public static <E> List<E> newList(Collection<? extends E> c) {
        if (c == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(c);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static <E> LinkedList<E> newLinkedList(Collection<? extends E> c) {
        if (c == null) {
            return new LinkedList<>();
        }
        return new LinkedList<>(c);
    }

    public static <E> MutableArrayList<E> newMutableList() {
        return new MutableArrayList<>();
    }

    public static <E> MutableArrayList<E> newMutableList(int capacity) {
        return new MutableArrayList<>(capacity);
    }

    public static <E> MutableArrayList<E> newMutableList(Collection<? extends E> c) {
        if (c == null) {
            return new MutableArrayList<>();
        }
        return new MutableArrayList<>(c);
    }

    public static <E> MutableLinkedList<E> newMutableLinkedList() {
        return new MutableLinkedList<>();
    }

    public static <E> MutableLinkedList<E> newMutableLinkedList(Collection<? extends E> c) {
        if (c == null) {
            return new MutableLinkedList<>();
        }
        return new MutableLinkedList<>(c);
    }

    public static <E> MutableVector<E> newMutableVector() {
        return new MutableVector<>();
    }

    public static <E> MutableVector<E> newMutableVector(int initialCapacity) {
        return new MutableVector<>(initialCapacity);
    }

    public static <E> MutableVector<E> newMutableVector(int initialCapacity, int capacityIncrement) {
        return new MutableVector<>(initialCapacity, capacityIncrement);
    }

    public static <E> MutableVector<E> newMutableVector(Collection<? extends E> c) {
        if (c == null) {
            return new MutableVector<>();
        }
        return new MutableVector<>(c);
    }

    public static <E> FixedArrayList<E> newFixedList(int limit) {
        return new FixedArrayList<>(limit);
    }

    public static <E> LimitList<E> newLimitList() {
        return new LimitList<>();
    }

    public static <E> LimitList<E> newLimitList(int limit) {
        return new LimitList<>(limit);
    }

    public static <E> LimitList<E> newLimitList(int initialCapacity, int limit) {
        return new LimitList<>(initialCapacity, limit);
    }

    public static <E> LimitList<E> newLimitList(Collection<? extends E> c) {
        return new LimitList<>(c);
    }

    public static <E> LimitList<E> newLimitList(Collection<? extends E> c, int limit) {
        return new LimitList<>(c, limit);
    }

    public static <E> List<E> newCopyOnWriteList() {
        return new CopyOnWriteArrayList<>();
    }

    public static <E> List<E> newCopyOnWriteList(E[] array) {
        return new CopyOnWriteArrayList<>(array);
    }

    public static <E> List<E> newCopyOnWriteList(Collection<? extends E> c) {
        if (c == null) {
            return new CopyOnWriteArrayList<>();
        }
        return new CopyOnWriteArrayList<>(c);
    }

    public static <E> List<E> newSynchronizedList() {
        return java.util.Collections.synchronizedList(new ArrayList<>());
    }

    public static <E> List<E> newSynchronizedList(List<E> list) {
        if (list == null) {
            return java.util.Collections.synchronizedList(new ArrayList<>());
        }
        return java.util.Collections.synchronizedList(list);
    }

    public static <E> List<E> unmodified(List<E> list) {
        return java.util.Collections.unmodifiableList(list);
    }

    public static <E> List<E> singleton(E e) {
        return new SingletonList<>(e);
    }

    public static <E> List<E> singletonNullable(E e) {
        List<E> list = new ArrayList<>();
        if (e != null) {
            list.add(e);
        }
        return list;
    }

    public static <E> List<E> empty() {
        return (List<E>) EmptyList.EMPTY;
    }

    // -------------------- function --------------------

    public static <E> List<E> def(List<E> list) {
        return list == null ? new ArrayList<>() : list;
    }

    public static <E> List<E> def(List<E> list, List<E> def) {
        return list == null ? def : list;
    }

    public static <E> List<E> def(List<E> list, Supplier<List<E>> def) {
        return list == null ? def.get() : list;
    }

    @SafeVarargs
    public static <E> List<E> of(E... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    @SafeVarargs
    public static <E, V> List<E> of(Function<V, E> mapper, V... values) {
        Valid.notNull(mapper, "convert function is null");
        List<E> list = new ArrayList<>();
        int length = Arrays1.length(values);
        for (int i = 0; i < length; i++) {
            list.add(mapper.apply(values[i]));
        }
        return list;
    }

    public static <E, V> List<E> map(List<V> list, Function<V, E> mapper) {
        Valid.notNull(mapper, "convert function is null");
        List<E> result = new ArrayList<>();
        if (isEmpty(list)) {
            return result;
        }
        for (V v : list) {
            result.add(mapper.apply(v));
        }
        return result;
    }

    public static <E> List<E> as(Iterator<E> iterator) {
        List<E> list = new ArrayList<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    public static <E> List<E> as(Enumeration<E> iterator) {
        List<E> list = new ArrayList<>();
        if (iterator != null) {
            while (iterator.hasMoreElements()) {
                list.add(iterator.nextElement());
            }
        }
        return list;
    }

    /**
     * 保留集合的前几位
     *
     * @param list 集合
     * @param len  保留几位
     * @param <E>  ignore
     */
    public static <E> void cutAfter(List<E> list, int len) {
        cutAround(list, 0, len);
    }

    /**
     * 保留集合的后几位
     *
     * @param list 集合
     * @param len  保留几位
     * @param <E>  ignore
     */
    public static <E> void cutBefore(List<E> list, int len) {
        cutAround(list, size(list) - len, len);
    }

    /**
     * 保留集合的中间几位
     *
     * @param list  集合
     * @param start 从哪位开始切
     * @param len   保留几位
     * @param <E>   ignore
     */
    public static <E> void cutAround(List<E> list, int start, int len) {
        int size = size(list);
        if (size >= len + start) {
            for (int i = size - len - start; i > 0; i--) {
                list.remove(len + i - 1);
            }
            if (start > 0) {
                list.subList(0, start).clear();
            }
        } else {
            throw Exceptions.index("cut list error: startIndex: " + start + ", cutLength: " + len + ", needSize: " + (start + len) + ", but maxSize: " + size);
        }
    }

    /**
     * 合并 list
     *
     * @param source    合并到的 list
     * @param listArray 需要合并的 list
     * @param <E>       ignore
     */
    @SafeVarargs
    public static <E> void merge(List<E> source, List<E>... listArray) {
        if (listArray == null) {
            return;
        }
        for (List<E> m : listArray) {
            if (m != null) {
                source.addAll(m);
            }
        }
    }

    /**
     * 从list随机获取一个元素
     *
     * @param list list
     * @param <E>  ignore
     * @return 元素
     */
    public static <E> E random(List<E> list) {
        int size = size(list);
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return list.get(0);
        } else {
            return list.get(Randoms.RANDOM.nextInt(size));
        }
    }

    /**
     * 翻转集合
     *
     * @param list 集合
     * @param <E>  ignore
     */
    public static <E> void reverse(List<E> list) {
        int size = size(list);
        if (size == 0) {
            return;
        }
        for (int i = 0, mid = size / 2; i < mid; i++) {
            int len = size - i - 1;
            if (list.get(i) != list.get(len)) {
                list.set(len, list.set(i, list.get(len)));
            }
        }
    }

    /**
     * 查找第一个查询到的元素的位置
     *
     * @param list 集合
     * @param e    元素
     * @param <E>  ignore
     * @return 位置
     */
    public static <E> int indexOf(List<E> list, E e) {
        if (isEmpty(list)) {
            return -1;
        }
        return list.indexOf(e);
    }

    /**
     * 查找最后一个查询到的元素的位置
     *
     * @param list 集合
     * @param e    元素
     * @param <E>  ignore
     * @return 位置
     */
    public static <E> int lastIndexOf(List<E> list, E e) {
        if (isEmpty(list)) {
            return -1;
        }
        return list.lastIndexOf(e);
    }

    /**
     * 集合是否相等
     *
     * @param list1 ignore
     * @param list2 ignore
     * @return true 相等
     */
    public static boolean eq(List<?> list1, List<?> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        int size = size(list1);
        if (size != size(list2)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!Objects1.eq(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除元素到指定大小
     *
     * @param list list
     * @param size 大小
     * @param <E>  E
     */
    public static <E> void removeToSize(List<E> list, int size) {
        int beforeSize = size(list);
        if (size >= beforeSize) {
            return;
        }
        list.subList(size, beforeSize).clear();
    }

    /**
     * list 分片
     *
     * @param list list
     * @param size size
     * @param <E>  E
     * @return partition list
     */
    public static <E> List<List<E>> partition(List<E> list, int size) {
        return new PartitionList<>(list, size);
    }


    /**
     * 集合去重 -> list
     *
     * @param c         c
     * @param keyGetter keyGetter
     * @param <E>       E
     * @return distinct list
     */
    public static <E> List<E> distinct(Collection<E> c, Function<E, ?> keyGetter) {
        return new ArrayList<>(Collections.distinct(c, keyGetter));
    }

    // -------------------- get set --------------------

    /**
     * 获取元素 如果元素存在
     *
     * @param list list
     * @param i    i
     * @param <E>  E
     * @return E
     */
    public static <E> E getIfPresent(List<E> list, int i) {
        int size = size(list);
        if (size == 0) {
            return null;
        }
        if (i >= size) {
            return null;
        }
        return list.get(i);
    }

    public static <E> E get(List<E> list, int i) {
        int size = size(list);
        if (size == 0) {
            return null;
        }
        if (i >= size) {
            throw Exceptions.index("list length: " + size + " get index: " + i);
        }
        return list.get(i);
    }

    public static <E> void set(List<E> list, int i, E e) {
        sets(list, i, e, true);
    }

    public static <E> void set(List<E> list, int i, E e, boolean force) {
        sets(list, i, e, force);
    }

    public static <E> void setex(List<E> list, int i, E e) {
        sets(list, i, e, false);
    }

    public static <E> E getSet(List<E> list, int i, E e) {
        return sets(list, i, e, true);
    }

    private static <E> E sets(List<E> list, int i, E e, boolean force) {
        int size = size(list);
        if (size == 0) {
            return null;
        }
        if (i >= size) {
            throw Exceptions.index("list length: " + size + " get index: " + i);
        }
        E o = list.get(i);
        if (force) {
            list.set(i, e);
            return o;
        } else {
            if (o == null) {
                list.set(i, e);
            }
            return null;
        }
    }

    // -------------------- toArray --------------------

    public static byte[] toBytes(List<Byte> list) {
        int size = size(list);
        byte[] arr = new byte[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static short[] toShorts(List<Short> list) {
        int size = size(list);
        short[] arr = new short[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static int[] toInts(List<Integer> list) {
        int size = size(list);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static long[] toLongs(List<Long> list) {
        int size = size(list);
        long[] arr = new long[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static float[] toFloats(List<Float> list) {
        int size = size(list);
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static double[] toDoubles(List<Double> list) {
        int size = size(list);
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static boolean[] toBooleans(List<Boolean> list) {
        int size = size(list);
        boolean[] arr = new boolean[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static char[] toChars(List<Character> list) {
        int size = size(list);
        char[] arr = new char[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * 拷贝集合
     *
     * @param src    原集合
     * @param target 目标集合
     * @param <T>    ignore
     */
    public static <T> void copy(List<? extends T> src, List<? super T> target) {
        int srcSize = size(src);
        int distSize = size(target);
        int size = Math.min(srcSize, distSize);
        if (srcSize < 10 || (src instanceof RandomAccess && target instanceof RandomAccess)) {
            for (int i = 0; i < size; i++) {
                target.set(i, src.get(i));
            }
        } else {
            ListIterator<? super T> ti = target.listIterator();
            ListIterator<? extends T> si = src.listIterator();
            for (int i = 0; i < size; i++) {
                ti.next();
                ti.set(si.next());
            }
        }
    }

}
