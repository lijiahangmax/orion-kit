package com.orion.lang.wrapper;

import com.orion.utils.Objects1;
import com.orion.utils.reflect.Methods;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 分组集合
 * 根据getter方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/1/3 15:59
 */
public class GroupList<V> {

    private List<V> list;

    public GroupList(List<V> list) {
        this.list = list;
    }

    /**
     * 通过字段和值提取分组
     *
     * @param field 字段
     * @param value 值
     * @return ignore
     */
    public List<V> group(String field, Object value) {
        List<V> groupList = new ArrayList<>();
        for (V v : list) {
            if (v == null) {
                continue;
            }
            Object val = Methods.invokeMethod(v, Methods.getGetterMethodByCache(v.getClass(), field));
            if (Objects1.eq(value, val)) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段和值提取分组
     *
     * @param field 字段
     * @param f     Predicate
     * @return ignore
     */
    public List<V> group(String field, Predicate<V> f) {
        List<V> groupList = new ArrayList<>();
        for (V v : list) {
            if (v == null) {
                continue;
            }
            V val = Methods.invokeMethod(v, Methods.getGetterMethodByCache(v.getClass(), field));
            if (f.test(val)) {
                groupList.add(v);
            }
        }
        return groupList;
    }

    /**
     * 通过字段提取分组
     *
     * @param field 字段
     * @param <E>   ignore
     * @return ignore
     */
    public <E> Map<E, List<V>> group(String field) {
        Map<E, List<V>> map = new LinkedHashMap<>();
        for (V v : list) {
            if (v == null) {
                continue;
            }
            E val = Methods.invokeMethod(v, Methods.getGetterMethodByCache(v.getClass(), field));
            List<V> vs = map.computeIfAbsent(val, k -> new ArrayList<>());
            vs.add(v);
        }
        return map;
    }

}
