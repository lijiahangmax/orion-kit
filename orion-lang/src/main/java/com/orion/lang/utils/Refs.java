package com.orion.lang.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.orion.lang.define.wrapper.Ref;

/**
 * ref 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/19 18:07
 */
public class Refs {

    private Refs() {
    }

    /**
     * 转为 ref json
     *
     * @param o o
     * @return json
     */
    public static String json(Object o) {
        return Ref.of(o).toString();
    }

    /**
     * ref json 转为 object
     *
     * @param json json
     * @return value
     */
    public static Object unref(String json) {
        Ref<?> ref = JSON.parseObject(json, Ref.class);
        if (ref == null) {
            return null;
        }
        return ref.getValue();
    }

    /**
     * ref json 转为 type
     *
     * @param json json
     * @param type type
     * @param <T>  T
     * @return value
     */
    public static <T> T unref(String json, Class<T> type) {
        Ref<T> ref = JSON.parseObject(json, new TypeReference<Ref<T>>(type) {
        });
        if (ref == null) {
            return null;
        }
        return ref.getValue();
    }

    /**
     * ref json 转为 type
     *
     * @param json json
     * @param type type
     * @param <T>  T
     * @return value
     */
    public static <T> T unref(String json, TypeReference<Ref<T>> type) {
        Ref<T> ref = JSON.parseObject(json, type);
        if (ref == null) {
            return null;
        }
        return ref.getValue();
    }

    /**
     * ref json 转为 string
     *
     * @param json json
     * @return value
     */
    public static String unrefToString(String json) {
        Ref<?> ref = JSON.parseObject(json, Ref.class);
        if (ref == null) {
            return null;
        }
        return ref.getValue().toString();
    }

}
