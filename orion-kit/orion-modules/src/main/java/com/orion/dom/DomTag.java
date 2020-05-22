package com.orion.dom;

import com.orion.utils.Exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XML构建信息
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/25 14:04
 */
@SuppressWarnings("ALL")
public class DomTag implements Serializable {

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
    private List<DomTag> childNode;

    /**
     * 值
     */
    private String value;

    /**
     * 是否使用 cdata
     */
    private Boolean cdata = false;

    public DomTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DomTag setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public DomTag addAttributes(String key, String value) {
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }

    public DomTag setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public DomTag newChildNode(String name) {
        if (value == null) {
            if (this.childNode == null) {
                this.childNode = new ArrayList<>();
            }
            DomTag domTag = new DomTag(name);
            this.childNode.add(domTag);
            return domTag;
        }
        throw Exceptions.argument("value is not null");
    }

    public List<DomTag> getChildNode() {
        return childNode;
    }

    public DomTag addChildNode(DomTag child) {
        if (value == null) {
            if (this.childNode == null) {
                this.childNode = new ArrayList<>();
            }
            this.childNode.add(child);
            return this;
        }
        throw Exceptions.argument("value is not null");
    }

    public DomTag setChildNode(List<DomTag> childNode) {
        if (value == null) {
            this.childNode = childNode;
            return this;
        }
        throw Exceptions.argument("value is not null");
    }

    public String getValue() {
        return value;
    }

    public DomTag setValue(String value) {
        if (childNode == null) {
            this.value = value;
            return this;
        }
        throw Exceptions.argument("childNode is not null");
    }

    public DomTag cdata() {
        cdata = true;
        return this;
    }

    public Boolean getCdata() {
        return cdata;
    }

}