package com.orion.lang;

import com.orion.utils.Objects1;
import com.orion.utils.reflect.Reflects;
import com.orion.utils.Strings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * 分组集合
 * 根据getter方法
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/3 15:59
 */
public class GroupList<V> {

    private static final MultiHashMap<Class, String, Method> METHOD_CACHE = new MultiHashMap<>(true);

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
            Object val = Reflects.invokeMethod(v, getGetterMethod(v.getClass(), field));
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
    public List<V> group(String field, Predicate<Object> f) {
        List<V> groupList = new ArrayList<>();
        for (V v : list) {
            if (v == null) {
                continue;
            }
            Object val = Reflects.invokeMethod(v, getGetterMethod(v.getClass(), field));
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
        Map<E, List<V>> map = new TreeMap<>();
        for (V v : list) {
            if (v == null) {
                continue;
            }
            E val = Reflects.invokeMethod(v, getGetterMethod(v.getClass(), field));
            List<V> vs = map.computeIfAbsent(val, k -> new ArrayList<>());
            vs.add(v);
        }
        return map;
    }

    /**
     * 获取getter方法
     *
     * @param clazz class
     * @param field field
     * @return getter
     */
    private static Method getGetterMethod(Class clazz, String field) {
        Method method = METHOD_CACHE.get(clazz, field);
        if (method == null) {
            method = Reflects.getAccessibleMethod(clazz, "get" + Strings.firstUpper(field), 0);
            if (method == null) {
                method = Reflects.getAccessibleMethod(clazz, "is" + Strings.firstUpper(field), 0);
            }
            if (method != null) {
                METHOD_CACHE.put(clazz, field, method);
            }
        }
        return method;
    }

}
