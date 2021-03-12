package com.orion.utils.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.utils.Strings;

import java.util.*;

/**
 * jackson 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/2 10:23
 */
public class JackSons {

    private JackSons() {
    }

    /**
     * 单例Mapper对象
     */
    private static class MapperInstance {
        private static ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
    }

    /**
     * 获得Mapper对象
     */
    public static ObjectMapper get() {
        return MapperInstance.objectMapper;
    }

    /**
     * 修改Mapper对象
     */
    public static ObjectMapper set(ObjectMapper objectMapper) {
        return (MapperInstance.objectMapper = objectMapper);
    }

    /**
     * bean -> json
     *
     * @param bean 对象
     * @return ignore
     */
    public static String toJson(Object bean) {
        if (bean == null) {
            return Strings.EMPTY;
        }
        try {
            return MapperInstance.objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * json -> bean
     *
     * @param json        json
     * @param targetClass ignore
     * @param <T>         ignore
     * @return ignore
     */
    public static <T> T toBean(String json, Class<T> targetClass) {
        if (Strings.isEmpty(json)) {
            return null;
        }
        try {
            return MapperInstance.objectMapper.readValue(json, targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> list
     *
     * @param json  json
     * @param clazz clazz
     * @param <T>   ignore
     * @return ignore
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (Strings.isEmpty(json)) {
            return new ArrayList<>();
        }
        try {
            return MapperInstance.objectMapper.readValue(json, getJavaType(List.class, clazz));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> set
     *
     * @param json  json
     * @param clazz clazz
     * @param <T>   ignore
     * @return ignore
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        if (Strings.isEmpty(json)) {
            return new HashSet<>();
        }
        try {
            return MapperInstance.objectMapper.readValue(json, getJavaType(Set.class, clazz));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> map
     *
     * @param json json
     * @param ks   key class
     * @param vs   value class
     * @param <K>  K
     * @param <V>  V
     * @return ignore
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> ks, Class<V> vs) {
        if (Strings.isEmpty(json)) {
            return new HashMap<>(16);
        }
        try {
            return MapperInstance.objectMapper.readValue(json, getJavaType(Map.class, ks, vs));
        } catch (Exception e) {
            return null;
        }
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
        try {
            return MapperInstance.objectMapper.readValue(json, getJavaType(rawClass, genericClasses));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取带泛型的类型
     *
     * @param rawClass       class
     * @param genericClasses 泛型class
     * @return JavaType
     */
    public static JavaType getJavaType(Class<?> rawClass, Class<?>... genericClasses) {
        return MapperInstance.objectMapper.getTypeFactory().constructParametricType(rawClass, genericClasses);
    }

}
