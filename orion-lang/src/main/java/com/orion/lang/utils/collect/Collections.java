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
package com.orion.lang.utils.collect;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 集合工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/29 17:31
 */
@SuppressWarnings("ALL")
public class Collections {

    protected Collections() {
    }

    public static <E> Collection<E> newSynchronizedCollection(Collection<E> c) {
        return java.util.Collections.synchronizedCollection(c);
    }

    public static <E> Collection<E> unmodified(Collection<E> c) {
        return java.util.Collections.unmodifiableCollection(c);
    }

    /**
     * 将集合连接成字符串
     *
     * @param c 集合
     * @return String
     */
    public static String join(Collection<?> c) {
        return join(c, Const.COMMA, Const.EMPTY, Const.EMPTY);
    }

    public static String join(Collection<?> c, String delimiter) {
        return join(c, delimiter, Const.EMPTY, Const.EMPTY);
    }

    public static String join(Collection<?> c, String delimiter, String open, String end) {
        int size = size(c);
        open = Strings.def(open);
        end = Strings.def(end);
        delimiter = Strings.def(delimiter);
        if (size == 0) {
            return open + end;
        }
        StringBuilder sb = new StringBuilder(open);
        Iterator<?> iterator = c.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (++i != size) {
                sb.append(delimiter);
            }
        }
        return sb.append(end).toString();
    }

    /**
     * 获取stream
     *
     * @param c   c
     * @param <E> E
     * @return stream
     */
    public static <E> Stream<E> stream(Collection<E> c) {
        return isEmpty(c) ? Stream.empty() : c.stream();
    }

    /**
     * 遍历
     *
     * @param c      c
     * @param action action
     * @param <E>    E
     */
    public static <E> void forEach(Collection<E> c, Consumer<? super E> action) {
        if (isEmpty(c)) {
            return;
        }
        c.forEach(action);
    }

    /**
     * 集合长度
     *
     * @param c 集合
     * @return 长度
     */
    public static int size(Collection<?> c) {
        return c == null ? 0 : c.size();
    }

    /**
     * 集合是否为空
     *
     * @param c 集合
     * @return true为空
     */
    public static boolean isEmpty(Collection<?> c) {
        return size(c) == 0;
    }

    /**
     * 集合是否不为空
     *
     * @param c 集合
     * @return true不为空
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    /**
     * 集合是否全为空
     *
     * @param cs 集合
     * @return true全为空
     */
    public static boolean isAllEmpty(Collection<?>... cs) {
        if (cs == null) {
            return true;
        }
        for (Collection<?> c : cs) {
            if (!isEmpty(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 集合是否全不为空
     *
     * @param cs 集合
     * @return true全不为空, 参数为空false
     */
    public static boolean isNoneEmpty(Collection<?>... cs) {
        if (cs == null) {
            return false;
        }
        for (Collection<?> c : cs) {
            if (isEmpty(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 集合是否包含元素
     *
     * @param c   c
     * @param e   e
     * @param <E> E
     * @return 是否包含
     */
    public static <E> boolean contains(Collection<E> c, E e) {
        if (isEmpty(c)) {
            return false;
        }
        return c.contains(e);
    }

    /**
     * 集合是否全部包含元素
     *
     * @param c   c
     * @param e   e
     * @param <E> E
     * @return 是否全部包含
     */
    public static <E> boolean containsAll(Collection<E> c, Collection<E> e) {
        if (isEmpty(c)) {
            return false;
        }
        return c.containsAll(e);
    }

    /**
     * 去除集合中的null
     *
     * @param c 集合
     */
    public static void compact(Collection<?> c) {
        if (size(c) == 0) {
            return;
        }
        while (c.remove(null)) {
            // ignore
        }
    }

    /**
     * 排除集合中指定的值
     *
     * @param c  集合
     * @param es 排除值 这个值可以为基本类型的元素, 但是不能是基本类型的数组
     */
    public static void exclude(Collection<?> c, Object... es) {
        if (size(c) == 0) {
            return;
        }
        Set<Object> e = Sets.of(es);
        for (Object o : e) {
            while (c.remove(o)) {
                // ignore
            }
        }
    }

    /**
     * 查询交集
     *
     * @param c1  集合1
     * @param c2  集合2
     * @param <E> 类型
     * @return 交集
     */
    public static <E> Set<E> inter(Collection<E> c1, Collection<E> c2) {
        if (isEmpty(c1) || isEmpty(c2)) {
            return new HashSet<>();
        }
        Set<E> r = new HashSet<>(c1);
        r.retainAll(c2);
        return r;
    }

    /**
     * 查询并集
     *
     * @param c1  集合1
     * @param c2  集合2
     * @param <E> 类型
     * @return 并集
     */
    public static <E> Set<E> union(Collection<E> c1, Collection<E> c2) {
        if (isEmpty(c1) && isEmpty(c2)) {
            return new HashSet<>();
        } else if (isEmpty(c1) && !isEmpty(c2)) {
            return new HashSet<>(c2);
        } else if (isEmpty(c2) && !isEmpty(c1)) {
            return new HashSet<>(c1);
        }
        Set<E> r = new HashSet<>(c1);
        r.addAll(c2);
        return r;
    }

    /**
     * 查询差集
     *
     * @param c1  集合1
     * @param c2  集合2
     * @param <E> 类型
     * @return 差集
     */
    public static <E> Set<E> diff(Collection<E> c1, Collection<E> c2) {
        return diff(c1, c2, false);
    }

    /**
     * 查询差集 先合并
     *
     * @param c1  集合1
     * @param c2  集合2
     * @param <E> 类型
     * @return 差集
     */
    public static <E> Set<E> diffAll(Collection<E> c1, Collection<E> c2) {
        return diff(c1, c2, true);
    }

    /**
     * 查询差集
     *
     * @param c1      集合1
     * @param c2      集合2
     * @param <E>     类型
     * @param diffAll 是否先合并
     * @return 差集
     */
    public static <E> Set<E> diff(Collection<E> c1, Collection<E> c2, boolean diffAll) {
        if (isEmpty(c2) && isEmpty(c1)) {
            return new HashSet<>();
        } else if (isEmpty(c2) && !isEmpty(c1)) {
            return new HashSet<>(c1);
        } else if (isEmpty(c1) && !isEmpty(c2)) {
            return new HashSet<>(c2);
        }
        Set<E> r = new HashSet<>(c1);
        if (diffAll) {
            HashSet<E> i = new HashSet<>(c1);
            r.addAll(c2);
            i.retainAll(c2);
            r.removeAll(i);
        } else {
            r.removeAll(c2);
        }
        return r;
    }

    /**
     * 将指定元素装配给集合
     *
     * @param list  集合
     * @param value 元素
     * @param <E>   ignore
     */
    public static <E> void fill(List<E> list, E value) {
        fill(list, value, 0, size(list) - 1);
    }

    public static <E> void fill(List<E> list, E value, int start) {
        fill(list, value, start, size(list) - 1);
    }

    public static <E> void fill(List<E> list, E value, int start, int end) {
        int size = size(list);
        if (size != 0 && size > start && size > end && (start != end) && start >= 0 && end > 0) {
            for (int i = start; i < end + 1; i++) {
                list.set(i, value);
            }
        }
    }

    /**
     * 最小值
     *
     * @param c   集合
     * @param <T> 必须实现Comparable接口
     * @return 最小值
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> c) {
        Iterator<? extends T> i = c.iterator();
        T cn = i.next();
        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(cn) < 0) {
                cn = next;
            }
        }
        return cn;
    }

    /**
     * 最小值
     *
     * @param c    集合
     * @param comp 排序接口
     * @param <T>  ignore
     * @return 最小值
     */
    public static <T> T min(Collection<? extends T> c, Comparator<? super T> comp) {
        if (comp == null) {
            return (T) min((Collection) c);
        }
        Iterator<? extends T> i = c.iterator();
        T cn = i.next();
        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, cn) < 0) {
                cn = next;
            }
        }
        return cn;
    }

    /**
     * 最大值
     *
     * @param c   集合
     * @param <T> 必须实现Comparable接口
     * @return 最小值
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> c) {
        Iterator<? extends T> i = c.iterator();
        T cn = i.next();
        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(cn) > 0) {
                cn = next;
            }
        }
        return cn;
    }

    /**
     * 最大值
     *
     * @param c    集合
     * @param comp 排序接口
     * @param <T>  ignore
     * @return 最小值
     */
    public static <T> T max(Collection<? extends T> c, Comparator<? super T> comp) {
        if (comp == null) {
            return (T) max((Collection) c);
        }
        Iterator<? extends T> i = c.iterator();
        T cn = i.next();
        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, cn) > 0) {
                cn = next;
            }
        }
        return cn;
    }

    /**
     * 获取第一个元素
     *
     * @param c   集合
     * @param <E> ignore
     * @return 第一个元素
     */
    public static <E> E first(Collection<E> c) {
        if (size(c) == 0) {
            return null;
        }
        return c.iterator().next();
    }

    /**
     * 获取最后一个元素
     *
     * @param c   集合
     * @param <E> ignore
     * @return 最后一个元素
     */
    public static <E> E last(Collection<E> c) {
        if (size(c) == 0) {
            return null;
        }
        if (c instanceof List) {
            List<E> list = (List<E>) c;
            return list.get(list.size() - 1);
        }
        Iterator<E> iterator = c.iterator();
        while (true) {
            E e = iterator.next();
            if (!iterator.hasNext()) {
                return e;
            }
        }
    }

    /**
     * 集合去重
     *
     * @param c         c
     * @param keyGetter keyGetter
     * @param <E>       E
     * @return distinct collection
     */
    public static <E> Collection<E> distinct(Collection<E> c, Function<E, ?> keyGetter) {
        Map<Object, E> map = new LinkedHashMap<>(16);
        for (E e : c) {
            map.put(keyGetter.apply(e), e);
        }
        return map.values();
    }

    /**
     * 查询第一个重复项
     *
     * @param list list
     * @param <T>  T
     * @return 重复项目
     */
    public static <T> T getFirstDuplicateItem(Collection<T> list) {
        Map<T, Integer> map = Maps.newMap();
        for (T item : list) {
            if (map.containsKey(item)) {
                return item;
            } else {
                map.put(item, 1);
            }
        }
        return null;
    }

    /**
     * 查询全部重复项
     *
     * @param list list
     * @param <T>  T
     * @return 重复项目
     */
    public static <T> List<T> getDuplicateItems(Collection<T> list) {
        Map<T, Integer> map = Maps.newMap();
        for (T item : list) {
            if (map.containsKey(item)) {
                map.put(item, map.get(item) + 1);
            } else {
                map.put(item, 1);
            }
        }
        // 返回
        List<T> result = Lists.newList();
        map.forEach((k, v) -> {
            if (v > 1) {
                result.add(k);
            }
        });
        return result;
    }

}
