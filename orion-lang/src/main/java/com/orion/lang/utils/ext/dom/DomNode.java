package com.orion.lang.utils.ext.dom;

import com.orion.lang.define.collect.MutableArrayList;
import com.orion.lang.define.collect.MutableHashMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * XML 解析存储
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/26 21:41
 */
@SuppressWarnings("ALL")
public class DomNode implements Serializable {

    /**
     * 值
     */
    private Object value;

    /**
     * 属性
     */
    private Map<String, String> attr;

    DomNode(Object value) {
        this.value = value;
    }

    public Object getValueClass() {
        return value.getClass();
    }

    public <V> MutableHashMap<String, V> getMapValue() {
        if (value instanceof Map) {
            return new MutableHashMap<>(((Map) value));
        } else {
            return null;
        }
    }

    public <T> MutableArrayList<T> getListValue() {
        if (value instanceof List) {
            return new MutableArrayList<>(((List) value));
        } else {
            return null;
        }
    }

    public String getStringValue() {
        return value.toString();
    }

    public <T> T getValue() {
        return (T) value;
    }

    public DomNode setValue(Object value) {
        this.value = value;
        return this;
    }

    public Map<String, String> getAttr() {
        return attr;
    }

    public DomNode setAttr(Map<String, String> attr) {
        this.attr = attr;
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
