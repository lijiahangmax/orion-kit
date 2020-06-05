package com.orion.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orion.utils.Strings;

import java.util.*;

/**
 * Gson工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/5/29 16:11
 */
public class Gsons {

    private Gsons() {
    }

    /**
     * 单例gson对象
     */
    private static class GsonInstant {
        private static Gson GSON = new Gson();
    }

    /**
     * 获得gson对象
     */
    public static Gson get() {
        return GsonInstant.GSON;
    }

    /**
     * 修改gson对象
     */
    public static Gson set(GsonBuilder builder) {
        return (GsonInstant.GSON = builder.create());
    }

    /**
     * 修改gson对象
     */
    public static Gson set(Gson gson) {
        return (GsonInstant.GSON = gson);
    }

    /**
     * json序列化对象
     */
    public static String toJSON(Object o) {
        if (o == null) {
            return "";
        }
        return get().toJson(o);
    }

    /**
     * json反序列化对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return null;
        }
        return get().fromJson(json, clazz);
    }

    /**
     * json序列化list
     */
    public static String toJSON(List l) {
        if (l == null) {
            return "";
        }
        return get().toJson(l, TypeToken.get(List.class).getType());
    }

    /**
     * json反序列化list
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return new ArrayList<>();
        }
        return get().fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * json序列化set
     */
    public static String toJSON(Set s) {
        if (s == null) {
            return "";
        }
        return get().toJson(s, TypeToken.get(Set.class).getType());
    }

    /**
     * json反序列化set
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return new HashSet<>();
        }
        return get().fromJson(json, TypeToken.getParameterized(Set.class, clazz).getType());
    }

    /**
     * json序列化map
     */
    public static String toJSON(Map map) {
        if (map == null) {
            return "";
        }
        return get().toJson(map, TypeToken.get(Map.class).getType());
    }

    /**
     * json反序列化集合
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valClass) {
        if (Strings.isBlank(json)) {
            return new HashMap<>();
        }
        return get().fromJson(json, TypeToken.getParameterized(Map.class, keyClass, valClass).getType());
    }

}
