package com.orion.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.orion.utils.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * fastJson工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/19 To0:07
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
    public static String toJSON(Object bean) {
        if (bean == null) {
            return "";
        }
        return JSON.toJSONString(bean);
    }

    /**
     * bean -> json
     *
     * @param bean 对象
     * @return ignore
     */
    public static String toJSONLog(Object bean) {
        if (bean == null) {
            return "";
        }
        return JSON.toJSONString(bean, WriteMapNullValue, PrettyFormat);
    }

    /**
     * bean -> json
     *
     * @param bean 对象
     * @return ignore
     */
    public static String toJSONWriteNull(Object bean) {
        if (bean == null) {
            return "";
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
    public static String toJSON(Object bean, SerializerFeature... features) {
        if (null == bean) {
            return "";
        } else {
            if (null == features) {
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
        if (Strings.isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, targetClass);
        } catch (Exception e) {
            return null;
        }
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
        if (Strings.isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, targetClass, features);
        } catch (Exception e) {
            return null;
        }
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
        if (Strings.isEmpty(json)) {
            return new ArrayList<>();
        }
        try {
            return JSON.parseArray(json, targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> map
     *
     * @param json json
     * @return ignore
     */
    public static Map<String, Object> toMap(String json) {
        if (Strings.isEmpty(json)) {
            return new HashMap<>(16);
        }
        try {
            return JSON.parseObject(json).getInnerMap();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> JSONObject
     *
     * @param json json
     * @return ignore
     */
    public static JSONObject toJSONObject(String json) {
        if (Strings.isEmpty(json)) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> JSONArray
     *
     * @param json json
     * @return ignore
     */
    public static JSONArray toJSONArray(String json) {
        if (Strings.isEmpty(json)) {
            return new JSONArray();
        }
        try {
            return JSON.parseArray(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建jsonp
     */
    public static String createJSONP(String function, Object... params) {
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
        if (Strings.isEmpty(json)) {
            return "";
        }
        json = JSON.toJSONString(json);
        if (Strings.isEmpty(json)) {
            return "";
        }
        json = json.substring(1, json.length() - 1);
        return json;
    }

    /**
     * json解码 (去 /)
     *
     * @param json json
     * @return json
     */
    public static String decode(String json) {
        if (Strings.isEmpty(json)) {
            return "";
        }
        return json.replaceAll("\\\\", "");
    }

    /**
     * 压缩json 默认是压缩过的, 用于压缩 PrettyFormat
     *
     * @param json json
     * @return json
     */
    public static String compress(String json) {
        if (Strings.isEmpty(json)) {
            return "";
        }
        return json.replaceAll("\n", "")
                .replaceAll("\r", "");
    }

}
