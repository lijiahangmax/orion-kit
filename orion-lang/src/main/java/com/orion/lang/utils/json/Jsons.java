package com.orion.lang.utils.json;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Strings;

import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * fastJson工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/19 00:07
 */
public class Jsons {

    private Jsons() {
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
        return JSON.toJSONString(bean);
    }

    /**
     * bean -> json
     *
     * @param bean 对象
     * @return ignore
     */
    public static String toJsonLog(Object bean) {
        if (bean == null) {
            return Strings.EMPTY;
        }
        return JSON.toJSONString(bean, WriteMapNullValue, PrettyFormat);
    }

    /**
     * bean -> json
     *
     * @param bean 对象
     * @return ignore
     */
    public static String toJsonWriteNull(Object bean) {
        if (bean == null) {
            return Strings.EMPTY;
        }
        return JSON.toJSONString(bean, WriteMapNullValue);
    }

    /**
     * bean -> json
     *
     * @param bean     对象
     * @param features ignore
     * @return ignore
     */
    public static String toJson(Object bean, SerializerFeature... features) {
        if (bean == null) {
            return Strings.EMPTY;
        } else {
            if (features == null) {
                return JSON.toJSONString(bean);
            } else {
                return JSON.toJSONString(bean, features);
            }
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
        if (Strings.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, targetClass);
    }

    /**
     * json -> bean
     *
     * @param json        json
     * @param targetClass ignore
     * @param features    ignore
     * @param <T>         ignore
     * @return ignore
     */
    public static <T> T toBean(String json, Class<T> targetClass, Feature... features) {
        if (Strings.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, targetClass, features);
    }

    /**
     * json -> list
     *
     * @param json        json
     * @param targetClass ignore
     * @param <T>         ignore
     * @return ignore
     */
    public static <T> List<T> toList(String json, Class<T> targetClass) {
        if (Strings.isBlank(json)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(json, targetClass);
    }

    /**
     * json -> set
     *
     * @param json  json
     * @param clazz clazz
     * @param <T>   T
     * @return ignore
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        if (Strings.isBlank(json)) {
            return new HashSet<>(Const.CAPACITY_16);
        }
        return JSON.parseObject(json, new TypeReference<Set<T>>(clazz) {
        });
    }

    /**
     * json -> map
     *
     * @param json json
     * @return ignore
     */
    public static Map<String, Object> toMap(String json) {
        if (Strings.isBlank(json)) {
            return new HashMap<>(Const.CAPACITY_16);
        }
        return JSON.parseObject(json).getInnerMap();
    }

    /**
     * json -> map
     *
     * @param json json
     * @param kc   key class
     * @param vc   value class
     * @param <K>  K
     * @param <V>  V
     * @return ignore
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kc, Class<V> vc) {
        if (Strings.isBlank(json)) {
            return new HashMap<>(Const.CAPACITY_16);
        }
        return JSON.parseObject(json, new TypeReference<Map<K, V>>(kc, vc) {
        });
    }

    /**
     * json -> JSONObject
     *
     * @param json json
     * @return ignore
     */
    public static JSONObject toJsonObject(String json) {
        if (Strings.isBlank(json)) {
            return new JSONObject();
        }
        return JSON.parseObject(json);
    }

    /**
     * json -> object
     *
     * @param json json
     * @param ref  ref
     * @param <T>  T
     * @return object
     */
    public static <T> T toJsonObject(String json, TypeReference<T> ref) {
        return JSON.parseObject(json, ref);
    }

    /**
     * json -> JSONArray
     *
     * @param json json
     * @return ignore
     */
    public static JSONArray toJsonArray(String json) {
        if (Strings.isBlank(json)) {
            return new JSONArray();
        }
        return JSON.parseArray(json);
    }

    /**
     * 创建jsonp
     */
    public static String createJsonp(String function, Object... params) {
        JSONPObject call = new JSONPObject();
        call.setFunction(function);
        if (params != null) {
            for (Object param : params) {
                call.addParameter(param);
            }
        }
        return call.toString();
    }

    /**
     * json编码 (加 /)
     *
     * @param json json
     * @return json
     */
    public static String encode(String json) {
        if (Strings.isBlank(json)) {
            return Strings.EMPTY;
        }
        json = JSON.toJSONString(json);
        if (Strings.isBlank(json)) {
            return Strings.EMPTY;
        }
        return json.substring(1, json.length() - 1);
    }

    /**
     * json解码 (去 /)
     *
     * @param json json
     * @return json
     */
    public static String decode(String json) {
        if (Strings.isBlank(json)) {
            return Strings.EMPTY;
        }
        return json.replaceAll("\\\\", Strings.EMPTY);
    }

    /**
     * 压缩json 默认是压缩过的, 用于压缩 PrettyFormat
     *
     * @param json json
     * @return json
     */
    public static String compress(String json) {
        if (Strings.isBlank(json)) {
            return Strings.EMPTY;
        }
        return json.replaceAll(Const.LF, Strings.EMPTY)
                .replaceAll(Const.CR, Strings.EMPTY);
    }

}
