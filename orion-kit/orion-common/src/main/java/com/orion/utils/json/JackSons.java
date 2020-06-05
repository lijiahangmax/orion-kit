package com.orion.utils.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.utils.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson 工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/2 10:23
 */
public class JackSons {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new JsonFactory());

    private JackSons() {
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
        try {
            return OBJECT_MAPPER.writeValueAsString(bean);
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
            return OBJECT_MAPPER.readValue(json, targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> list
     * value 为 LinkedHashMap
     *
     * @param json json
     * @param <T>  ignore
     * @return ignore
     */
    public static <T> List<T> toList(String json) {
        if (Strings.isEmpty(json)) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json -> map
     * value 为 LinkedHashMap
     *
     * @param json json
     * @return ignore
     */
    public static Map<String, Object> toMap(String json) {
        if (Strings.isEmpty(json)) {
            return new HashMap<>(16);
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

}
