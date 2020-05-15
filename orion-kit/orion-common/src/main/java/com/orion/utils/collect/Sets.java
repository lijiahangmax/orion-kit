package com.orion.utils.collect;

import com.orion.lang.collect.ConcurrentHashSet;
import com.orion.lang.collect.ConvertHashSet;
import com.orion.utils.Arrays1;

import java.util.*;

/**
 * Set 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/29 17:06
 */
@SuppressWarnings("ALL")
public class Sets extends Collections {

    private Sets() {
    }

    // --------------- singleton map ---------------

    private static final Map<Object, Set> SINGLETON_MAP = new HashMap<>(4);

    public static <E> Set<E> newSet() {
        return new HashSet<>();
    }

    public static <E> Set<E> newSet(int capacity) {
        return new HashSet<>(capacity);
    }

    public static <E> Set<E> newSet(Collection<? extends E> c) {
        if (size(c) == 0) {
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

    public static <E> TreeSet<E> newTreeSet(Collection<? extends E> c) {
        if (size(c) == 0) {
            return new TreeSet<>();
        }
        return new TreeSet<>(c);
    }

    public static <E> ConvertHashSet<E> newConvertSet() {
        return new ConvertHashSet<>();
    }

    public static <E> ConvertHashSet<E> newConvertSet(int capacity) {
        return new ConvertHashSet<>(capacity);
    }

    public static <E> ConvertHashSet<E> newConvertSet(Collection<? extends E> c) {
        if (size(c) == 0) {
            return new ConvertHashSet<>();
        }
        return new ConvertHashSet<>(c);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet() {
        return new ConcurrentHashSet<>();
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(int capacity) {
        return new ConcurrentHashSet<>(capacity);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(Collection<? extends E> c) {
        if (size(c) == 0) {
            return new ConcurrentHashSet<>();
        }
        return new ConcurrentHashSet<>(c);
    }

    public static <E> ConcurrentHashSet<E> newCurrentSet(Map<? extends E, ?> m) {
        if (Maps.size(m) == 0) {
            return new ConcurrentHashSet<>();
        }
        return new ConcurrentHashSet<>(m);
    }

    // --------------- singleton ---------------

    public static <E> Set<E> singletonSet(Object key) {
        if (SINGLETON_MAP.containsKey(key)) {
            return SINGLETON_MAP.get(key);
        }
        Set<E> set = new HashSet<>();
        SINGLETON_MAP.put(key, set);
        return set;
    }

    // --------------- function ---------------

    public static <E> Set<E> of(E... e) {
        Set<E> set = new HashSet<>();
        int length = Arrays1.length(e);
        for (int i = 0; i < length; i++) {
            set.add(e[i]);
        }
        return set;
    }

    /**
     * 合并set
     *
     * @param set 合并到的set
     * @param ms  需要合并的set
     * @param <E> ignore
     * @return 合并后的set
     */
    public static <E> Set<E> merge(Set<E> set, Set<E>... ms) {
        if (set == null) {
            set = new HashSet<>();
        }
        if (ms == null) {
            return set;
        }
        for (Set<E> m : ms) {
            if (m != null) {
                set.addAll(m);
            }
        }
        return set;
    }

    /**
     * 从set随机获取一个元素
     *
     * @param set map
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
            return (E) (set.toArray()[new Random().nextInt(size)]);
        }
    }

}
