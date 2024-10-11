/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
