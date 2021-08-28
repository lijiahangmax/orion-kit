package com.orion.lang.wrapper;

import com.orion.utils.Objects1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分组集合
 * 根据getter方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/3 15:59
 */
public class GroupList<E> {

    private List<E> list;

    public GroupList(List<E> list) {
        this.list = list;
    }

    public static <E> GroupList<E> of(List<E> list) {
        return new GroupList<>(list);
    }

    /**
     * 通过字段和值提取分组
     *
     * @param mapping 字段
     * @param value   值
     * @param <V>     V
     * @return ignore
     */
    public <V> List<E> group(Function<E, V> mapping, V value) {
        List<E> groupList = new ArrayList<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            if (Objects1.eq(value, mapping.apply(v))) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段和值提取分组
     *
     * @param mapping 字段
     * @param f       Predicate
     * @param <V>     V
     * @return ignore
     */
    public <V> List<E> group(Function<E, V> mapping, Predicate<V> f) {
        List<E> groupList = new ArrayList<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            V apply = mapping.apply(v);
            if (f.test(apply)) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段提取分组
     *
     * @param mapping 字段
     * @param <V>     V
     * @return ignore
     */
    public <V> Map<V, List<E>> group(Function<E, V> mapping) {
        Map<V, List<E>> map = new LinkedHashMap<>();
        for (E v : list) {
            if (v == null) {
                continue;
            }
            V apply = mapping.apply(v);
            List<E> vs = map.computeIfAbsent(apply, k -> new ArrayList<>());
            vs.add(v);
        }
        return map;
    }

}
