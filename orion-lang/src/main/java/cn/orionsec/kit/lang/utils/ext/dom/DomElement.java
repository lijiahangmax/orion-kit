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
package cn.orionsec.kit.lang.utils.ext.dom;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XML 构建信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/25 14:04
 */
public class DomElement implements Serializable {

    /**
     * 标签名称
     */
    private String name;

    /**
     * 属性集
     */
    private Map<String, String> attributes;

    /**
     * 子节点
     */
    private List<DomElement> childNode;

    /**
     * 值
     */
    private String value;

    /**
     * 是否使用 cdata
     */
    private boolean cdata;

    public DomElement(String name) {
        this(name, null);
    }

    public DomElement(String name, String value) {
        this.name = name;
        this.value = value;
        this.cdata = false;
    }

    public String getName() {
        return name;
    }

    public DomElement setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public DomElement addAttributes(String key, String value) {
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }

    public DomElement setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public DomElement newChildNode(String name) {
        if (value != null) {
            throw Exceptions.argument("value is not null");
        }
        if (this.childNode == null) {
            this.childNode = new ArrayList<>();
        }
        DomElement domElement = new DomElement(name);
        this.childNode.add(domElement);
        return domElement;
    }

    public List<DomElement> getChildNode() {
        return childNode;
    }

    public DomElement addChildNode(DomElement child) {
        if (value != null) {
            throw Exceptions.argument("value is not null");
        }
        if (this.childNode == null) {
            this.childNode = new ArrayList<>();
        }
        this.childNode.add(child);
        return this;
    }

    public DomElement setChildNode(List<DomElement> childNode) {
        if (value != null) {
            throw Exceptions.argument("value is not null");
        }
        this.childNode = childNode;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DomElement setValue(String value) {
        if (childNode != null) {
            throw Exceptions.argument("childNode is not null");
        }
        this.value = value;
        return this;
    }

    public DomElement cdata() {
        this.cdata = true;
        return this;
    }

    public boolean isCdata() {
        return cdata;
    }

}