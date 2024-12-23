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
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Set 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/29 17:06
 */
@SuppressWarnings("ALL")
public class Sets extends Collections {

    private Sets() {
    }

    public static <E> Set<E> newSet() {
        return new HashSet<>();
    }

    public static <E> Set<E> newSet(int capacity) {
        return new HashSet<>(capacity);
    }

    public static <E> Set<E> newSet(Collection<? extends E> c) {
        if (c == null) {
            return new HashSet<>();
        }
        return new HashSet<>(c);
    }

    public static <E> TreeSet<E> newTreeSet() {
        return new TreeSet<>();
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet<>(comparator);
    }

    public static <E> TreeSet<E> newTreeSet(SortedSet<E> s) {
        if (s == null) {
            return new TreeSet<>();
        }
        return new TreeSet<>(s);
    }

    public static <E> TreeSet<E> newTreeSet(Collection<? extends E> c) {
        if (c == null) {
            return new TreeSet<>();
        }
        return new TreeSet<>(c);
    }

    public static <E> MutableHashSet<E> newMutableSet() {
        return new MutableHashSet<>();
    }

    public static <E> MutableHashSet<E> newMutableSet(int capacity) {
        return new MutableHashSet<>(capacity);
    }

    public static <E> MutableHashSet<E> newMutableSet(Collection<? extends E> c) {
        if (c == null) {
            return new MutableHashSet<>();
        }
        return new MutableHashSet<>(c);
    }

    public static <E> MutableTreeSet<E> newMutableTreeSet() {
        return new MutableTreeSet<>();
    }

    public static <E> MutableTreeSet<E> newMutableTreeSet(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    public static <E> MutableTreeSet<E> newMutableTreeSet(SortedSet<E> s) {
        if (s == null) {
            return new MutableTreeSet<>();
        }
        return new MutableTreeSet<>(s);
    }

    public static <E> MutableTreeSet<E> newMutableTreeSet(Collection<? extends E> c) {
        if (c == null) {
            return new MutableTreeSet<>();
        }
        return new MutableTreeSet<>(c);
    }

    public static <E> Set<E> newSynchronizedSet() {
        return java.util.Collections.synchronizedSet(new HashSet<>());
    }

    public static <E> Set<E> newSynchronizedSet(Set<E> set) {
        if (set == null) {
            return java.util.Collections.synchronizedSet(new HashSet<>());
        }
        return java.util.Collections.synchronizedSet(set);
    }

    public static <E> SortedSet<E> newSynchronizedSortedSet() {
        return java.util.Collections.synchronizedSortedSet(new TreeSet<>());
    }

    public static <E> SortedSet<E> newSynchronizedSortedSet(SortedSet<E> set) {
        if (set == null) {
            return java.util.Collections.synchronizedSortedSet(new TreeSet<>());
        }
        return java.util.Collections.synchronizedSortedSet(set);
    }

    public static BitSet newBitSet() {
        return new BitSet();
    }

    public static BitSet newBitSet(int initialSize) {
        return new BitSet(initialSize);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet() {
        return new ConcurrentHashSet<>();
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(int capacity) {
        return new ConcurrentHashSet<>(capacity);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(Collection<? extends E> c) {
        if (c == null) {
            return new ConcurrentHashSet<>();
        }
        return new ConcurrentHashSet<>(c);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(Map<? extends E, ?> m) {
        if (m == null) {
            return new ConcurrentHashSet<>();
        }
        return new ConcurrentHashSet<>(m);
    }

    public static <E> Set<E> unmodified(Set<? extends E> c) {
        return java.util.Collections.unmodifiableSet(c);
    }

    public static <E> Set<E> unmodified(SortedSet<E> c) {
        return java.util.Collections.unmodifiableSortedSet(c);
    }

    public static <E> Set<E> singleton(E e) {
        return new SingletonSet<>(e);
    }

    public static <E> Set<E> empty() {
        return (Set<E>) EmptySet.EMPTY;
    }

    // -------------------- function --------------------

    public static <E> Set<E> def(Set<E> set) {
        return set == null ? new HashSet<>() : set;
    }

    public static <E> Set<E> def(Set<E> set, Set<E> def) {
        return set == null ? def : set;
    }

    public static <E> Set<E> def(Set<E> set, Supplier<Set<E>> def) {
        return set == null ? def.get() : set;
    }

    @SafeVarargs
    public static <E> Set<E> of(E... array) {
        return new HashSet<>(Arrays.asList(array));
    }

    @SafeVarargs
    public static <E, V> Set<E> of(Function<V, E> mapper, V... values) {
        Valid.notNull(mapper, "convert function is null");
        Set<E> list = new HashSet<>();
        int length = Arrays1.length(values);
        for (int i = 0; i < length; i++) {
            list.add(mapper.apply(values[i]));
        }
        return list;
    }

    public static <E, V> Set<E> map(List<V> l, Function<V, E> mapper) {
        Valid.notNull(mapper, "convert function is null");
        Set<E> set = new HashSet<>();
        if (isEmpty(l)) {
            return set;
        }
        for (V v : l) {
            set.add(mapper.apply(v));
        }
        return set;
    }

    public static <E> Set<E> as(Iterator<E> iterator) {
        Set<E> list = new LinkedHashSet<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    public static <E> Set<E> as(Enumeration<E> iterator) {
        Set<E> list = new LinkedHashSet<>();
        if (iterator != null) {
            while (iterator.hasMoreElements()) {
                list.add(iterator.nextElement());
            }
        }
        return list;
    }

    /**
     * 合并set
     *
     * @param source   合并到的 set
     * @param setArray 需要合并的 set
     * @param <E>      ignore
     */
    @SafeVarargs
    public static <E> void merge(Set<E> source, Set<E>... setArray) {
        if (setArray == null) {
            return;
        }
        for (Set<E> m : setArray) {
            if (m != null) {
                source.addAll(m);
            }
        }
    }

    /**
     * 从set获取元素
     *
     * @param set set
     * @param i   index
     * @param <E> ignore
     * @return 元素
     */
    public static <E> E get(Set<E> set, int i) {
        if (size(set) <= i) {
            return null;
        }
        int idx = 0;
        for (E e : set) {
            if (idx++ == i) {
                return e;
            }
        }
        return null;
    }

    /**
     * 从set随机获取一个元素
     *
     * @param set set
     * @param <E> ignore
     * @return 元素
     */
    public static <E> E random(Set<E> set) {
        int size = size(set);
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return set.iterator().next();
        } else {
            return get(set, Randoms.RANDOM.nextInt(size));
        }
    }

    /**
     * 删除元素到指定大小
     *
     * @param set  set
     * @param size 大小
     * @param <E>  E
     */
    public static <E> void removeToSize(Set<E> set, int size) {
        int oldSize = size(set);
        if (size >= oldSize) {
            return;
        }
        Iterator<E> iterator = set.iterator();
        int d = oldSize - size;
        for (int i = 0; i < size; i++) {
            iterator.next();
        }
        for (int i = 0; i < d; i++) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * set 分片
     *
     * @param set  set
     * @param size size
     * @param <E>  E
     * @return partition set
     */
    public static <E> Set<Set<E>> partition(Set<E> set, int size) {
        return new PartitionSet<>(set, size);
    }

    /**
     * 集合去重 -> set
     *
     * @param c         c
     * @param keyGetter keyGetter
     * @param <E>       E
     * @return distinct set
     */
    public static <E> Set<E> distinct(Collection<E> c, Function<E, ?> keyGetter) {
        return new HashSet<>(Collections.distinct(c, keyGetter));
    }

}
