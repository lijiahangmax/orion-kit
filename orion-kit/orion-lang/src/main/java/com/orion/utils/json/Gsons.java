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
 * @since 2019/5/29 16:11
 */
public class Gsons {

    private Gsons() {
    }

    /**
     * 单例gson对象
     */
    private static class GsonInstant {
        private static Gson gson = new Gson();
    }

    /**
     * 获得gson对象
     */
    public static Gson get() {
        return GsonInstant.gson;
    }

    /**
     * 修改gson对象
     */
    public static Gson set(GsonBuilder builder) {
        return (GsonInstant.gson = builder.create());
    }

    /**
     * 修改gson对象
     */
    public static Gson set(Gson gson) {
        return (GsonInstant.gson = gson);
    }

    /**
     * bean -> json
     *
     * @param o bean
     * @return json
     */
    public static String toJson(Object o) {
        if (o == null) {
            return "";
        }
        return GsonInstant.gson.toJson(o);
    }

    /**
     * json -> bean
     *
     * @param json  json
     * @param clazz bean class
     * @param <T>   T
     * @return bean
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return null;
        }
        return GsonInstant.gson.fromJson(json, clazz);
    }

    /**
     * json -> list
     *
     * @param json  json
     * @param clazz T class
     * @param <T>   T
     * @return list
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return new ArrayList<>();
        }
        return GsonInstant.gson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * json -> set
     *
     * @param json  json
     * @param clazz T class
     * @param <T>   T
     * @return set
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return new HashSet<>();
        }
        return GsonInstant.gson.fromJson(json, TypeToken.getParameterized(Set.class, clazz).getType());
    }

    /**
     * json -> map
     *
     * @param json     json
     * @param keyClass K class
     * @param valClass V class
     * @param <K>      K
     * @param <V>      V
     * @return map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valClass) {
        if (Strings.isBlank(json)) {
            return new HashMap<>();
        }
        return GsonInstant.gson.fromJson(json, TypeToken.getParameterized(Map.class, keyClass, valClass).getType());
    }

    /**
     * json -> object
     *
     * @param json           json
     * @param rawClass       class
     * @param genericClasses 泛型class
     * @param <T>            T
     * @return T
     */
    public static <T> T toObject(String json, Class<?> rawClass, Class<?>... genericClasses) {
        return GsonInstant.gson.fromJson(json, TypeToken.getParameterized(rawClass, genericClasses).getType());
    }

}
