package com.orion.dom;

import com.orion.able.Jsonable;
import com.orion.lang.collect.ConvertArrayList;
import com.orion.lang.collect.ConvertHashMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * XML解析存储
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/26 21:41
 */
@SuppressWarnings("ALL")
public class DomNode implements Serializable, Jsonable {

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

    public ConvertHashMap getMapValue() {
        if (value instanceof Map) {
            return new ConvertHashMap(((Map) value));
        } else {
            return null;
        }
    }

    public ConvertArrayList getListValue() {
        if (value instanceof List) {
            return new ConvertArrayList(((List) value));
        } else {
            return null;
        }
    }

    public String getStringValue() {
        return value.toString();
    }

    public Object getValue() {
        return value;
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
        return toJSON();
    }

    @Override
    public String toJsonString() {
        return toJSON();
    }

}
